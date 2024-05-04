package com.test.image.processors.track.opt.mshift;

import com.test.image.util.ColourHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

abstract class AbstractColourHistogramShiftTest {
    private final int xIndex = 0;
    private final int yIndex = 1;
    private final int binWidth = 5;
    private final Random random = new Random(System.currentTimeMillis());
    private final int singleColour = ColourHelper.getRgb(255, 0, 0);

    private ColourHistogram underTest;

    private int w;
    private int h;

    @Before
    public void setup() {
        underTest = instance(binWidth);

        w = 3 + random.nextInt(2);
        h = 3 + random.nextInt(2);

        final int[][] targetPixels = singleColourTargetPixels(w, h, singleColour);
        underTest.fill(targetPixels);
    }

    abstract ColourHistogram instance(int binWidth);

    private int[][] singleColourTargetPixels(int w, int h, int singleColour) {
        final int[][] targetPixels = new int[h][w];
        for (int[] row : targetPixels) {
            Arrays.fill(row, singleColour);
        }

        return targetPixels;
    }

    @Test
    public void shouldShiftToSingleColourSinglePixel() {
        final int x = random.nextInt(w);
        final int y = random.nextInt(h);
        final int[][] segmentPixels = new int[h][w];
        segmentPixels[y][x] = singleColour;

        final int[] centre = new int[]{-1, -1};
        underTest.findSimilarityCentre(segmentPixels, centre);
        Assert.assertEquals(x, centre[xIndex]);
        Assert.assertEquals(y, centre[yIndex]);
    }

    @Test
    public void shouldShiftHorizontallyToSingleColourColumn() {
        final int x = random.nextInt(w);
        final int[][] segmentPixels = new int[h][w];
        for (int j=0 ; j<h ; j++) {
            segmentPixels[j][x] = singleColour;
        }

        final int[] centre = new int[]{-1, -1};
        underTest.findSimilarityCentre(segmentPixels, centre);
        Assert.assertEquals(x, centre[xIndex]);
        Assert.assertEquals(h/2, centre[yIndex]);
    }

    @Test
    public void shouldShiftVerticallyToSingleColourRow() {
        final int y = random.nextInt(h);
        final int[][] segmentPixels = new int[h][w];
        Arrays.fill(segmentPixels[y], singleColour);

        final int[] centre = new int[]{-1, -1};
        underTest.findSimilarityCentre(segmentPixels, centre);
        Assert.assertEquals(w/2, centre[xIndex]);
        Assert.assertEquals(y, centre[yIndex]);
    }
}
