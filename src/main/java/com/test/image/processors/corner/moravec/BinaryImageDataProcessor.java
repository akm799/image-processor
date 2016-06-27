package com.test.image.processors.corner.moravec;

import com.test.image.ImageDataProcessor;
import com.test.image.model.Constants;
import com.test.image.model.GrayScaleImage;

public final class BinaryImageDataProcessor implements ImageDataProcessor {
    private final int threshold;

    public BinaryImageDataProcessor() {
        this(0);
    }

    public BinaryImageDataProcessor(int threshold) {
        this.threshold = threshold;
    }

    @Override
    public GrayScaleImage processImage(GrayScaleImage image) {
        final GrayScaleImage output = new GrayScaleImage(image.getWidth(), image.getHeight());
        for (int j=0 ; j<image.getHeight() ; j++) {
            for (int i=0 ; i<image.getWidth() ; i++) {
                if (image.getPixel(i, j) > threshold) {
                    output.setPixel(i, j, Constants.MAX_INTENSITY);
                }
            }
        }

        return output;
    }
}
