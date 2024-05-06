package com.test.image.processors.track.opt.mshift.impl.histo;

import com.test.image.util.ColourHelper;

public final class NormalizedColourHistogram extends AbstractColourHistogram {

    NormalizedColourHistogram(int binWidth) {
        super(binWidth);
    }

    @Override
    void getRgbValues(int rgb, int[] rgbValues) {
        ColourHelper.getNormalizedRgbValues(rgb, rgbValues);
    }
}
