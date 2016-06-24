package com.test.image.processors.corner.moravec;

import com.test.image.ImageDataProcessor;
import com.test.image.filter.FilterImageDataProcessor;
import com.test.image.filter.gaussian.GaussianImageFilter;
import com.test.image.model.Constants;
import com.test.image.model.GrayScaleImage;
import com.test.image.processors.chain.ChainImageDataProcessor;

import java.util.ArrayList;
import java.util.Collection;

public final class CornerImageDataProcessor implements ImageDataProcessor {
    private final CornerConfig config = new CornerConfig();

    @Override
    public GrayScaleImage processImage(GrayScaleImage image) {
        final GrayScaleImage output = buildProcessor(config.blurImage).processImage(image);
        capIntensities(output);

        return output;
    }

    private ImageDataProcessor buildProcessor(boolean blur) {
        final Collection<ImageDataProcessor> processors = new ArrayList<>(3);

        if (blur) {
            processors.add(new FilterImageDataProcessor(new GaussianImageFilter(config.blurRadius, config.blurSigma)));
        }

        processors.add(new MoravecCornerScoreEvaluator());
        processors.add(new NonMaximaSuppressor());

        return new ChainImageDataProcessor(processors);
    }

    private void capIntensities(GrayScaleImage image) {
        for (int j=0 ; j<image.getHeight() ; j++) {
            for (int i=0 ; i<image.getWidth() ; i++) {
                if (image.getPixel(i, j) > Constants.MAX_INTENSITY) {
                    image.setPixel(i, j, Constants.MAX_INTENSITY);
                }
            }
        }
    }
}
