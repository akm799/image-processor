package com.test.image.processors.corner.moravec;

import com.test.image.ImageDataProcessor;
import com.test.image.model.Constants;
import com.test.image.model.GrayScaleImage;

final class LowValueSuppressor implements ImageDataProcessor {
    private final int threshold;

    LowValueSuppressor(int threshold) {
        this.threshold = threshold;
    }

    @Override
    public GrayScaleImage processImage(GrayScaleImage image) {
        final GrayScaleImage output = new GrayScaleImage(image.getWidth(), image.getHeight());
        for (int j=0 ; j<image.getHeight() ; j++) {
            for (int i=0 ; i<image.getWidth() ; i++) {
                final int value = image.getPixel(i, j);
                if (value < threshold) {
                    output.setPixel(i, j, 0);
                } else {
                    output.setPixel(i, j, value);
                }
            }
        }

        return output;
    }
}
