package com.test.image.model;

import org.junit.Assert;

public class GrayScaleImageTestHelper {

    public static void assertPixels(int[][] expected, GrayScaleImage actual) {
        checkExpected(expected);

        Assert.assertNotNull(actual);
        Assert.assertEquals(expected.length, actual.getHeight());
        Assert.assertEquals(expected[0].length, actual.getWidth());
        for (int j=0 ; j<actual.getHeight() ; j++) {
            for (int i=0 ; i<actual.getWidth() ; i++) {
                Assert.assertEquals(expected[j][i], actual.getPixel(i, j));
            }
        }
    }

    private static void checkExpected(int[][] expected) {
        if (expected == null) {
            throw new IllegalArgumentException("Expected pixel array cannot be null.");
        }

        if (expected.length == 0) {
            throw new IllegalArgumentException("Expected pixel array cannot be empty.");
        }

        int width = 0;
        for (int[] row : expected) {
            if (row == null) {
                throw new IllegalArgumentException("Error: found null row in input pixel array.");
            }

            if (row.length == 0) {
                throw new IllegalArgumentException("Error: found empty row in input pixel array.");
            }

            if (width == 0) {
                width = row.length;
            } else if (row.length != width) {
                throw new IllegalArgumentException("Error: found row with length " + row.length + " in input pixel array but the previous row had a length of " + width);
            }
        }
    }

    private GrayScaleImageTestHelper() {}
}
