package com.test.image.processors.edge;

import com.test.image.filter.FilterImageDataProcessor;
import com.test.image.filter.ImageFilter;
import com.test.image.model.Gradients;
import com.test.image.model.GrayScaleImage;

public abstract class AbstractGradientsEvaluator implements GradientsEvaluator {

    protected AbstractGradientsEvaluator() {}

    public final Gradients gradients(GrayScaleImage image) {
        final GrayScaleImage horizontal = horizontalGradients(image);
        final GrayScaleImage vertical = verticalGradients(image);

        return new Gradients(horizontal, vertical);
    }

    private GrayScaleImage horizontalGradients(GrayScaleImage image) {
        return gradients(image, getHorizontalGradientsImageFilter());
    }

    private GrayScaleImage verticalGradients(GrayScaleImage image) {
        return gradients(image, getVerticalGradientsImageFilter());
    }

    protected abstract ImageFilter getHorizontalGradientsImageFilter();

    protected abstract ImageFilter getVerticalGradientsImageFilter();

    private GrayScaleImage gradients(GrayScaleImage image, ImageFilter gradientsFilter) {
        final FilterImageDataProcessor imageProcessor = new FilterImageDataProcessor(gradientsFilter);
        return imageProcessor.processImage(image);
    }
}
