package com.test.image.processors.track.opt.mshift;

public interface ColourHistogram {
    void fill(int[][] imagePixels);
    void findSimilarityCentre(int[][] segmentPixels, int[] centre);
}
