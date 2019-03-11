package com.test.image.cluster;

/**
 * Created by Thanos Mavroidis on 11/03/2019.
 */
public final class ColourRangeData implements ColourRange {
    private final int rgb;
    private final double radius;

    public ColourRangeData(int rgb, double radius) {
        this.rgb = rgb;
        this.radius = radius;
    }

    @Override
    public int getRgb() {
        return rgb;
    }

    @Override
    public double getRadius() {
        return radius;
    }
}
