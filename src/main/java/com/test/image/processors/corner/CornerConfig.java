package com.test.image.processors.corner;

/**
 * Configuration parameters for Harris edge detection. The image processed for corner detection
 * is the output of the (Canny) edge detector.
 */
public final class CornerConfig {
    /**
     * Value used in the Harris corner detection score formula: Det(H) - k*(Trace(H)**2)
     */
    public final float k = 0.04f;

    /**
     * Threshold for R value in Harris corner detection.
     */
    public final float rThreshold = 10000;

    CornerConfig() {}
}
