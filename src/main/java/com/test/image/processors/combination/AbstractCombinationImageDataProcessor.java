package com.test.image.processors.combination;

import com.test.image.CombinationImageDataProcessor;
import com.test.image.model.Constants;
import com.test.image.model.GrayScaleImage;

/**
 * Created by Thanos Mavroidis on 06/03/2019.
 */
public abstract class AbstractCombinationImageDataProcessor implements CombinationImageDataProcessor {

    @Override
    public final GrayScaleImage combineImages(GrayScaleImage image1, GrayScaleImage image2) {
        ensureImageSizes(image1, image2);

        final GrayScaleImage output = new GrayScaleImage(image1.getWidth(), image2.getHeight());
        combineImages(image1, image2, output);

        return output;
    }

    private void ensureImageSizes(GrayScaleImage image1, GrayScaleImage image2) {
        if (image1.getWidth() != image2.getWidth() || image1.getHeight() != image1.getHeight()) {
            throw new IllegalArgumentException("Incompatible dimensions of the first input image (" + image1.getWidth() + ", " + image1.getHeight() + ") and the second input image (" + image2.getWidth() + ", " + image2.getHeight() + ").");
        }
    }

    private void combineImages(GrayScaleImage image1, GrayScaleImage image2, GrayScaleImage output) {
        for (int j=0; j<image1.getHeight() ; j++) {
            for (int i=0 ; i<image1.getWidth() ; i++) {
                final int value1 = image1.getPixel(i, j);
                final int value2 = image2.getPixel(i, j);
                output.setPixel(i, j, combineValuesSafe(value1, value2));
            }
        }
    }

    private int combineValuesSafe(int v1, int v2) {
        final int value = combineValues(v1, v2);

        if (value < 0) {
            return 0;
        } else if (value > Constants.MAX_INTENSITY) {
            return Constants.MAX_INTENSITY;
        } else {
            return value;
        }
    }

    protected abstract int combineValues(int v1, int v2);
}
