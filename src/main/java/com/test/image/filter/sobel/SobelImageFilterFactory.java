package com.test.image.filter.sobel;

import com.test.image.filter.ImageFilter;

public class SobelImageFilterFactory {

    public static ImageFilter getHorizontal3x3Instance() {
        final int radius = 1;
        final float[][] values = {
                {-1, 0, 1},
                {-2, 0, 2},
                {-1, 0, 1}
        };

        return new ImageFilter() {
            @Override
            public int getRadius() {
                return radius;
            }

            @Override
            public float getValue(int xOffset, int yOffset) {
                return values[yOffset + radius][xOffset + radius];
            }
        };
    }

    public static ImageFilter getVertical3x3Instance() {
        final int radius = 1;
        final float[][] values = { //TODO Find correct direction of Sobel vertical operator.
                {-1, -2, -1},
                { 0,  0,  0},
                { 1,  2,  1}
        };

        return new ImageFilter() {
            @Override
            public int getRadius() {
                return radius;
            }

            @Override
            public float getValue(int xOffset, int yOffset) {
                return values[yOffset + radius][xOffset + radius];
            }
        };
    }

    public static ImageFilter getHorizontal5x5Instance() {
        final int radius = 2;
        final float[][] values = {
                {-1,  -2, 0,  2, 1},
                {-4,  -8, 0,  8, 4},
                {-6, -12, 0, 12, 6},
                {-4,  -8, 0,  8, 4},
                {-1,  -2, 0,  2, 1}
        };

        return new ImageFilter() {
            @Override
            public int getRadius() {
                return radius;
            }

            @Override
            public float getValue(int xOffset, int yOffset) {
                return values[yOffset + radius][xOffset + radius];
            }
        };
    }

    public static ImageFilter getVertical5x5Instance() {
        final int radius = 2;
        final float[][] values = { //TODO Find correct direction of Sobel vertical operator.
                {-1, -4,  -6, -4, -1},
                {-2, -8, -12, -8, -2},
                { 0,  0,   0,  0,  0},
                { 2,  8,  12,  8,  2},
                { 1,  4,   6,  4,  1}
        };

        return new ImageFilter() {
            @Override
            public int getRadius() {
                return radius;
            }

            @Override
            public float getValue(int xOffset, int yOffset) {
                return values[yOffset + radius][xOffset + radius];
            }
        };
    }

    private SobelImageFilterFactory() {}
}
