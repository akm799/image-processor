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
    private static final int D_PIXEL_TOLERANCE = 0;
    private static final int N_ITERATIONS_MAX = 100;

    private final ColouredWindow targetWindow;
    private final ColouredWindow initialOffTargetWindow;
    private final int trackingWindowColour;

    private final int nSideDivs;
    private final int nSideDivsSq;
    private final float binWidth;
    private final int[] colourHistogram;

    private final boolean track;
    private final boolean normaliseColours;

    private int[][] weights;
    private ColouredWindow trackingWindow;

    /**
     * @param targetWindow the window which we want to track
     * @param initialOffTargetWindow our initial window which is the same size as our initial window but centered somewhere else so as to contain only a part of the initial window
     * @param trackingWindowColour the colour of the window we will find when we process the image
     * @param nDivisionsInColourSide the number of divisions in each side of our colour cube
     */
    public ColourTrackImageProcessor(ColouredWindow targetWindow, ColouredWindow initialOffTargetWindow, int trackingWindowColour, int nDivisionsInColourSide) {
        this(targetWindow, initialOffTargetWindow, trackingWindowColour, nDivisionsInColourSide, false, false);
    }

    /**
     * @param targetWindow the window which we want to track
     * @param initialOffTargetWindow our initial window which is the same size as our initial window but centered somewhere else so as to contain only a part of the initial window
     * @param trackingWindowColour the colour of the window we will find when we process the image
     * @param nDivisionsInColourSide the number of divisions in each side of our colour cube
     * @param normaliseColours if true the colours of the input image will be normalised before processing
     * @param noTracking if true no tracking will be done but only the initial and off-centre windows will be shown
     */
    public ColourTrackImageProcessor(ColouredWindow targetWindow, ColouredWindow initialOffTargetWindow, int trackingWindowColour, int nDivisionsInColourSide, boolean normaliseColours, boolean noTracking) {
        checkArgs(targetWindow, initialOffTargetWindow, trackingWindowColour, nDivisionsInColourSide);

        this.targetWindow = targetWindow;
        this.initialOffTargetWindow = initialOffTargetWindow;
        this.trackingWindow = new ColouredWindow(initialOffTargetWindow, trackingWindowColour);
        this.trackingWindowColour = trackingWindowColour;
        this.normaliseColours = normaliseColours;
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
        /*
        debugShift(image, initialOffTargetWindow);

        return image;
         */
        if (track) {
            shiftTowardsTheInitialWindow(image);
        }

        return composeFinalImage(image);
    }

    private void shiftTowardsTheInitialWindow(BufferedImage image) {
        if (normaliseColours) {
            normalizeColours(image);
        }

        fillColourHistogramForWindow(image, targetWindow);
        weights = new int[trackingWindow.height + 1][trackingWindow.width + 1];

        int n = 0;
        boolean notConverged = true;
        while (notConverged && n < N_ITERATIONS_MAX) {
            notConverged = !shiftCentre(image, trackingWindow);
            n++;
        }
    }

    private void normalizeColours(BufferedImage image) {
        for (int j=0 ; j<image.getHeight() ; j++) {
            for (int i=0 ; i<image.getWidth() ; i++) {
                final int rgb = image.getRGB(i, j);
                final int red = ColourHelper.getRed(rgb);
                final int green = ColourHelper.getGreen(rgb);
                final int blue = ColourHelper.getBlue(rgb);
                final int intensity = red + green + blue;
                if (intensity > 0) {
                    final int r = MAX_COLOUR_VALUE_INT * red / intensity;
                    final int g = MAX_COLOUR_VALUE_INT * green / intensity;
                    final int b = MAX_COLOUR_VALUE_INT * blue / intensity;
                    image.setRGB(i, j, ColourHelper.getRgb(r, g, b));
                }
            }
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

    // Shifts the input window towards the target window once only, producing an image with information useful for debugging.
    private void debugShift(BufferedImage image, Window window) {
        if (normaliseColours) {
            normalizeColours(image);
        }

        fillColourHistogramForWindow(image, targetWindow);
        weights = new int[window.height + 1][window.width + 1];

        // Calculate the weights.
        int maxWeight = 0;
        for (int j=window.yMin ; j<=window.yMax ; j++) {
            final int wy = j - window.yMin;
            for (int i=window.xMin ; i<=window.xMax ; i++) {
                final int rgb = image.getRGB(i, j);
                final int binIndex = findBinIndexForColour(rgb);
                final int weight = colourHistogram[binIndex];
                weights[wy][i - window.xMin] = weight;
                if (weight > maxWeight) {
                    maxWeight = weight;
                }
            }
        }

        // Calculate the weighted sums.
        int xSum = 0;
        int ySum = 0;
        float sumOfWeights = 0;
        final float maxWeightFloat = (float)maxWeight;
        for (int j=window.yMin ; j<=window.yMax ; j++) {
            final int wy = j - window.yMin;
            for (int i=window.xMin ; i<=window.xMax ; i++) {
                final float weight = weights[wy][i - window.xMin]/maxWeightFloat;
                xSum += weight*i;
                ySum += weight*j;
                sumOfWeights += weight;

                // Paint the window pixels in grayscale using the weight as the intensity, for debugging purposes.
                final int scale = Math.round(MAX_COLOUR_VALUE_INT*weight);
                image.setRGB(i, j, ColourHelper.getRgb(scale, scale, scale));
            }
        }

        final int x = Math.round(xSum/sumOfWeights);
        final int y = Math.round(ySum/sumOfWeights);
        final int dx = x - (window.xMin + window.width/2);
        final int dy = y - (window.yMin + window.height/2);
        System.out.println("Shift: (" + dx + ", " + dy + ")");

        // Draw the new, shifted, centre for debugging purposes.
        for (int j=window.yMin ; j<=window.yMax ; j++) {
            for (int i=window.xMin ; i<=window.xMax ; i++) {
                if (j == y || x == i) {
                    image.setRGB(i, j, ColourHelper.getRgb(MAX_COLOUR_VALUE_INT, MAX_COLOUR_VALUE_INT, MAX_COLOUR_VALUE_INT));
                }
            }
        }
    }

    private boolean shiftCentre(BufferedImage image, Window window) {
        // Calculate the weights.
        int maxWeight = 0;
        for (int j=window.yMin ; j<=window.yMax ; j++) {
            final int wy = j - window.yMin;
            for (int i=window.xMin ; i<=window.xMax ; i++) {
                final int rgb = image.getRGB(i, j);
                final int binIndex = findBinIndexForColour(rgb);
                final int weight = colourHistogram[binIndex];
                weights[wy][i - window.xMin] = weight;
                if (weight > maxWeight) {
                    maxWeight = weight;
                }
            }
        }

        // Calculate the weighted sums.
        int xSum = 0;
        int ySum = 0;
        float sumOfWeights = 0;
        final float maxWeightFloat = (float)maxWeight;
        for (int j=window.yMin ; j<=window.yMax ; j++) {
            final int wy = j - window.yMin;
            for (int i=window.xMin ; i<=window.xMax ; i++) {
                final float weight = weights[wy][i - window.xMin]/maxWeightFloat;
                xSum += weight*i;
                ySum += weight*j;
                sumOfWeights += weight;
            }
        }

        final int x = Math.round(xSum/sumOfWeights);
        final int y = Math.round(ySum/sumOfWeights);
        final int dx = x - (window.xMin + window.width/2);
        final int dy = y - (window.yMin + window.height/2);
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
            return Arrays.asList(targetWindow, initialOffTargetWindow, trackingWindow);
        } else {
            return Arrays.asList(targetWindow, initialOffTargetWindow);
        }
    }
}
