package com.test.image.processors.doc;

import com.test.image.AbstractFileImageProcessor;
import com.test.image.ImageDataProcessor;
import com.test.image.ImageProcessor;
import com.test.image.model.GrayScaleImage;
import com.test.image.processors.chain.ChainImageDataProcessor;
import com.test.image.processors.sharp.UsmImageDataProcessor;
import com.test.image.processors.var.VariationsImageDataProcessor;
import com.test.image.util.GrayScaleImageHelper;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Thanos Mavroidis on 10/03/2019.
 */
public final class DocScanImageProcessor extends AbstractFileImageProcessor {

    @Override
    public String getDescription() {
        return "Document Scan Processing";
    }

    @Override
    public BufferedImage processImage(BufferedImage image) {
        final GrayScaleImage input = new GrayScaleImage(image);
        final ImageDataProcessor imageDataProcessor = buildProcessor();
        final GrayScaleImage sharp = imageDataProcessor.processImage(input);

        return GrayScaleImageHelper.toBufferedImage(sharp);
    }

    private ImageDataProcessor buildProcessor() {
        final Collection<ImageDataProcessor> processors = new ArrayList<>(2);
        processors.add(new UsmImageDataProcessor(7f));
        processors.add(new VariationsImageDataProcessor());

        return new ChainImageDataProcessor(processors);
    }
}
