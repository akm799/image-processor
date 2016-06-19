package com.test.image.processors.edge;

import com.test.image.ImageDataProcessor;
import com.test.image.filter.FilterImageDataProcessor;
import com.test.image.filter.gaussian.GaussianImageFilter;
import com.test.image.model.Gradients;
import com.test.image.model.GrayScaleImage;
import com.test.image.processors.chain.ChainImageDataProcessor;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Canny edge detection implementation.
 *
 * https://en.wikipedia.org/wiki/Canny_edge_detector
 */
public final class EdgeImageDataProcessor implements ImageDataProcessor {
    private final EdgeConfig config = new EdgeConfig();

    @Override
    public GrayScaleImage processImage(GrayScaleImage image) {
        final ImageDataProcessor imageDataProcessor = buildProcessorChain(config.blurImage, config.performNonMaximumSuppression, config.performDoubleThresholdAnalysis, config.performHysterisisBlobAnalysis);

        return imageDataProcessor.processImage(image);
    }

    private ImageDataProcessor buildProcessorChain(boolean blur, boolean nonMaxSuppression, boolean doubleThreshold, boolean hysterisis) {
        final Collection<ImageDataProcessor> processors = new ArrayList<>(4);

        if (blur) {
            processors.add(new FilterImageDataProcessor(new GaussianImageFilter(config.blurRadius, config.blurSigma)));
        }

        if (nonMaxSuppression) {
            processors.add(new FirstEdgeApproxImageDataProcessor(new NonMaximaSuppressor()));
        } else {
            processors.add(new FirstEdgeApproxImageDataProcessor(new IdentityGradientsProcessor()));
        }

        if (doubleThreshold) {
            processors.add(new DoubleThresholdProcessor(config.lowThresholdReductionFactor));
            if (hysterisis) { // Hysterisis analysis is not applicable if the double threshold analysis has not been carried out.
                processors.add(new HysterisisProcessor());
            }
        }

        return new ChainImageDataProcessor(processors);
    }

    private static final class FirstEdgeApproxImageDataProcessor implements ImageDataProcessor {
        private final ImageGradientsDataProcessor gradientsDataProcessor;
        private final GradientsEvaluator gradientsEvaluator = new SobelGradientsEvaluator();

        FirstEdgeApproxImageDataProcessor(ImageGradientsDataProcessor gradientsDataProcessor) {
            this.gradientsDataProcessor = gradientsDataProcessor;
        }

        @Override
        public GrayScaleImage processImage(GrayScaleImage image) {
            final Gradients gradients = gradientsEvaluator.gradients(image);

            return  gradientsDataProcessor.processGradients(gradients);
        }
    }
}
