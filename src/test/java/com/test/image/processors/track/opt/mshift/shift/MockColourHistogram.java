package com.test.image.processors.track.opt.mshift.shift;

import com.test.image.processors.track.opt.mshift.ColourHistogram;
import com.test.image.test.ColourHistogramDataAssertion;

import java.awt.*;

public final class MockColourHistogram implements ColourHistogram {
    private static final int X_INDEX = 0;
    private static final int Y_INDEX = 1;


    private final int[][] imagePixels;
    private final Rectangle targetRegion;

    private int i;
    private int[][] centres;

    MockColourHistogram(int[][] imagePixels, Rectangle targetRegion) {
        this.imagePixels = imagePixels;
        this.targetRegion = targetRegion;
    }

    @Override
    public void fill(int[][] imagePixels, Rectangle targetRegion) {}

    @Override
    public int findSimilarityScore(int[][] imagePixels, Rectangle targetRegion) {
        return 0;
    }

    void setCentres(int[][] centres) {
        checkCentres(centres);

        i = 0;
        this.centres = centres;
    }

    private void checkCentres(int[][] centres) {
        if (centres != null) {
            for (int i=0 ; i<centres.length ; i++) {
                if (centres[i] == null || centres[i].length != 2) {
                    throw new IllegalArgumentException("Illegal input centres[" + i + "] which is null or has a length other than 2.");
                }
            }
        }
    }

    @Override
    public void findSimilarityCentre(int[][] imagePixels, Rectangle targetRegion, int[] centre) {
        if (centres == null || centres.length == 0) {
            throw new IllegalStateException("No mock call expectations defined.");
        }

        if (this.imagePixels == imagePixels) {
            try {
                centre[X_INDEX] = centres[i][X_INDEX];
                centre[Y_INDEX] = centres[i++][Y_INDEX];
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new RuntimeException("Mock method called for the " + i + "th time but only " + this.centres.length + " such mock call expectations have been defined.");
            }
        }
    }

    @Override
    public void assertData(ColourHistogramDataAssertion assertion) {}
}
