package com.test.image.cluster;

/**
 * Spherical colour range in RGB space centred around some RGB value
 * and with some radius in RGB space.
 *
 * Created by Thanos Mavroidis on 11/03/2019.
 */
public interface ColourRange {

    /**
     * Returns the central colour of this colour range.
     *
     * @return the central colour of this colour range
     */
    int getRgb();

    /**
     * Returns the radius of this colour range in RGB space.
     *
     * @return the radius of this colour range in RGB space
     */
    double getRadius();
}
