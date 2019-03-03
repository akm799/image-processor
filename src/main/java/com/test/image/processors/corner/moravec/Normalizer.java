package com.test.image.processors.corner.moravec;

import com.test.image.ImageDataProcessor;
import com.test.image.model.Constants;
import com.test.image.model.GrayScaleImage;

final class Normalizer implements ImageDataProcessor {
    private static final int MIN_INDEX = 0;
    private static final int MAX_INDEX = 1;
    private static final int NUMBER_OF_INDEXES = 2;

    Normalizer() {}

    @Override
    public GrayScaleImage processImage(GrayScaleImage image) {
        final GrayScaleImage normalized = new GrayScaleImage(image.getWidth(), image.getHeight());
        final int[] limits = findLimitsExcludingZeros(image);
        normalize(image, limits[MIN_INDEX], limits[MAX_INDEX], normalized);

        return normalized;
    }

    private int[] findLimitsExcludingZeros(GrayScaleImage image) {
        final int[] limits = new int[NUMBER_OF_INDEXES];
        limits[MIN_INDEX] = Integer.MAX_VALUE;
        limits[MAX_INDEX] = Integer.MIN_VALUE;

        for (int j=0 ; j<image.getHeight() ; j++) {
            for (int i=0 ; i<image.getWidth() ; i++) {
                final int value = image.getPixel(i, j);
                if (value > 0) { // Exclude zeros, i.e. pixels rejected by the threshold filter.
                    if (value < limits[MIN_INDEX]) {
                        limits[MIN_INDEX] = value;
                    }

                    if (value > limits[MAX_INDEX]) {
                        limits[MAX_INDEX] = value;
                    }
                }
            }
        }

        return limits;
    }
    
    private void normalize(GrayScaleImage input, float min, float max, GrayScaleImage output) {
        final float normalizationFactor = Constants.MAX_INTENSITY/(max - min);
        for (int j=0 ; j<input.getHeight() ; j++) {
            for (int i=0 ; i<input.getWidth() ; i++) {
                final float value = input.getPixel(i, j);
                if (value > 0) {
                    final int normalizedValue = Math.round((value - min) * normalizationFactor);
                    output.setPixel(i, j, normalizedValue);
                }
            }
        }
    }
}
