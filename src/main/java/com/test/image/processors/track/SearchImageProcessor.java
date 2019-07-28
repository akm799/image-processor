package com.test.image.processors.track;

import com.test.image.AbstractFileImageProcessor;
import com.test.image.ImageProcessor;
import com.test.image.processors.track.search.WindowsIterator;
import com.test.image.processors.track.shift.*;
import com.test.image.processors.track.shift.impl.ColourCubeDifferenceImpl;
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
 * Created by Thanos Mavroidis on 28/07/2019.
 */
public final class SearchImageProcessor extends AbstractFileImageProcessor {
    private final int nDivsInSide = 51;

    private final int maxIterations = 10;
    private final double convergenceDistance = 1.5;

    private final int bestMatchColour;
    private final ColouredWindow targetWindow;

    private Window trackingWindow;
    private Window bestMatchWindow;
    private ColourCubeHistogram targetColourDistribution;

    public SearchImageProcessor(ColouredWindow targetWindow, int bestMatchColour) {
        this.targetWindow = targetWindow;
        this.bestMatchColour = bestMatchColour;
    }

    @Override
    public String getDescription() {
        return "Found best match with the input window.";
    }

    @Override
    public BufferedImage processImage(BufferedImage image) {
        targetColourDistribution = buildColourHistogramForWindow(image, targetWindow);
        trackingWindow = findMostSimilarWindow(image);
        shiftTowardsTheTargetWindow(image);

        final Collection<ColouredWindow> windows = Arrays.asList(targetWindow, new ColouredWindow(bestMatchWindow, bestMatchColour));
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

    private void shiftTowardsTheTargetWindow(BufferedImage image) {
        Point shift = null;
        float highestSimilarity = -1;

        int i = 0;
        while (haveNotConverged(shift) && i < maxIterations) {
            shift = calculateNewBestCentre(image);
            if (shift != null) {
                trackingWindow = new Window(shiftWindow(shift, trackingWindow));
                final ColourCubeHistogram trackingColourDistribution = buildColourHistogramForWindow(image, trackingWindow);
                final float similarity = ColourSimilarity.findSimilarity(targetColourDistribution, trackingColourDistribution);
                if (similarity > highestSimilarity) {
                    highestSimilarity = similarity;
                    bestMatchWindow = trackingWindow;
                }
            }

            i++;
        }

        if (i == maxIterations) {
            System.out.println("Could not converge after " + i + " iterations.");
        }
    }

    private boolean haveNotConverged(Point shift) {
        if (shift == null) {
            return true;
        }

        return (Math.sqrt(shift.x*shift.x + shift.y*shift.y) > convergenceDistance);
    }

    private Point calculateNewBestCentre(BufferedImage image) {
        final ColourCubeHistogram trackingColourDistribution = buildColourHistogramForWindow(image, trackingWindow);
        final ColourCubeDifference comparison = new ColourCubeDifferenceImpl(targetColourDistribution, trackingColourDistribution);

        return ColourMeanShift.shift(trackingColourDistribution, comparison);
    }

    private Rectangle shiftWindow(Point shift, Window window) {
        return new Rectangle(window.xMin + shift.x, window.yMin + shift.y, window.width, window.height);
    }
}
