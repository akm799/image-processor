package com.test.image.processors.track.opt.mshift;

import com.test.image.util.ColourHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

abstract class AbstractColourHistogramScoreTest {
    private final int binWidth = 5;
    private final Random random = new Random(System.currentTimeMillis());
    private final int singleColour = ColourHelper.getRgb(255, 0, 0);

    private ColourHistogram underTest;

    private final int w = 5;
    private final int h = 5;

    @Before
    public void setup() {
        underTest = instance(binWidth);
        ColourHistogramTestHelper.fillColourHistogramWithSingleColour(underTest, w, h, singleColour);
    }

    abstract ColourHistogram instance(int binWidth);

    @Test
    public void shouldFindScore() {
        final int[][] segmentPixels = new int[h][w];

        final int score0 = underTest.findSimilarityScore(segmentPixels);
        Assert.assertEquals(0, score0);

        fillSingleColour(segmentPixels, 10);
        final int score1 = underTest.findSimilarityScore(segmentPixels);
        Assert.assertTrue(score1 > score0);

        clear(segmentPixels);
        fillSingleColour(segmentPixels, 20);
        final int score2 = underTest.findSimilarityScore(segmentPixels);
        Assert.assertTrue(score2 > score1);
    }

    private void clear(int[][] segmentPixels) {
        for (int[] row : segmentPixels) {
            Arrays.fill(row, 0);
        }
    }

    private void fillSingleColour(int[][] segmentPixels, int nTimes) {
        final int maxTimes = w * h;
        if (nTimes > maxTimes) {
            nTimes = maxTimes;
        }

        int i, j = 0, n = 0;
        while (j < h && n < nTimes) {
            i = 0;
            while ((i < w && n < nTimes)) {
                segmentPixels[j][i++] = singleColour;
                n++;
            }
            j++;
        }
    }
}
