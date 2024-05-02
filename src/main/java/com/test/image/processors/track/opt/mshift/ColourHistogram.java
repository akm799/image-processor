package com.test.image.processors.track.opt.mshift;

import com.test.image.test.ColourHistogramDataAssertion;

public interface ColourHistogram {
    void fill(int[][] imagePixels);
    void findSimilarityCentre(int[][] segmentPixels, int[] centre);

    // This method is for test purposes only.
    void assertData(ColourHistogramDataAssertion assertion);
}
