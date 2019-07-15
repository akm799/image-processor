package com.test.image.processors.track;

import com.test.image.AbstractFileImageProcessor;
import com.test.image.ImageProcessor;
import com.test.image.processors.track.shift.ColourCubeDifference;
import com.test.image.processors.track.shift.ColourCubeHistogram;
import com.test.image.processors.track.shift.ColourMeanShift;
import com.test.image.processors.track.shift.MutableColourCubeHistogram;
import com.test.image.processors.track.shift.impl.ColourCubeDifferenceImpl;
import com.test.image.processors.track.shift.impl.MutableColourCubeHistogramImpl;
import com.test.image.processors.window.ColouredWindow;
import com.test.image.processors.window.Window;
import com.test.image.processors.window.WindowImageProcessor;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Collection;

/**
 * Created by Thanos Mavroidis on 10/07/2019.
 */
public final class IterativeTrackImageProcessor extends AbstractFileImageProcessor {
    private final int nDivsInSide = 51;

    private final int maxIterations = 10;
    private final double convergenceDistance = 1.5;

    private final ColouredWindow initialWindow;
    private final ColouredWindow offCentreWindow;
    private final int trackingWindowColour;

    private ColouredWindow trackingWindow;

    public IterativeTrackImageProcessor(ColouredWindow initialWindow, ColouredWindow offCentreWindow, int trackingWindowColour) {
        this.initialWindow = initialWindow;
        this.offCentreWindow = offCentreWindow;
        this.trackingWindowColour = trackingWindowColour;
    }

    @Override
    public String getDescription() {
        return "Evaluated window towards initial one.";
    }

    @Override
    public BufferedImage processImage(BufferedImage image) {
        shiftTowardsTheInitialWindow(image);

        final Collection<ColouredWindow> windows = Arrays.asList(initialWindow, trackingWindow, offCentreWindow);
        final ImageProcessor windowImageProcessor = new WindowImageProcessor(windows);

        return windowImageProcessor.processImage(image);
    }

    private void shiftTowardsTheInitialWindow(BufferedImage image) {
        Point shift = null;
        trackingWindow = offCentreWindow;

        int i = 0;
        while (haveNotConverged(shift) && i < maxIterations) {
            shift = calculateOffTargetNewCentre(image);
            if (shift != null) {
                trackingWindow = new ColouredWindow(shiftWindow(shift, trackingWindow), trackingWindowColour);
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

    private Point calculateOffTargetNewCentre(BufferedImage image) {
        final ColourCubeHistogram initial = buildColourHistogramForWindow(image, initialWindow);
        final ColourCubeHistogram offCentre = buildColourHistogramForWindow(image, trackingWindow);
        final ColourCubeDifference comparison = new ColourCubeDifferenceImpl(initial, offCentre);

        return ColourMeanShift.shift(offCentre, comparison);
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

    private Rectangle shiftWindow(Point shift, Window window) {
        return new Rectangle(window.xMin + shift.x, window.yMin + shift.y, window.width, window.height);
    }
}
