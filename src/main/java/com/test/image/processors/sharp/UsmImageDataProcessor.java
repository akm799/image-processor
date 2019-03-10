package com.test.image.processors.sharp;

import com.test.image.CombinationImageDataProcessor;
import com.test.image.ImageDataProcessor;
import com.test.image.filter.FilterImageDataProcessor;
import com.test.image.filter.gaussian.GaussianImageFilter;
import com.test.image.model.GrayScaleImage;
import com.test.image.processors.scale.IntensityScaleImageDataProcessor;

/**
 * UnSharp Masking algorithm implementation.
 *
 * https://en.wikipedia.org/wiki/Unsharp_masking
 *
 * https://homepages.inf.ed.ac.uk/rbf/HIPR2/unsharp.htm
 * 
 * Created by Thanos Mavroidis on 06/03/2019.
 */
public final class UsmImageDataProcessor implements ImageDataProcessor {
    private static final float DEFAULT_SCALE_FACTOR = 0.7f;
    private static final int DEFAULT_RADIUS = 5;
    private static final float DEFAULT_SIGMA = 9f;

    private final CombinationImageDataProcessor add = new AddImageDataProcessor();
    private final CombinationImageDataProcessor subtract = new SubtractImageDataProcessor();

    private final ImageDataProcessor blur;
    private final IntensityScaleImageDataProcessor scale;

    public UsmImageDataProcessor() {
        this(DEFAULT_SCALE_FACTOR);
    }

    public UsmImageDataProcessor(float scaleFactor) {
        this(scaleFactor, DEFAULT_RADIUS, DEFAULT_SIGMA);
    }

    public UsmImageDataProcessor(float scaleFactor, int radius, float sigma) {
        blur = new FilterImageDataProcessor(new GaussianImageFilter(radius, sigma));
        scale = new IntensityScaleImageDataProcessor(scaleFactor);
    }

    @Override
    public GrayScaleImage processImage(GrayScaleImage original) {
        final GrayScaleImage blurred = blur.processImage(original);
        final GrayScaleImage originalMinusBlurred = subtract.combineImages(original, blurred);
        final GrayScaleImage originalMinusBlurredScaled = scale.processImage(originalMinusBlurred);

        return add.combineImages(original, originalMinusBlurredScaled);
    }
}
