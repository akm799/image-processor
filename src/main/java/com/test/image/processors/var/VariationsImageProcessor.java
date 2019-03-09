package com.test.image.processors.var;

import com.test.image.AbstractFileImageProcessor;
import com.test.image.ImageDataProcessor;
import com.test.image.model.GrayScaleImage;
import com.test.image.processors.chain.ChainImageDataProcessor;
import com.test.image.processors.scale.Normalizer;
import com.test.image.util.GrayScaleImageHelper;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Thanos Mavroidis on 09/03/2019.
 */
public final class VariationsImageProcessor extends AbstractFileImageProcessor {

    @Override
    public String getDescription() {
        return "Neighbouring pixel variations image.";
    }

    @Override
    public BufferedImage processImage(BufferedImage image) {
        final GrayScaleImage input = new GrayScaleImage(image);
        final ImageDataProcessor imageDataProcessor = buildImageDataProcessor();
        final GrayScaleImage diff = imageDataProcessor.processImage(input);

        return GrayScaleImageHelper.toBufferedImage(diff);
    }

    private ImageDataProcessor buildImageDataProcessor() {
        final Collection<ImageDataProcessor> processors = new ArrayList<>(2);
        processors.add(new VariationsImageDataProcessor());
        processors.add(new Normalizer());

        return new ChainImageDataProcessor(processors);
    }
}
