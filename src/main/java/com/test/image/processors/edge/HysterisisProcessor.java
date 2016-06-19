package com.test.image.processors.edge;

import com.test.image.ImageDataProcessor;
import com.test.image.model.Constants;
import com.test.image.model.Direction;
import com.test.image.model.GrayScaleImage;

public final class HysterisisProcessor implements ImageDataProcessor {

    @Override
    public GrayScaleImage processImage(GrayScaleImage image) {
        final GrayScaleImage output = new GrayScaleImage(image.getWidth(), image.getHeight());
        for (int j=0; j<image.getHeight() ; j++) {
            for (int i=0 ; i<image.getWidth() ; i++) {
                if (isStrongPixel(image, i, j)) {
                    output.setPixel(i, j, Constants.MAX_INTENSITY);
                } else if (isWeakPixel(image, i, j)) {
                    if (isConnectedToStrongPixel(image, i, j)) {
                        output.setPixel(i, j, Constants.MAX_INTENSITY);
                    } else {
                        output.setPixel(i, j, 0);
                    }
                }
            }
        }

        return output;
    }

    private boolean isConnectedToStrongPixel(GrayScaleImage image, int x, int y) {
        for (Direction direction : Direction.values()) {
            final int xNeighbour = x + direction.xOffset;
            final int yNeighbour = y + direction.yOffset;
            if (image.isInRange(xNeighbour, yNeighbour) && isStrongPixel(image, xNeighbour, yNeighbour)) {
                return true;
            }
        }

        return false;
    }

    private boolean isStrongPixel(GrayScaleImage image, int x, int y) {
        return pixelHasIntensity(image, x, y, Constants.MAX_INTENSITY);
    }

    private boolean isWeakPixel(GrayScaleImage image, int x, int y) {
        return pixelHasIntensity(image, x, y, Constants.WEAK_PIXEL_INTENSITY);
    }

    private boolean pixelHasIntensity(GrayScaleImage image, int x, int y, int intensity) {
        return (image.getPixel(x, y) == intensity);
    }
}
