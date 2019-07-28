package com.test.image.processors.track;

import com.test.image.AbstractFileImageProcessor;
import com.test.image.ImageProcessor;
import com.test.image.processors.track.shift.ColourCubeHistogram;
import com.test.image.processors.track.shift.ColourSimilarity;
import com.test.image.processors.track.shift.MutableColourCubeHistogram;
import com.test.image.processors.track.shift.impl.MutableColourCubeHistogramImpl;
import com.test.image.processors.window.ColouredWindow;
import com.test.image.processors.window.Window;
import com.test.image.processors.window.WindowImageProcessor;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

/**
 * This processor will take a reference window and then fully cover the input image in adjacent windows of equal size
 * in order to find the window with the highest similarity to the reference one. It will, then, return an image which
 * is a copy of the input image, with the reference and highest similarity windows superimposed.
 *
 * Created by Thanos Mavroidis on 28/07/2019.
 */
public final class TopSimilarityImageProcessor extends AbstractFileImageProcessor {
    private final int nDivsInSide = 51;

    private final int targetColour;
    private final int highestSimilarityColour;
    private final Window targetWindow;

    public TopSimilarityImageProcessor(Window targetWindow, int targetColour, int highestSimilarityColour) {
        this.targetColour = targetColour;
        this.highestSimilarityColour = highestSimilarityColour;
        this.targetWindow = targetWindow;
    }

    @Override
    public String getDescription() {
        return "Determined highest similarity window.";
    }

    @Override
    public BufferedImage processImage(BufferedImage image) {
        final Window mostSimilarWindow = findMostSimilarWindow(image);
        final Collection<ColouredWindow> windows = Arrays.asList(new ColouredWindow(targetWindow, targetColour), new ColouredWindow(mostSimilarWindow, highestSimilarityColour));
        final ImageProcessor windowImageProcessor = new WindowImageProcessor(windows);

        return windowImageProcessor.processImage(image);
    }

    private ColourCubeHistogram buildColourHistogramForWindow(BufferedImage image, Window window) {
        final MutableColourCubeHistogram histogram = new MutableColourCubeHistogramImpl(window.width, window.height, nDivsInSide);
        for (int j=window.yMin ; j<=window.yMax ; j++) {
            for (int i=window.xMin ; i<=window.xMax ; i++) {
                final int pixelIndex = (j - window.yMin)*window.width + (i - window.xMin);
                final int rgb = image.getRGB(i, j);
                histogram.add(pixelIndex, rgb);
            }
        }

        return histogram;
    }

    private Window findMostSimilarWindow(BufferedImage image) {
        final ColourCubeHistogram targetHistogram = buildColourHistogramForWindow(image, targetWindow);

        float highestSimilarity = -1;
        Window mostSimilarWindow = null;

        final Iterator<Window> iterator = new WindowsIterator(image, targetWindow);
        while (iterator.hasNext()) {
            final Window testWindow = iterator.next();
            final ColourCubeHistogram testHistogram = buildColourHistogramForWindow(image, testWindow);
            final float similarity = ColourSimilarity.findSimilarity(targetHistogram, testHistogram);
            if (similarity > highestSimilarity) {
                highestSimilarity = similarity;
                mostSimilarWindow = testWindow;
            }
        }

        return mostSimilarWindow;
    }

    private static final class WindowsIterator implements Iterator<Window> {
        private final int imageWidth;
        private final int imageHeight;

        private final int windowWidth;
        private final int windowHeight;

        private final int nWidths;
        private final int nHeights;
        private final int nWindows;

        private final Rectangle rectangle = new Rectangle();

        private int windowsCounter = 0;

        WindowsIterator(BufferedImage image, Window window) {
            imageWidth = image.getWidth();
            imageHeight = image.getHeight();

            windowWidth = window.width;
            windowHeight = window.height;

            nWidths = partsIn(image.getWidth(), window.width);
            nHeights = partsIn(image.getHeight(), window.height);
            nWindows = nWidths*nHeights;
        }

        @Override
        public boolean hasNext() {
            return windowsCounter < nWindows;
        }

        @Override
        public Window next() {
            final int xIndex = windowsCounter%nWidths;
            final int yIndex = windowsCounter/nWidths;

            final int left = xIndex*windowWidth;
            final int top = yIndex*windowHeight;

            final int safeLeft = (left + windowWidth <= imageWidth ? left : imageWidth - windowWidth);
            final int safeTop = (top + windowHeight <= imageHeight ? top : imageHeight - windowHeight);

            rectangle.x = safeLeft;
            rectangle.y = safeTop;
            rectangle.width = windowWidth;
            rectangle.height = windowHeight;

            windowsCounter++;

            return new Window(rectangle);
        }

        private int partsIn(int large, int small) {
            return (large/small + (large%small == 0 ? 0 : 1));
        }
    }
}
