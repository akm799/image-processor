package com.test.image.processors.track.opt.mshift.impl;

import com.test.image.processors.track.opt.mshift.ColourHistogram;
import com.test.image.test.ColourHistogramDataAssertion;

abstract class AbstractColourHistogram implements ColourHistogram {
    private static final int RED_INDEX = 0;
    private static final int GREEN_INDEX = 1;
    private static final int BLUE_INDEX = 2;
    private static final int X_INDEX = 0;
    private static final int Y_INDEX = 1;

    private final int binWidth;

    private final int[][][] data;

    private final int[] rgbValues = new int[3];

    AbstractColourHistogram(int binWidth) {
        final int maxNo = 256;
        final int n = maxNo/binWidth + maxNo%binWidth;
        this.binWidth = binWidth;
        this.data = new int[n][n][n];
    }

    @Override
    public final void fill(int[][] imagePixels) {
        final int w = imagePixels[0].length;
        for (int[] row : imagePixels) {
            for (int i=0; i<w; i++) {
                getRgbValues(row[i], rgbValues);
                final int ri = rgbValues[RED_INDEX]   / binWidth;
                final int gi = rgbValues[GREEN_INDEX] / binWidth;
                final int bi = rgbValues[BLUE_INDEX]  / binWidth;
                data[ri][gi][bi]++;
            }
        }
    }

    @Override
    public final int findSimilarityScore(int[][] segmentPixels) {
        final int h = segmentPixels.length;
        final int w = segmentPixels[0].length;

        int sumOfWeights = 0;
        for (int j=0 ; j<h ; j++) {
            for (int i=0 ; i<w ; i++) {
                getRgbValues(segmentPixels[j][i], rgbValues);
                final int ri = rgbValues[RED_INDEX]/binWidth;
                final int gi = rgbValues[GREEN_INDEX]/binWidth;
                final int bi = rgbValues[BLUE_INDEX]/binWidth;
                sumOfWeights += data[ri][gi][bi];
            }
        }

        return sumOfWeights;
    }

    @Override
    public final void findSimilarityCentre(int[][] segmentPixels, int[] centre) {
        final int h = segmentPixels.length;
        final int w = segmentPixels[0].length;

        int xc = 0;
        int yc = 0;
        int sumOfWeights = 0;
        for (int j=0 ; j<h ; j++) {
            for (int i=0 ; i<w ; i++) {
                getRgbValues(segmentPixels[j][i], rgbValues);
                final int ri = rgbValues[RED_INDEX]/binWidth;
                final int gi = rgbValues[GREEN_INDEX]/binWidth;
                final int bi = rgbValues[BLUE_INDEX]/binWidth;
                final int weight = data[ri][gi][bi];
                xc += weight*i;
                yc += weight*j;
                sumOfWeights += weight;
            }
        }

        final float sum = sumOfWeights;
        centre[X_INDEX] = Math.round(xc/sum);
        centre[Y_INDEX] = Math.round(yc/sum);
    }

    abstract void getRgbValues(int rgb, int[] rgbValues);

    @Override
    public void assertData(ColourHistogramDataAssertion assertion) {
        assertion.assertData(data);
    }
}
