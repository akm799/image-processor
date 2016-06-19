package com.test.image.processors.edge;

import com.test.image.ImageDataProcessor;
import com.test.image.model.Constants;
import com.test.image.model.GrayScaleImage;

public final class DoubleThresholdProcessor implements ImageDataProcessor {
    private final float lowThresholdReductionFactor;

    DoubleThresholdProcessor(float lowThresholdReductionFactor) {
        if (lowThresholdReductionFactor < 0 || lowThresholdReductionFactor >= 1) {
            throw new IllegalArgumentException("Illegal 'lowThresholdReductionFactor' argument: " + lowThresholdReductionFactor + " This argument must be: 0 <= lowThresholdReductionFactor < 1");
        }

        this.lowThresholdReductionFactor = lowThresholdReductionFactor;
    }

    @Override
    public GrayScaleImage processImage(GrayScaleImage image) {
        final ThresholdFinder thresholdFinder = new OtsuThresholdFinder();
        final float highThreshold = thresholdFinder.findThreshold(image);
        final float lowThreshold = highThreshold*lowThresholdReductionFactor;

        final GrayScaleImage output = new GrayScaleImage(image.getWidth(), image.getHeight());
        for (int j=0 ; j<image.getHeight() ; j++) {
            for (int i=0 ; i<image.getWidth() ; i++) {
                final int value = image.getPixel(i, j);
                if (value >= highThreshold) {
                    output.setPixel(i, j, Constants.MAX_INTENSITY);
                } else if (lowThreshold <= value && value < highThreshold) {
                    output.setPixel(i, j, Constants.WEAK_PIXEL_INTENSITY);
                } else {
                    output.setPixel(i, j, 0);
                }
            }
        }

        return output;
    }
}