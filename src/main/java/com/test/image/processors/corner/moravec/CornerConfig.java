package com.test.image.processors.corner.moravec;

@Deprecated
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
    @Deprecated
    public final int windowRadius = 5;

    /**
     * The amount of pixels which the Moravec window will be shifted by (to compute the square differences sum with
     * respect to the un-shifted window).
     */
    @Deprecated
    public final int windowShift = 2*windowRadius + 1;

    /**
     * Ratio to evaluate the lowest score for the corner score threshold. This ratio is used in the following algorithm.
     *  1) Work out the frequency (count) of corner scores and order these frequencies in descending corner score.
     *  2) Go down the list (i.e. in descending corner score)
     *  3) Evaluate the frequency increase ratio, i.e. the current frequency devided by the previous frequency
     *  4) If the ratio is greater than this value then our threshold is the PREVIOUS corner score (i.e. one up in the list)
     *
     *  This assumes that we have a sudden increase of frequencies for edges since we expect a lot more edges than corners.
     */
    public final int suddenIncreaseRatio = 2;

    /**
     * Set to true if the image is to be blurred or to false otherwise.
     * Please note that if this parameter is false then the #blurRadius and #blurSigma parameters are ignored.
     * In addition if the #edgeProcess parameter is set to true this parameter will be assumed to be set to false
     * (no matter what value is defined here).
     */
    public final boolean blurImage = false;

    /**
     * Set to true if the image is to be edge processed before the corner score evaluation or false otherwise.
     * Please note that if this value is set to true then the #blurImage value is ignored, i.e. no separate
     * blurring (except as part of the edge processing) is applied.
     */
    public final boolean edgeProcess = true;

    CornerConfig() {}
}
