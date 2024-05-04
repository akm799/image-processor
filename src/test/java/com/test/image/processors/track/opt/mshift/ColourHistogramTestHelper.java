package com.test.image.processors.track.opt.mshift;

import java.util.Arrays;

class ColourHistogramTestHelper {

    static void fillColourHistogramWithSingleColour(ColourHistogram underTest, int w, int h, int singleColour) {
        final int[][] targetPixels = singleColourTargetPixels(w, h, singleColour);
        underTest.fill(targetPixels);
    }

    private static int[][] singleColourTargetPixels(int w, int h, int singleColour) {
        final int[][] targetPixels = new int[h][w];
        for (int[] row : targetPixels) {
            Arrays.fill(row, singleColour);
        }

        return targetPixels;
    }

    private ColourHistogramTestHelper() {}
}
