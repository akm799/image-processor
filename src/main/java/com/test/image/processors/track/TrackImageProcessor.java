package com.test.image.processors.track;

import com.test.image.AbstractFileImageProcessor;
import com.test.image.ImageProcessor;
import com.test.image.processors.track.shift.ColourCubeDifference;
import com.test.image.processors.track.shift.ColourCubeHistogram;
import com.test.image.processors.track.shift.ColourMeanShift;
import com.test.image.processors.track.shift.MutableColourCubeHistogram;
import com.test.image.processors.track.shift.impl.ColourCubeDifferenceImpl;
import com.test.image.processors.track.shift.impl.MutableColourCubeHistogramImpl;
import com.test.image.processors.window.Window;
import com.test.image.processors.window.WindowImageProcessor;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Collection;

/**
 * Created by Thanos Mavroidis on 10/07/2019.
 */
public final class TrackImageProcessor extends AbstractFileImageProcessor {
    private final int nDivsInSide = 51;

    private final Window initialWindow;
    private final Window offCentreWindow;

    public TrackImageProcessor(Rectangle initialWindow, Rectangle offCentreWindow) {
        this.initialWindow = new Window(initialWindow);
        this.offCentreWindow = new Window(offCentreWindow);
    }

    @Override
    public String getDescription() {
        return "Evaluated window towards initial one.";
    }

    @Override
    public BufferedImage processImage(BufferedImage image) {
        final Rectangle trackingRectangle = shiftTowardsTheInitialWindow(image);
        final Rectangle initialRectangle = new Rectangle(initialWindow.xMin, initialWindow.yMin, initialWindow.width, initialWindow.height);
        final Rectangle offCentreRectangle = new Rectangle(offCentreWindow.xMin, offCentreWindow.yMin, offCentreWindow.width, offCentreWindow.height);
        final Collection<Rectangle> windows = Arrays.asList(initialRectangle, trackingRectangle, offCentreRectangle);
        final ImageProcessor windowImageProcessor = new WindowImageProcessor(windows);

        return windowImageProcessor.processImage(image);
    }

    private Rectangle shiftTowardsTheInitialWindow(BufferedImage image) {
        final Point shift = calculateOffTargetNewCentre(image);

        return shiftWindow(shift, offCentreWindow);
    }

    //TODO Investigate why the y-shit seems to be working but the x-shift does not.
    private Point calculateOffTargetNewCentre(BufferedImage image) {
        final ColourCubeHistogram initial = buildColourHistogramForWindow(image, initialWindow);
        final ColourCubeHistogram offCentre = buildColourHistogramForWindow(image, offCentreWindow);
        final ColourCubeDifference comparison = new ColourCubeDifferenceImpl(initial, offCentre);

        return ColourMeanShift.shift(offCentre, comparison);
    }

    private ColourCubeHistogram buildColourHistogramForWindow(BufferedImage image, Window window) {
        final MutableColourCubeHistogram histogram = new MutableColourCubeHistogramImpl(window.width, window.height, nDivsInSide);
        for (int j=window.yMin ; j<=window.yMax ; j++) {
            for (int i=window.xMin ; i<=window.xMax ; i++) {
                final int pixelIndex = (j - window.yMin)*window.height + (i - window.xMin);
                final int rgb = image.getRGB(i, j);
                histogram.add(pixelIndex, rgb);
            }
        }

        return histogram;
    }

    private Rectangle shiftWindow(Point shift, Window window) {
        return new Rectangle(window.xMin + shift.x, window.yMin + shift.y, window.width, window.height);
    }
}
