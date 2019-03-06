package com.test.image.processors.scale;

import com.test.image.ImageDataProcessor;
import com.test.image.model.Constants;
import com.test.image.model.GrayScaleImage;

/**
 * Created by Thanos Mavroidis on 06/03/2019.
 */
public final class IntensityScaleImageDataProcessor implements ImageDataProcessor {
    private final float amount;

    public IntensityScaleImageDataProcessor(float amount) {
        this.amount = amount;
    }

    @Override
    public GrayScaleImage processImage(GrayScaleImage image) {
        final GrayScaleImage output = new GrayScaleImage(image.getWidth(), image.getHeight());
        scale(image, amount, output);

        return output;
    }

    private void scale(GrayScaleImage input, float amount, GrayScaleImage output) {
        for (int j=0; j<input.getHeight() ; j++) {
            for (int i=0 ; i<input.getWidth() ; i++) {
                final int value = input.getPixel(i, j);
                output.setPixel(i, j, scaleValuesSafe(amount, value));
            }
        }
    }

    private int scaleValuesSafe(float a, int v) {
        final int value = Math.round(a * v);

        if (value < 0) {
            return 0;
        } else if (value > Constants.MAX_INTENSITY) {
            return Constants.MAX_INTENSITY;
        } else {
            return value;
        }
    }
}
