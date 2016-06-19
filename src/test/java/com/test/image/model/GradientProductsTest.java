package com.test.image.model;

import org.junit.Assert;
import org.junit.Test;

public class GradientProductsTest {

    @Test
    public void shouldComputeGradientProducts() {
        final int[][] horizontalPixels = {
                {1,   3,  5},
                {7,   9, 11},
                {13, 15, 17}
        };

        final int[][] verticalPixels = {
                {2,   4,  6},
                {8,  10, 12},
                {14, 16, 18}
        };


        // Expected gradient products values.

        final int[][] horizontalSquared = {
                {  1,   9,  25},
                { 49,  81, 121},
                {169, 225, 289}
        };

        final int[][] verticalSquared = {
                {  4,  16,  36},
                { 64, 100, 144},
                {196, 256, 324}
        };

        final int[][] mixedProducts = {
                {  2,  12,  30},
                { 56,  90, 132},
                {182, 240, 306}
        };

        final GrayScaleImage horizontal = GrayScaleImageFactory.instance(horizontalPixels);
        final GrayScaleImage vertical = GrayScaleImageFactory.instance(verticalPixels);
        final Gradients gradients = new Gradients(horizontal, vertical);

        final GradientProducts underTest = new GradientProducts(gradients);
        Assert.assertEquals(horizontal.getWidth(), underTest.width);
        Assert.assertEquals(vertical.getHeight(), underTest.height);
        for (int j=0 ; j<underTest.height ; j++) {
            for (int i=0 ; i<underTest.width ; i++) {
                Assert.assertEquals(horizontalSquared[j][i], underTest.horizontalSquared.getPixel(i, j));
                Assert.assertEquals(verticalSquared[j][i], underTest.verticalSquared.getPixel(i, j));
                Assert.assertEquals(mixedProducts[j][i], underTest.mixedProduct.getPixel(i, j));
            }
        }
    }
}
