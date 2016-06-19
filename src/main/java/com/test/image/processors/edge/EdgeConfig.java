package com.test.image.processors.edge;

/**
 * Configuration parameters for Canny edge detection.
 *
 * https://en.wikipedia.org/wiki/Canny_edge_detector
 */
public final class EdgeConfig {
    /**
     * Radius used for the Gaussian blur. Minimum is 1.
     */
    public final int blurRadius = 2;

    /**
     * Sigma value used for the Gaussian blur. Must be greater than 0.
     */
    public final float blurSigma = 1.4f;


    /**
     * Fraction used to determine the low threshold in the double-threshold analysis.
     * The high threshold is determined independently. Them the low threshold will be
     * the high one multiplied by this value. Therefore, this value must be less than 1
     * and greater or equal to 0.
     */
    public final float lowThresholdReductionFactor = 0.5f;



    /**
     * Set to true if the image is to be blurred or to false otherwise.
     * Please note that if this parameter is false then the #blurRadius and #blurSigma parameters are ignored.
     */
    public final boolean blurImage = true;

    /**
     * Set to true if non-maximum suppression is to be performed or to false otherwise.
     */
    public final boolean performNonMaximumSuppression = true;

    /**
     * Set to true if double-threshold analysis is to be performed or to false otherwise.
     */
    public final boolean performDoubleThresholdAnalysis = true;

    /**
     * Set to true if hysterisis blob analysis is to be performed or false otherwise.
     * Please note that if the #performDoubleThresholdAnalysis parameter is set to false, then the value of
     * this parameter is ignored.
     */
    public final boolean performHysterisisBlobAnalysis = true;

    EdgeConfig() {}
}
