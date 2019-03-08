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
final class UsmImageDataProcessor implements ImageDataProcessor {
    private final ImageDataProcessor blur = new FilterImageDataProcessor(new GaussianImageFilter(5, 9.0f));
    private final IntensityScaleImageDataProcessor scale = new IntensityScaleImageDataProcessor(0.7f);
    private final CombinationImageDataProcessor add = new AddImageDataProcessor();
    private final CombinationImageDataProcessor subtract = new SubtractImageDataProcessor();

    UsmImageDataProcessor() {}

    @Override
    public GrayScaleImage processImage(GrayScaleImage original) {
        final GrayScaleImage blurred = blur.processImage(original);
        final GrayScaleImage originalMinusBlurred = subtract.combineImages(original, blurred);
        final GrayScaleImage originalMinusBlurredScaled = scale.processImage(originalMinusBlurred);

        return add.combineImages(original, originalMinusBlurredScaled);
    }
}
