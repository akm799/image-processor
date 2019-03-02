package com.test.image.processors.corner.moravec;

import com.test.image.ImageDataProcessor;
import com.test.image.model.Constants;
import com.test.image.model.GrayScaleImage;

@Deprecated
public final class Normalizer implements ImageDataProcessor {

    @Override
    public GrayScaleImage processImage(GrayScaleImage image) {
        final GrayScaleImage normalized = new GrayScaleImage(image.getWidth(), image.getHeight());
        final float max = findMax(image);
        normalize(image, max, normalized);

        return normalized;
    }

    private int findMax(GrayScaleImage image) {
        int max = 0;
        for (int j=0 ; j<image.getHeight() ; j++) {
            for (int i=0 ; i<image.getWidth() ; i++) {
                final int value = image.getPixel(i, j);
                if (value > max) {
                    max = value;
                }
            }
        }

        return max;
    }

    private void normalize(GrayScaleImage input, float max, GrayScaleImage output) {
        final float normalizationFactor = Constants.MAX_INTENSITY/max;
        for (int j=0 ; j<input.getHeight() ; j++) {
            for (int i=0 ; i<input.getWidth() ; i++) {
                final int value = input.getPixel(i, j);
                if (value > 0) {
                    final int normalizedValue = Math.round(value*normalizationFactor);
                    output.setPixel(i, j, normalizedValue);
                }
            }
        }
    }
}
