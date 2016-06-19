package com.test.image.model;

import org.junit.Assert;
import org.junit.Test;

public class GradientsTest {
    private static final double P = Math.PI;
    private static final float M = (float)Math.sqrt(2);

    @Test
    public void shouldComputeGradients() {
        final int[][] horizontalPixels = {
                { 1,  1,  0},
                {-1, -1, -1},
                { 0,  1,  0}
        };

        final int[][] verticalPixels = {
                { 0,  1,  1},
                { 1,  0, -1},
                {-1, -1,  0}
        };

        final float[][] expectedMagnitudes = {
                {1, M, 1},
                {M, 1, M},
                {1, M, 0}
        };

        final double[][] expectedDirections = {
                {  0,    P/4,   P/2},
                {3*P/4,  P,  -3*P/4},
                { -P/2, -P/4,   P/2} // The last angle is undefined but we chose PI/2 since the DeltaX is zero.
        };

        final GrayScaleImage horizontal = GrayScaleImageFactory.instance(horizontalPixels);
        final GrayScaleImage vertical = GrayScaleImageFactory.instance(verticalPixels);
        final Gradients underTest = new Gradients(horizontal, vertical);
        for (int j=0 ; j<expectedMagnitudes.length ; j++) {
            for (int i=0 ; i<expectedMagnitudes[0].length ; i++) {
                Assert.assertEquals(expectedMagnitudes[j][i], underTest.magnitudes.getPixelMetaData(i, j), 0);
                Assert.assertEquals((float)expectedDirections[j][i], underTest.directions.getPixelMetaData(i, j), 0);
            }
        }
    }
}
