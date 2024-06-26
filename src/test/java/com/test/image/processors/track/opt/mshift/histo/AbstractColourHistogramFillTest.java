package com.test.image.processors.track.opt.mshift.histo;

import com.test.image.processors.track.opt.mshift.ColourHistogram;
import com.test.image.test.ColourHistogramDataAssertion;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

abstract class AbstractColourHistogramFillTest {

    private final int binWidth = 5;
    private ColourHistogram underTest;

    final int redIndex = 0;
    final int greenIndex = 1;
    final int blueIndex = 2;
    private final int countIndex = 3;
    private final int xIndex = 0;
    private final int yIndex = 1;

    abstract ColourHistogram instance(int binWidth);

    abstract int getExampleRgb();

    abstract void getExampleRgbBin(int[] bin);

    @Before
    public void setUp() {
        underTest = instance(binWidth);
    }

    @Test
    public void shouldBuildEmptyHistogram() {
        final int n = 52;
        final ColourHistogramDataAssertion empty = data -> {
            assertDataSize(data, n);
            assertSingleValue(data, 0);
        };

        underTest.assertData(empty);
    }

    @Test
    public void shouldFillHistogramWithSingleNonBackgroundPixel() {
        final int[][] pixelCoordinates = new int[][]{{7, 7}};
        testHistogramFillingWithNonBackgroundPixel(pixelCoordinates);
    }

    @Test
    public void shouldFillHistogramWithTwoNonBackgroundPixel() {
        final int[][] pixelCoordinates = new int[][]{{7, 7}, {7, 8}};
        testHistogramFillingWithNonBackgroundPixel(pixelCoordinates);
    }

    private void testHistogramFillingWithNonBackgroundPixel(int[][] pixelCoordinates) {
        final int[] bin = new int[3];
        final int rgb = getExampleRgb();
        getExampleRgbBin(bin);

        final int[][] imagePixels = new int[10][10];
        final Rectangle targetRegion = new Rectangle(4, 4, 5, 5);
        final int nTargetRegionPixels = targetRegion.width * targetRegion.height;
        checkPixelCoordinatesInTargetRegion(pixelCoordinates, targetRegion);

        for (int[] coordinates : pixelCoordinates) {
            imagePixels[coordinates[xIndex]][coordinates[yIndex]] = rgb;
        }
        underTest.fill(imagePixels, targetRegion);

        final int n = 52;
        final int[] singleBin = new int[]{bin[redIndex], bin[greenIndex], bin[blueIndex], pixelCoordinates.length};
        final int[] backgroundPixelBins = new int[]{0, 0, 0, nTargetRegionPixels-pixelCoordinates.length};
        final int[][] values = new int[][]{singleBin, backgroundPixelBins};
        final ColourHistogramDataAssertion singleBinAssertion = data -> {
            assertDataSize(data, n);
            assertValues(data, values);
        };
        underTest.assertData(singleBinAssertion);
    }

    private void checkPixelCoordinatesInTargetRegion(int[][] pixelCoordinates, Rectangle targetRegion) {
        final int xMax = targetRegion.x + targetRegion.width;
        final int yMax = targetRegion.y + targetRegion.height;
        for (int[] coordinates : pixelCoordinates) {
            final int x = coordinates[xIndex];
            final int y = coordinates[yIndex];
            Assert.assertTrue("Internal error: pixel coordinate outside the target region x-range.", targetRegion.x <= x && x < xMax);
            Assert.assertTrue("Internal error: pixel coordinate outside the target region y-range.", targetRegion.y <= y && y < yMax);
        }
    }

    private void assertDataSize(int[][][] data, int n) {
        Assert.assertNotNull(data);
        Assert.assertEquals(n, data.length);
        Assert.assertEquals(n, data[0].length);
        Assert.assertEquals(n, data[0][0].length);
    }

    private void assertSingleValue(int[][][] data, int value) {
        final int n = data.length;
        for (int k=0 ; k<n ; k++) {
            for (int j=0 ; j<n ; j++) {
                for (int i=0 ; i<n ; i++) {
                    Assert.assertEquals(value, data[k][j][i]);
                }
            }
        }
    }

    private void assertValues(int[][][] data, int[][] values) {
        final int n = data.length;
        for (int k=0 ; k<n ; k++) {
            for (int j=0 ; j<n ; j++) {
                for (int i=0 ; i<n ; i++) {
                    final Integer expected = match(k, j, i, values);
                    if (expected != null) {
                        assertEquals(data, k, j, i, expected);
                    } else {
                        assertEquals(data, k, j, i, 0);
                    }
                }
            }
        }
    }

    private Integer match(int k, int j, int i, int[][] values) {
        for (int[] value : values) {
            if ((k == value[redIndex] && j == value[greenIndex] && i == value[blueIndex])) {
                return value[countIndex];
            }
        }

        return null;
    }

    private void assertEquals(int[][][] data, int k, int j, int i, int expected) {
        final int actual = data[k][j][i];
        Assert.assertEquals(msg(k, j, i, expected, actual), expected, actual);
    }

    private String msg(int k, int j, int i, int expected, int actual) {
        return "expected" + valueText(k, j, i, expected) + " actual" + valueText(k, j, i, actual);
    }

    private String valueText(int k, int j, int i, int value) {
        return String.format("[%d][%d][%d]=", k, j, i) + value;
    }
}
