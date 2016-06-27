package com.test.image.processors.corner.moravec;

import com.test.image.ImageDataProcessor;
import com.test.image.filter.FilterImageDataProcessor;
import com.test.image.filter.gaussian.GaussianImageFilter;
import com.test.image.model.GrayScaleImage;
import com.test.image.processors.chain.ChainImageDataProcessor;
import com.test.image.processors.edge.EdgeImageDataProcessor;

import java.util.ArrayList;
import java.util.Collection;

public final class CornerImageDataProcessor implements ImageDataProcessor {
    private final CornerConfig config = new CornerConfig();

    @Override
    public GrayScaleImage processImage(GrayScaleImage image) {
        final GrayScaleImage output = buildProcessor().processImage(image);

        return output;
    }

    private ImageDataProcessor buildProcessor() {
        final Collection<ImageDataProcessor> processors = new ArrayList<>(6);

        if (config.blurImage && !config.edgeProcess) {
            processors.add(new FilterImageDataProcessor(new GaussianImageFilter(config.blurRadius, config.blurSigma)));
        }

        if (config.edgeProcess) {
            processors.add(new EdgeImageDataProcessor());
        }

        processors.add(new MoravecCornerScoreEvaluator());
        processors.add(new NonMaximaSuppressor());
        processors.add(new Normalizer());
        processors.add(new MoravecThresholdApplier());

        return new ChainImageDataProcessor(processors);
    }
}
