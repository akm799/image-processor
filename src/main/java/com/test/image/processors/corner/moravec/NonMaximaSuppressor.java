package com.test.image.processors.corner.moravec;

import com.test.image.ImageDataProcessor;
import com.test.image.model.Direction;
import com.test.image.model.GrayScaleImage;

final class NonMaximaSuppressor implements ImageDataProcessor {

    NonMaximaSuppressor() {}

    @Override
    public GrayScaleImage processImage(GrayScaleImage image) {
        final GrayScaleImage output = new GrayScaleImage(image.getWidth(), image.getHeight());
        suppressNonMaxima(image, output);

        return output;
    }

    private void suppressNonMaxima(GrayScaleImage input, GrayScaleImage output) {
        final int width = output.getWidth();
        final int height = output.getHeight();
        for (int j=0 ; j<height ; j++) {
            for (int i=0 ; i<width ; i++) {
                final int value = input.getPixel(i, j);
                if (hasHigherValueNeighbour(input, i, j, value)) {
                    output.setPixel(i, j, 0);
                } else {
                    output.setPixel(i, j, value);
                }
            }
        }
    }

    private boolean hasHigherValueNeighbour(GrayScaleImage input, int x, int y, int value) {
        for (Direction direction : Direction.values()) {
            final int xn = x + direction.xOffset;
            final int yn = y + direction.yOffset;
            if (input.isInRange(xn, yn) && input.getPixel(xn, yn) > value) {
                return true;
            }
        }

        return false;
    }
}
