package com.test.image.processors.corner.moravec;

import com.test.image.ImageDataProcessor;
import com.test.image.model.GrayScaleImage;
import com.test.image.processors.chain.ChainImageDataProcessor;

import java.util.ArrayList;
import java.util.Collection;

/**
 * https://arxiv.org/pdf/1209.1558.pdf
 *
 * Look at "V. Moravec Corner Detection" inside the document at the link above.
 */
public final class CornerImageDataProcessor implements ImageDataProcessor {
    private final int lowSquareDiffSumValueThreshold = 0; //TODO Define a value.

    @Override
    public GrayScaleImage processImage(GrayScaleImage image) {
        return buildProcessor().processImage(image);
    }

    private ImageDataProcessor buildProcessor() {
        final Collection<ImageDataProcessor> processors = new ArrayList<>(3);
        processors.add(new MoravecCornerScoreEvaluator());
        processors.add(new LowValueSuppressor(lowSquareDiffSumValueThreshold));
        processors.add(new NonMaximaSuppressor());

        return new ChainImageDataProcessor(processors);
    }
}
