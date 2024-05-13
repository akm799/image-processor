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


    private final int[][] imagePixels = new int[50][50];
    private final Rectangle targetRegion = new Rectangle(1, 2, 9, 9);
    private final int xTargetRegionCentre = targetRegion.x + targetRegion.width/2;
    private final int yTargetRegionCentre = targetRegion.y + targetRegion.height/2;

    @Test
    public void shouldShift() {
        final int[][] centres = {{8, 9}, {9, 10}};
        final Rectangle expected = expectedShiftResult(centres);
        final ColourHistogram similarity = mockColourHistogram(centres);

        final MeanShift underTest = MeanShiftFactory.instance(1, 10);
        final Rectangle actual = underTest.shift(similarity, imagePixels, targetRegion);
        Assert.assertEquals(expected, actual);
    }

    private ColourHistogram mockColourHistogram(int[][] centres) {
        final MockColourHistogram histogram = new MockColourHistogram(imagePixels, targetRegion);
        histogram.setCentres(centres);

        return histogram;
    }

    private Rectangle expectedShiftResult(int[][] centres) {
        final int dx = centres[centres.length-1][X_INDEX] - xTargetRegionCentre;
        final int dy = centres[centres.length-1][Y_INDEX] - yTargetRegionCentre;
        final int x = targetRegion.x + dx;
        final int y = targetRegion.y + dy;

        return new Rectangle(x, y, targetRegion.width, targetRegion.height);
    }
}
