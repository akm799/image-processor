package com.test.image.processors.chain;

import com.test.image.ImageDataProcessor;
import com.test.image.model.GrayScaleImage;

import java.util.ArrayList;
import java.util.Collection;

public final class ChainImageDataProcessor implements ImageDataProcessor {
    private final Collection<ImageDataProcessor> processors = new ArrayList<>();

    public ChainImageDataProcessor(Collection<ImageDataProcessor> processors) {
        if (processors != null) {
            this.processors.addAll(processors);
        }
    }

    @Override
    public final GrayScaleImage processImage(GrayScaleImage baseImage) {
        GrayScaleImage image = baseImage;
        for (ImageDataProcessor processor : processors) {
            image = processor.processImage(image);
        }

        return image;
    }
}
