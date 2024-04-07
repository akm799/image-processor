package com.test.image.model;

import org.junit.Assert;
import org.junit.Test;

public class KernelTest {

    @Test
    public void shouldBuildKernel() {
        final String s = "1 2 3 \n4 5 6 \n7 8 9 \n";
        final int[][] k = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};

        final Kernel underTest = new Kernel(k);
        Assert.assertEquals(s, underTest.toString());
    }

    @Test
    public void shouldApplyKernel() {
        final Kernel underTest = pickAboveLeftPixelKernel();

        final int n = 10;
        final int[][] imageData = increasingPixelValuesInSquare(n);

        testKernel(imageData, 5, 5, underTest, 44);
        testKernel(imageData, 6, 5, underTest, 45);

        testKernel(imageData, 6, 6, underTest, 55);
        testKernel(imageData, 5, 6, underTest, 54);

        testKernel(imageData, 1, 1, underTest, 0);
        testKernel(imageData, 2, 1, underTest, 1);
    }

    private Kernel pickAboveLeftPixelKernel() {
        final int[][] k = {{1, 0, 0}, {0, 0, 0}, {0, 0, 0}};

        return new Kernel(k);
    }

    private int[][] increasingPixelValuesInSquare(int n) {
        int v = 0;
        final int[][] imageData = new int[n][n]; // square image.
        for (int j=0 ; j<n ; j++) {
            for (int i=0 ; i<n; i++) {
                imageData[j][i] = v++; // increasing pixel values.
            }
        }

        return imageData;
    }

    private void testKernel(int[][] imageData, int x, int y, Kernel underTest, int expected) {
        final int w = imageData[0].length;
        final int h = imageData.length;
        final GrayScaleImage image = toImage(imageData);
        final int actual = underTest.apply(image, w, h, x, y);
        Assert.assertEquals(expected, actual);
    }

    private GrayScaleImage toImage(int[][] imageData) {
        final int w = imageData[0].length;
        final int h = imageData.length;

        final ImageMetaData data = new ImageMetaData(w, h);
        for (int j=0 ; j<h; j++) {
            for (int i=0 ; i<w ; i++) {
                data.setPixelMetaData(i, j, imageData[j][i]);
            }
        }

        return new GrayScaleImage(data);
    }
}
