package com.test.image.model;

public final class Gradients {
    private static final double PI_OVER_TWO = Math.PI/2;

    public final int width;
    public final int height;

    public final GrayScaleImage horizontal;
    public final GrayScaleImage vertical;

    public final ImageMetaData magnitudes;
    public final ImageMetaData directions;

    public Gradients(GrayScaleImage horizontal, GrayScaleImage vertical) {
        checkArguments(horizontal, vertical);

        this.width = horizontal.getWidth();
        this.height = horizontal.getHeight();

        this.horizontal = horizontal;
        this.vertical = vertical;

        this.magnitudes = new ImageMetaData(width, height);
        this.directions = new ImageMetaData(width, height);
        fillMagnitudesAndDirections();
    }

    private void checkArguments(GrayScaleImage horizontal, GrayScaleImage vertical) {
        if (horizontal == null || vertical == null) {
            throw new NullPointerException("The 'horizontal or 'vertical' argument is null. No null arguments area allowed.");
        }

        if (horizontal.getWidth() != vertical.getWidth()) {
            throw new IllegalArgumentException("The 'horizontal or 'vertical' images do not have the same width.");
        }

        if (horizontal.getHeight() != vertical.getHeight()) {
            throw new IllegalArgumentException("The 'horizontal or 'vertical' images do not have the same height.");
        }
    }

    private void fillMagnitudesAndDirections() {
        for (int j=0 ; j<height ; j++) {
            for (int i=0 ; i<width ; i++) {
                final int gx = horizontal.getPixel(i, j);
                final int gy = vertical.getPixel(i, j);

                final double g = Math.sqrt(gx*gx + gy*gy);
                magnitudes.setPixelMetaData(i, j, (float)g);

                final double angle = findGradientVectorAngle(gx, gy);
                directions.setPixelMetaData(i, j, (float)angle);
            }
        }
    }

    private double findGradientVectorAngle(int gx, int gy) {
        if (gx == 0) {
            return (gy >= 0 ? PI_OVER_TWO : -PI_OVER_TWO);
        } else {
            return Math.atan2(gy, gx);
        }
    }
}
