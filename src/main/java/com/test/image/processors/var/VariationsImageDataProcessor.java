package com.test.image.processors.var;

import com.test.image.ImageDataProcessor;
import com.test.image.model.Direction;
import com.test.image.model.GrayScaleImage;
import com.test.image.util.edge.EdgeHandler;
import com.test.image.util.edge.EdgeHandlerFactory;

/**
 * Created by Thanos Mavroidis on 09/03/2019.
 */
final class VariationsImageDataProcessor implements ImageDataProcessor {
    private final EdgeHandler edgeHandler = EdgeHandlerFactory.extendInstance();

    VariationsImageDataProcessor() {}

    @Override
    public GrayScaleImage processImage(GrayScaleImage image) {
        final GrayScaleImage output = new GrayScaleImage(image.getWidth(), image.getHeight());
        variationsImage(image, output);

        return output;
    }

    private void variationsImage(GrayScaleImage input, GrayScaleImage output) {
        for (int j=0 ; j<input.getHeight() ; j++) {
            for (int i=0 ; i<input.getWidth() ; i++) {
                output.setPixel(i, j, averageDiffForPixel(input, i, j));
            }
        }
    }

    private int averageDiffForPixel(GrayScaleImage image, int i, int j) {
        final int value = image.getPixel(i, j);

        int sum = 0;
        for (Direction d : Direction.values()) {
            final int neighbourValue = edgeHandler.getPixelValueSafe(image, i + d.xOffset, j + d.yOffset);
            sum += Math.abs(neighbourValue - value);
        }

        return Math.round((float)sum/Direction.values().length);
    }
}
