package com.test.image.processors.track.opt.mshift;

import com.test.image.test.ColourHistogramDataAssertion;

import java.awt.*;

public interface ColourHistogram {
    void fill(int[][] imagePixels, Rectangle targetRegion);
    int findSimilarityScore(int[][] imagePixels, Rectangle targetRegion);
    void findSimilarityCentre(int[][] imagePixels, Rectangle targetRegion, int[] centre);

    // This method is for test purposes only.
    void assertData(ColourHistogramDataAssertion assertion);
}
