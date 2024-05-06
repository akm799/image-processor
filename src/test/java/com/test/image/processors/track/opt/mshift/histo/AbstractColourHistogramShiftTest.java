package com.test.image.processors.track.opt.mshift.histo;

import com.test.image.processors.track.opt.mshift.ColourHistogram;
import com.test.image.util.ColourHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.util.Random;

abstract class AbstractColourHistogramShiftTest {
    private final int xIndex = 0;
    private final int yIndex = 1;
    private final int binWidth = 5;
    private final Random random = new Random(System.currentTimeMillis());
    private final int singleColour = ColourHelper.getRgb(255, 0, 0);
    private final int w = 10;
    private final int h = 10;

    private ColourHistogram underTest;
    private Rectangle targetRegion;


    @Before
    public void setup() {
        underTest = instance(binWidth);

        targetRegion = buildTargetRegion();
        ColourHistogramTestHelper.fillColourHistogramWithSingleColour(underTest, w, h, singleColour, targetRegion);
    }

    private Rectangle buildTargetRegion() {
        final int side = 3 + random.nextInt(2);
        final int x = random.nextInt(w - side);
        final int y = random.nextInt(h - side);

        return new Rectangle(x, y, side, side);
    }

    abstract ColourHistogram instance(int binWidth);

    @Test
    public void shouldShiftToSingleColourSinglePixel() {
        final int x = targetRegion.x + random.nextInt(targetRegion.width);
        final int y = targetRegion.y + random.nextInt(targetRegion.height);
        final int[][] imagePixels = new int[h][w];
        imagePixels[y][x] = singleColour;

        final int[] centre = new int[]{-1, -1};
        underTest.findSimilarityCentre(imagePixels, targetRegion, centre);
        Assert.assertEquals(x, centre[xIndex]);
        Assert.assertEquals(y, centre[yIndex]);
    }

    @Test
    public void shouldShiftHorizontallyToSingleColourColumn() {
        final int yMax = targetRegion.y + targetRegion.height;
        final int x = targetRegion.x + random.nextInt(targetRegion.width);
        final int[][] imagePixels = new int[h][w];
        for (int j=targetRegion.y ; j<yMax ; j++) {
            imagePixels[j][x] = singleColour;
        }

        final int[] centre = new int[]{-1, -1};
        underTest.findSimilarityCentre(imagePixels, targetRegion, centre);
        Assert.assertEquals(x, centre[xIndex]);
        Assert.assertEquals(targetRegion.y + targetRegion.height/2, centre[yIndex]);
    }

    @Test
    public void shouldShiftVerticallyToSingleColourRow() {
        final int xMax = targetRegion.x + targetRegion.width;
        final int y = targetRegion.y + random.nextInt(targetRegion.height);
        final int[][] imagePixels = new int[h][w];
        for (int i=targetRegion.x ; i<xMax ; i++) {
            imagePixels[y][i] = singleColour;
        }

        final int[] centre = new int[]{-1, -1};
        underTest.findSimilarityCentre(imagePixels, targetRegion, centre);
        Assert.assertEquals(targetRegion.x + targetRegion.width/2, centre[xIndex]);
        Assert.assertEquals(y, centre[yIndex]);
    }

    @Test
    public void shouldNotShiftWhenNotRequired() {
        final Rectangle targetRegion = new Rectangle(2, 2, 3, 3);
        final int x = targetRegion.x + targetRegion.width/2; // x target region centre
        final int y = targetRegion.y + targetRegion.height/2; // y target region centre
        final int[][] imagePixels = new int[h][w];
        imagePixels[y][x] = singleColour;

        final int[] centre = new int[]{-1, -1};
        underTest.findSimilarityCentre(imagePixels, targetRegion, centre);
        Assert.assertEquals(x, centre[xIndex]);
        Assert.assertEquals(y, centre[yIndex]);
    }
}
