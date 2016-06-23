package com.test.image.processors.edge;

import com.test.image.model.*;
import org.junit.Test;


public class NonMaximumSuppressorTest {
    private static final float TEST_STEP = (float)(Math.PI/16);

    private final ImageGradientsDataProcessor underTest = new NonMaximaSuppressor();

    @Test
    public void shouldSuppressNonMaxInEastDirection() {
        final float east = 0;
        testEastOrWestDirectionMaxSuppression(east - TEST_STEP);
        testEastOrWestDirectionMaxSuppression(east);
        testEastOrWestDirectionMaxSuppression(east + TEST_STEP);
    }

    @Test
    public void shouldSuppressNonMaxInWestDirection() {
        final float westNorthWest = (float)Math.PI - TEST_STEP;
        testEastOrWestDirectionMaxSuppression(westNorthWest);

        final float westPositive = (float)Math.PI;
        testEastOrWestDirectionMaxSuppression(westPositive);

        final float westNegative = -(float)Math.PI;
        testEastOrWestDirectionMaxSuppression(westNegative);

        final float westSouthWest = TEST_STEP - (float)Math.PI;
        testEastOrWestDirectionMaxSuppression(westSouthWest);
    }

    private void testEastOrWestDirectionMaxSuppression(float a) {
        final float[][] magnitudes = {
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {2, 1, 3, 2, 3},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0}
        };

        final float[][] directions = {
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, a, a, a, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0}
        };

        final int[][] expected = {
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {2, 0, 3, 0, 3},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0}
        };

        testNonMaxSuppression(magnitudes, directions, expected);
    }

    @Test
    public void shouldSuppressNonMaxInNorthEastDirection() {
        final float northEast = (float)(Math.PI/4);
        testNorthEastOrSouthWestDirectionMaxSuppression(northEast - TEST_STEP);
        testNorthEastOrSouthWestDirectionMaxSuppression(northEast);
        testNorthEastOrSouthWestDirectionMaxSuppression(northEast + TEST_STEP);
    }

    @Test
    public void shouldSuppressNonMaxInSouthWestDirection() {
        final float southWest = (float)(Math.PI/4 - Math.PI);
        testNorthEastOrSouthWestDirectionMaxSuppression(southWest - TEST_STEP);
        testNorthEastOrSouthWestDirectionMaxSuppression(southWest);
        testNorthEastOrSouthWestDirectionMaxSuppression(southWest + TEST_STEP);
    }

    private void testNorthEastOrSouthWestDirectionMaxSuppression(float a) {
        final float[][] magnitudes = {
                {0, 0, 0, 0, 3},
                {0, 0, 0, 2, 0},
                {0, 0, 3, 0, 0},
                {0, 1, 0, 0, 0},
                {2, 0, 0, 0, 0}
        };

        final float[][] directions = {
                {0, 0, 0, 0, 0},
                {0, 0, 0, a, 0},
                {0, 0, a, 0, 0},
                {0, a, 0, 0, 0},
                {0, 0, 0, 0, 0}
        };

        final int[][] expected = {
                {0, 0, 0, 0, 3},
                {0, 0, 0, 0, 0},
                {0, 0, 3, 0, 0},
                {0, 0, 0, 0, 0},
                {2, 0, 0, 0, 0}
        };

        testNonMaxSuppression(magnitudes, directions, expected);
    }

    @Test
    public void shouldSuppressNonMaxInNorthDirection() {
        final float north = (float)(Math.PI/2);
        testNorthOrSouthDirectionMaxSuppression(north - TEST_STEP);
        testNorthOrSouthDirectionMaxSuppression(north);
        testNorthOrSouthDirectionMaxSuppression(north + TEST_STEP);
    }

    @Test
    public void shouldSuppressNonMaxInSouthDirection() {
        final float south = (float)(-Math.PI/2);
        testNorthOrSouthDirectionMaxSuppression(south - TEST_STEP);
        testNorthOrSouthDirectionMaxSuppression(south);
        testNorthOrSouthDirectionMaxSuppression(south + TEST_STEP);
    }

    private void testNorthOrSouthDirectionMaxSuppression(float a) {
        final float[][] magnitudes = {
                {0, 0, 3, 0, 0},
                {0, 0, 2, 0, 0},
                {0, 0, 3, 0, 0},
                {0, 0, 1, 0, 0},
                {0, 0, 2, 0, 0}
        };

        final float[][] directions = {
                {0, 0, 0, 0, 0},
                {0, 0, a, 0, 0},
                {0, 0, a, 0, 0},
                {0, 0, a, 0, 0},
                {0, 0, 0, 0, 0}
        };

        final int[][] expected = {
                {0, 0, 3, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 3, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 2, 0, 0}
        };

        testNonMaxSuppression(magnitudes, directions, expected);
    }

    @Test
    public void shouldSuppressNonMaxInNorthWestDirection() {
        final float northWest = (float)(3*Math.PI/4);
        testNorthWestOrSouthEastDirectionMaxSuppression(northWest - TEST_STEP);
        testNorthWestOrSouthEastDirectionMaxSuppression(northWest);
        testNorthWestOrSouthEastDirectionMaxSuppression(northWest + TEST_STEP);
    }

    @Test
    public void shouldSuppressNonMaxInSouthEastDirection() {
        final float southEast = (float)(-Math.PI/4);
        testNorthWestOrSouthEastDirectionMaxSuppression(southEast - TEST_STEP);
        testNorthWestOrSouthEastDirectionMaxSuppression(southEast);
        testNorthWestOrSouthEastDirectionMaxSuppression(southEast + TEST_STEP);
    }

    private void testNorthWestOrSouthEastDirectionMaxSuppression(float a) {
        final float[][] magnitudes = {
                {3, 0, 0, 0, 0},
                {0, 2, 0, 0, 0},
                {0, 0, 3, 0, 0},
                {0, 0, 0, 1, 0},
                {0, 0, 0, 0, 2}
        };

        final float[][] directions = {
                {0, 0, 0, 0, 0},
                {0, a, 0, 0, 0},
                {0, 0, a, 0, 0},
                {0, 0, 0, a, 0},
                {0, 0, 0, 0, 0}
        };

        final int[][] expected = {
                {3, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 3, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 2}
        };

        testNonMaxSuppression(magnitudes, directions, expected);
    }

    private void testNonMaxSuppression(float[][] magnitudes, float[][] directions, int[][] expected) {
        final Gradients gradients = GradientsFactory.instance(ImageMetaDataFactory.instance(magnitudes), ImageMetaDataFactory.instance(directions));

        final GrayScaleImage nonMaxSuppressed = underTest.processGradients(gradients);
        GrayScaleImageTestHelper.assertPixels(expected, nonMaxSuppressed);
    }
}
