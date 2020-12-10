package com.test.image.processors.track.mshift;

import com.test.image.AbstractFileImageProcessor;
import com.test.image.ImageProcessor;
import com.test.image.processors.window.ColouredWindow;
import com.test.image.processors.window.Window;
import com.test.image.processors.window.WindowImageProcessor;
import com.test.image.util.ColourHelper;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Collection;

/**
 * Created by Thanos Mavroidis on 08/12/2020.
 */
public final class ColourTrackImageProcessor extends AbstractFileImageProcessor  {
    private static final int MAX_COLOUR_VALUE_INT = 255;
    private static final float MAX_COLOUR_VALUE = (float)MAX_COLOUR_VALUE_INT;
    private static final int D_PIXEL_TOLERANCE = 3;
    private static final int N_ITERATIONS_MAX = 100;

    private final ColouredWindow initialWindow;
    private final ColouredWindow offCentreWindow;
    private final int trackingWindowColour;

    private final int nSideDivs;
    private final int nSideDivsSq;
    private final float binWidth;
    private final int[] colourHistogram;

    private final boolean track;

    private int x = 0;
    private int y = 0;
    private ColouredWindow trackingWindow;

    /**
     * @param initialWindow the window which we want to track
     * @param offCentreWindow our initial window which is the same size as our initial window but centered somewhere else so as to contain only a part of the initial window
     * @param trackingWindowColour the colour of the window we will find when we process the image
     * @param nDivisionsInColourSide the number of divisions in each side of our colour cube
     */
    public ColourTrackImageProcessor(ColouredWindow initialWindow, ColouredWindow offCentreWindow, int trackingWindowColour, int nDivisionsInColourSide) {
        this(initialWindow, offCentreWindow, trackingWindowColour, nDivisionsInColourSide, false);
    }

    /**
     * @param initialWindow the window which we want to track
     * @param offCentreWindow our initial window which is the same size as our initial window but centered somewhere else so as to contain only a part of the initial window
     * @param trackingWindowColour the colour of the window we will find when we process the image
     * @param nDivisionsInColourSide the number of divisions in each side of our colour cube
     * @param noTracking if true no tracking will be done but only the initial and off-centre windows will be shown
     */
    public ColourTrackImageProcessor(ColouredWindow initialWindow, ColouredWindow offCentreWindow, int trackingWindowColour, int nDivisionsInColourSide, boolean noTracking) {
        checkArgs(initialWindow, offCentreWindow, trackingWindowColour, nDivisionsInColourSide);

        this.initialWindow = initialWindow;
        this.offCentreWindow = offCentreWindow;
        this.trackingWindow = new ColouredWindow(offCentreWindow, trackingWindowColour);
        this.trackingWindowColour = trackingWindowColour;
        this.track = !noTracking;

        this.nSideDivs = nDivisionsInColourSide;
        this.nSideDivsSq = nSideDivs*nSideDivs;
        this.binWidth = MAX_COLOUR_VALUE/nSideDivs;
        this.colourHistogram = new int[nSideDivsSq*nSideDivs];
    }

    private void checkArgs(Window initialWindow, Window offCentreWindow, int trackingWindowColour, int nDivisionsInColourSide) {
        if (initialWindow == null || offCentreWindow == null) {
            throw new IllegalArgumentException("One or both input windows are null. No input windows are allowed to be null.");
        }

        if (!initialWindow.overlaps(offCentreWindow)) {
            throw new IllegalArgumentException("The initial and off-centre windows do not overlap. They must overlap, even by a small amount.");
        }

        if (!ColourHelper.isValidRgbColour(trackingWindowColour)) {
            throw new IllegalArgumentException("Invalid tracking window colour (" + trackingWindowColour + "). This colour must be a valid RGB colour.");
        }

        if (nDivisionsInColourSide <= 1) {
            throw new IllegalArgumentException("Illegal argument nDivisionsInColourSide=" + nDivisionsInColourSide + ". It must be greater than 1.");
        }
    }

    @Override
    public String getDescription() {
        return "Evaluated window towards initial one.";
    }

    @Override
    public BufferedImage processImage(BufferedImage image) {
        if (track) {
            shiftTowardsTheInitialWindow(image);
        }

        return composeFinalImage(image);
    }

    private void shiftTowardsTheInitialWindow(BufferedImage image) {
        int n = 0;
        boolean notConverged = true;
        while (notConverged && n < N_ITERATIONS_MAX) {
            fillColourHistogramForWindow(image, trackingWindow);
            notConverged = !shiftCentre(image, trackingWindow);
            n++;
        }
    }

    private void fillColourHistogramForWindow(BufferedImage image, Window window) {
        Arrays.fill(colourHistogram, 0);

        for (int j=window.yMin ; j<=window.yMax ; j++) {
            for (int i=window.xMin ; i<=window.xMax ; i++) {
                final int rgb = image.getRGB(i, j);
                final int binIndex = findBinIndexForColour(rgb);
                colourHistogram[binIndex]++;
            }
        }
    }

    private boolean shiftCentre(BufferedImage image, Window window) {
        int xSum = 0;
        int ySum = 0;
        int weightSum = 0;

        for (int j=window.yMin ; j<=window.yMax ; j++) {
            for (int i=window.xMin ; i<=window.xMax ; i++) {
                final int rgb = image.getRGB(i, j);
                final int binIndex = findBinIndexForColour(rgb);
                final int weight = colourHistogram[binIndex];
                xSum += weight * i;
                ySum += weight * j;
                weightSum += weight;
            }
        }

        final int x = xSum/weightSum;
        final int y = ySum/weightSum;
        final int dx = x - this.x;
        final int dy = y - this.y;

        this.x = x;
        this.y = y;
        this.trackingWindow = new ColouredWindow(trackingWindow.shift(dx, dy), trackingWindowColour);

        return (Math.abs(dx) <= D_PIXEL_TOLERANCE && Math.abs(dy) <= D_PIXEL_TOLERANCE);
    }

    private int findBinIndexForColour(int rgb) {
        final int rIndex = findSideBinIndex(ColourHelper.getRed(rgb));
        final int gIndex = findSideBinIndex(ColourHelper.getGreen(rgb));
        final int bIndex = findSideBinIndex(ColourHelper.getBlue(rgb));

        return bIndex*nSideDivsSq + gIndex*nSideDivs + rIndex;
    }

    private int findSideBinIndex(int rgbComponent) {
        if (rgbComponent == MAX_COLOUR_VALUE_INT) { // Include the 255 value in the last bin.
            return nSideDivs - 1;
        } else {
            return (int) (rgbComponent/binWidth);
        }
    }

    private BufferedImage composeFinalImage(BufferedImage image) {
        final Collection<ColouredWindow> windows = assembleDisplayWindows();
        final ImageProcessor windowImageProcessor = new WindowImageProcessor(windows);

        return windowImageProcessor.processImage(image);
    }

    private Collection<ColouredWindow> assembleDisplayWindows() {
        if (track) {
            return Arrays.asList(initialWindow, offCentreWindow, trackingWindow);
        } else {
            return Arrays.asList(initialWindow, offCentreWindow);
        }
    }
}
