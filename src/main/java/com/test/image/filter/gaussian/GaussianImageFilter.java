package com.test.image.filter.gaussian;

import com.test.image.filter.ImageFilter;

public class GaussianImageFilter implements ImageFilter {
    private final int radius;
    private final float[][] values;

    public GaussianImageFilter(int radius, float sigma) {
        checkArguments(radius, sigma);
        this.radius = radius;
        this.values = init(radius, sigma);
    }

    private void checkArguments(int radius, float sigma) {
        if (radius <= 0) {
            throw new IllegalArgumentException("Illegal 'radius' argument: " + radius + " This argument must be: radius >= 1");
        }

        if (sigma <= 0) {
            throw new IllegalArgumentException("Illegal 'sigma' argument: " + radius + " This argument must be: sigma > 0");
        }
    }

    private float[][] init(int radius, float sigma) {
        final int side = 2*radius + 1;
        final float[][] values = new float[side][side];
        initValues(radius, sigma, values);

        return values;
    }

    private void initValues(int radius, float sigma, float[][] values) {
        final int side = values.length;
        final double twoSigmaSq = 2*sigma*sigma;

        float sumOfValues = 0;
        for (int j=0 ; j<side ; j++) {
            final int dy = radius - j;
            for (int i=0 ; i<side ; i++) {
                final int dx = radius - i;
                final double rSq = dx*dx + dy*dy;
                values[j][i] = (float)Math.exp(-rSq/twoSigmaSq);
                sumOfValues += values[j][i];
            }
        }

        // Normalize the values
        for (int j=0 ; j<side ; j++) {
            for (int i=0 ; i<side ; i++) {
                values[j][i] /= sumOfValues;
            }
        }
    }

    @Override
    public int getRadius() {
        return radius;
    }

    @Override
    public float getValue(int xOffset, int yOffset) {
        return values[yOffset + radius][xOffset + radius];
    }
}
