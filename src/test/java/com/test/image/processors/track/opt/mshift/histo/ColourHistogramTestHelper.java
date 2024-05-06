package com.test.image.processors.track.opt.mshift.histo;

import com.test.image.processors.track.opt.mshift.ColourHistogram;

import java.awt.*;

class ColourHistogramTestHelper {

    static void fillColourHistogramWithSingleColour(ColourHistogram underTest, int w, int h, int singleColour, Rectangle targetRegion) {
        final int[][] imagePixels = imagePixels(w, h, singleColour, targetRegion);
        underTest.fill(imagePixels, targetRegion);
    }

    private static int[][] imagePixels(int w, int h, int singleColour, Rectangle targetRegion) {
        final int[][] imagePixels = new int[h][w];
        final int xMax = targetRegion.x + targetRegion.width;
        final int yMax = targetRegion.y + targetRegion.height;
        for (int j=targetRegion.y ; j<yMax ; j++) {
            for (int i= targetRegion.x ; i<xMax ; i++) {
                imagePixels[j][i] = singleColour;
            }
        }

        return imagePixels;
    }

    private ColourHistogramTestHelper() {}
}
