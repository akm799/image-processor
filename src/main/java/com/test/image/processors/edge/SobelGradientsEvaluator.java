package com.test.image.processors.edge;

import com.test.image.filter.ImageFilter;
import com.test.image.filter.sobel.SobelImageFilterFactory;

//TODO Add unit tests
public final class SobelGradientsEvaluator extends AbstractGradientsEvaluator {

    SobelGradientsEvaluator() {}

    @Override
    protected ImageFilter getHorizontalGradientsImageFilter() {
        return SobelImageFilterFactory.getHorizontal5x5Instance();
    }

    @Override
    protected ImageFilter getVerticalGradientsImageFilter() {
        return SobelImageFilterFactory.getVertical5x5Instance();
    }
}
