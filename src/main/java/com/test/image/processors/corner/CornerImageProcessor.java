package com.test.image.processors.corner;

import com.test.image.AbstractFileImageProcessor;
import com.test.image.ImageDataProcessor;
import com.test.image.model.GrayScaleImage;
import com.test.image.processors.corner.moravec.CornerImageDataProcessor;
import com.test.image.util.GrayScaleImageHelper;

import java.awt.image.BufferedImage;

public final class CornerImageProcessor extends AbstractFileImageProcessor {

    @Override
    public String getDescription() {
        return "Under construction";
    }

    @Override
    public BufferedImage processImage(BufferedImage image) {
        final GrayScaleImage input = new GrayScaleImage(image);
        final ImageDataProcessor imageDataProcessor = new CornerImageDataProcessor();
        final GrayScaleImage output = imageDataProcessor.processImage(input);

        return GrayScaleImageHelper.toBufferedImage(output);
    }
}
