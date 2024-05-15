package com.test.image.processors.track.opt.mshift.shift;

import com.test.image.processors.track.opt.mshift.ColourHistogram;
import com.test.image.processors.track.opt.mshift.MeanShift;
import com.test.image.processors.track.opt.mshift.impl.shift.MeanShiftFactory;
import org.junit.Assert;
import org.junit.Test;

import java.awt.*;

public class MeanShiftTest {
    private final static int X_INDEX = 0;
    private final static int Y_INDEX = 1;


    private final int width = 50;
    private final int height = 50;
    private final int[][] imagePixels = new int[height][width];
    private final Rectangle targetRegion = new Rectangle(1, 2, 9, 9);
    private final int xTargetRegionCentre = targetRegion.x + targetRegion.width/2;
    private final int yTargetRegionCentre = targetRegion.y + targetRegion.height/2;

    @Test
    public void shouldShift() {
        final int[][] centres = {{8, 9}, {9, 10}};
        final Rectangle expected = expectedShiftResultFromLastCentre(centres);
        final ColourHistogram similarity = mockColourHistogram(centres);

        final MeanShift underTest = MeanShiftFactory.instance(1, 10);
        final Rectangle actual = underTest.shift(similarity, imagePixels, targetRegion);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void shouldShiftHorizontally() {
        final int[][] centres = {{8, yTargetRegionCentre}, {9, yTargetRegionCentre}};
        final Rectangle expected = expectedShiftResultFromLastCentre(centres);
        final ColourHistogram similarity = mockColourHistogram(centres);

        final MeanShift underTest = MeanShiftFactory.instance(1, 10);
        final Rectangle actual = underTest.shift(similarity, imagePixels, targetRegion);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void shouldShiftVertically() {
        final int[][] centres = {{xTargetRegionCentre, 9}, {xTargetRegionCentre, 10}};
        final Rectangle expected = expectedShiftResultFromLastCentre(centres);
        final ColourHistogram similarity = mockColourHistogram(centres);

        final MeanShift underTest = MeanShiftFactory.instance(1, 10);
        final Rectangle actual = underTest.shift(similarity, imagePixels, targetRegion);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void shouldStopShiftingWhenMaxTriesExceeded() {
        final int maxTries = 5;
        final int[][] centres = {{8, 9}, {10, 11}, {12, 13}, {14, 15}, {16, 17}, {18, 19}, {20, 21}};
        Assert.assertTrue(centres.length > maxTries);
        final Rectangle expected = expectedShiftResult(centres[maxTries-1]);
        final ColourHistogram similarity = mockColourHistogram(centres);

        final MeanShift underTest = MeanShiftFactory.instance(1, maxTries);
        final Rectangle actual = underTest.shift(similarity, imagePixels, targetRegion);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void shouldStopShiftingWhenOverImageBorder() {
        final int xStep = 5;
        final int[][] centres = new int[100][];
        Assert.assertTrue(centres.length * xStep > width);

        final int lastIndex = populateOverTheBorderCentres(8, xStep, centres);
        Assert.assertTrue(lastIndex > 0);
        Assert.assertTrue(lastIndex < centres.length - 10);

        final Rectangle expected = expectedShiftResult(centres[lastIndex]);
        final ColourHistogram similarity = mockColourHistogram(centres);

        final MeanShift underTest = MeanShiftFactory.instance(1, 1000);
        final Rectangle actual = underTest.shift(similarity, imagePixels, targetRegion);
        Assert.assertEquals(expected, actual);
        Assert.assertTrue(actual.x + actual.width <= width);
        Assert.assertTrue(actual.x + actual.width + xStep > width);
    }

    private int populateOverTheBorderCentres(int x0, int xStep, int[][] centres) {
        int lastIndex = -1;

        int x = x0;
        for (int i=0 ; i<centres.length ; i++) {
            centres[i] = new int[]{x, 15};
            if (lastIndex < 0 && x + targetRegion.width > width) {
                lastIndex = i;
            }

            x += xStep;
        }

        return lastIndex;
    }

    private ColourHistogram mockColourHistogram(int[][] centres) {
        final MockColourHistogram histogram = new MockColourHistogram(imagePixels, targetRegion);
        histogram.setCentres(centres);

        return histogram;
    }

    private Rectangle expectedShiftResultFromLastCentre(int[][] centres) {
        return expectedShiftResult(centres[centres.length-1]);
    }

    private Rectangle expectedShiftResult(int[] centre) {
        final int dx = centre[X_INDEX] - xTargetRegionCentre;
        final int dy = centre[Y_INDEX] - yTargetRegionCentre;
        final int x = targetRegion.x + dx;
        final int y = targetRegion.y + dy;

        return new Rectangle(x, y, targetRegion.width, targetRegion.height);
    }
}
