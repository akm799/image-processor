package com.test.image.processors.chain;

import com.test.image.AbstractFileImageProcessor;
import com.test.image.ImageProcessor;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;

public abstract class ChainImageProcessor extends AbstractFileImageProcessor {
    private final Collection<ImageProcessor> processors = new ArrayList<>();

    public ChainImageProcessor(Collection<ImageProcessor> processors) {
        if (processors != null) {
            this.processors.addAll(processors);
        }
    }

    @Override
    public final BufferedImage processImage(BufferedImage baseImage) {
        BufferedImage image = baseImage;
        for (ImageProcessor processor : processors) {
            image = processor.processImage(image);
        }

        return image;
    }
}
