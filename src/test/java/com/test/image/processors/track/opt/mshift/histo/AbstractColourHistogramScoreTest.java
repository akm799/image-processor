package com.test.image.processors.track.opt.mshift.histo;

import com.test.image.processors.track.opt.mshift.ColourHistogram;
import com.test.image.util.ColourHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.util.Arrays;

abstract class AbstractColourHistogramScoreTest {
    private final int binWidth = 5;
    private final int singleColour = ColourHelper.getRgb(255, 0, 0);

    private ColourHistogram underTest;

    private final int w = 10;
    private final int h = 10;
    private final Rectangle targetRegion = new Rectangle(4, 4, 5, 5);

    @Before
    public void setup() {
        underTest = instance(binWidth);
        ColourHistogramTestHelper.fillColourHistogramWithSingleColour(underTest, w, h, singleColour, targetRegion);
    }

    abstract ColourHistogram instance(int binWidth);

    @Test
    public void shouldFindScore() {
        final int[][] imagePixels = new int[h][w];

        final int score0 = underTest.findSimilarityScore(imagePixels, targetRegion);
        Assert.assertEquals(0, score0);

        fillSingleColour(imagePixels, 10);
        final int score1 = underTest.findSimilarityScore(imagePixels, targetRegion);
        Assert.assertTrue(score1 > score0);

        clear(imagePixels);
        fillSingleColour(imagePixels, 20);
        final int score2 = underTest.findSimilarityScore(imagePixels, targetRegion);
        Assert.assertTrue(score2 > score1);
    }

    private void clear(int[][] imagePixels) {
        for (int[] row : imagePixels) {
            Arrays.fill(row, 0);
        }
    }

    private void fillSingleColour(int[][] imagePixels, int nTimes) {
        final int maxTimes = w * h;
        if (nTimes > maxTimes) {
            nTimes = maxTimes;
        }

        final int xMax = targetRegion.x + targetRegion.width;
        final int yMax = targetRegion.y + targetRegion.height;
        int i, j = targetRegion.y, n = 0;
        while (j < yMax && n < nTimes) {
            i = targetRegion.x;
            while ((i < xMax && n < nTimes)) {
                imagePixels[j][i++] = singleColour;
                n++;
            }
            j++;
        }
    }
}
