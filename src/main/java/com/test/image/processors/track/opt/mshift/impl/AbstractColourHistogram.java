package com.test.image.processors.track.opt.mshift.impl;

import com.test.image.processors.track.opt.mshift.ColourHistogram;

abstract class AbstractColourHistogram implements ColourHistogram {
    private static final int RED_INDEX = 0;
    private static final int GREEN_INDEX = 1;
    private static final int BLUE_INDEX = 2;
    private static final int X_INDEX = 0;
    private static final int Y_INDEX = 1;

    private final int binWidth;

    private int total = 0;
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
                total++;
            }
        }
    }

    @Override
    public final void findSimilarityCentre(int[][] segmentPixels, int[] centre) {
        final int h = segmentPixels.length;
        final int w = segmentPixels[0].length;

        int xc = 0;
        int yc = 0;
        for (int j=0 ; j<h ; j++) {
            for (int i=0 ; i<w ; i++) {
                getRgbValues(segmentPixels[j][i], rgbValues);
                final int ri = rgbValues[RED_INDEX]/binWidth;
                final int gi = rgbValues[GREEN_INDEX]/binWidth;
                final int bi = rgbValues[BLUE_INDEX]/binWidth;
                final int weight = data[ri][gi][bi];
                xc += weight*i;
                yc += weight*j;
            }
        }

        final float sumOfWeights = total;
        centre[X_INDEX] = Math.round(xc/sumOfWeights);
        centre[Y_INDEX] = Math.round(yc/sumOfWeights);
    }

    abstract void getRgbValues(int rgb, int[] rgbValues);
}
