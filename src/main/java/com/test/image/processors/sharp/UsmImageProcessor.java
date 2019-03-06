package com.test.image.processors.sharp;

import com.test.image.AbstractFileImageProcessor;
import com.test.image.ImageDataProcessor;
import com.test.image.model.GrayScaleImage;
import com.test.image.util.GrayScaleImageHelper;

import java.awt.image.BufferedImage;

/**
 * Created by Thanos Mavroidis on 06/03/2019.
 */
public final class UsmImageProcessor extends AbstractFileImageProcessor {

    @Override
    public String getDescription() {
        return "UnSharp Masking";
    }

    @Override
    public BufferedImage processImage(BufferedImage image) {
        final GrayScaleImage input = new GrayScaleImage(image);
        final ImageDataProcessor imageDataProcessor = new UsmImageDataProcessor();
        final GrayScaleImage sharp = imageDataProcessor.processImage(input);

        return GrayScaleImageHelper.toBufferedImage(sharp);
    }
}
