package com.test.image.processors.gray;

import com.test.image.AbstractFileImageProcessor;
import com.test.image.model.GrayScaleImage;
import com.test.image.util.GrayScaleImageHelper;

import java.awt.image.BufferedImage;

public class GrayScaleImageProcessor extends AbstractFileImageProcessor {

    @Override
    public String getDescription() {
        return ("Converted the image to gray-scale using a basic average conversion.");
    }

    @Override
    public BufferedImage processImage(BufferedImage image) {
        return GrayScaleImageHelper.toBufferedImage(new GrayScaleImage(image));
    }
}
