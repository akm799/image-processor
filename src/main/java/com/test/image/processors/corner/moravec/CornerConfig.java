package com.test.image.processors.corner.moravec;

public final class CornerConfig {
    /**
     * Radius used for the Gaussian blur. Minimum is 1.
     */
    public final int blurRadius = 2;

    /**
     * Sigma value used for the Gaussian blur. Must be greater than 0.
     */
    public final float blurSigma = 1.4f;

    /**
     * Moravec window radius. The resulting window will be NxN pixels where N = 2*windowRadius + 1
     */
    public final int windowRadius = 5;

    /**
     * The amount of pixels which the Moravec window will be shifted by (to compute the square differences sum with
     * respect to the un-shifted window).
     */
    public final int windowShift = 5;



    /**
     * Set to true if the image is to be blurred or to false otherwise.
     * Please note that if this parameter is false then the #blurRadius and #blurSigma parameters are ignored.
     */
    public final boolean blurImage = true;

    CornerConfig() {}
}
