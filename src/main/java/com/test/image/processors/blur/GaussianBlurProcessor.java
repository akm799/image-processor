package com.test.image.processors.blur;

import com.test.image.AbstractFileImageProcessor;
import com.test.image.filter.FilterImageDataProcessor;
import com.test.image.filter.gaussian.GaussianImageFilter;
import com.test.image.model.GrayScaleImage;
import com.test.image.util.GrayScaleImageHelper;

import java.awt.image.BufferedImage;

public class GaussianBlurProcessor extends AbstractFileImageProcessor {
    private final int radius;
    private final float sigma;

    public GaussianBlurProcessor(int radius, float sigma) {
        this.radius = radius;
        this.sigma = sigma;
    }

    @Override
    public BufferedImage processImage(BufferedImage image) {
        final GrayScaleImage input = new GrayScaleImage(image);
        final FilterImageDataProcessor imageProcessor = new FilterImageDataProcessor(new GaussianImageFilter(radius, sigma));
        final GrayScaleImage output = imageProcessor.processImage(input);

        return GrayScaleImageHelper.toBufferedImage(output);
    }

    @Override
    public String getDescription() {
        return ("Blurred the converted gray-scale image using a Gaussian blur filter with radius " + radius + " and sigma " + sigma + ".");
    }
}
