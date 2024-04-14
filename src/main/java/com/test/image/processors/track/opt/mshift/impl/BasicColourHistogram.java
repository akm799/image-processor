package com.test.image.processors.track.opt.mshift.impl;

import com.test.image.util.ColourHelper;

public final class BasicColourHistogram extends AbstractColourHistogram {

    BasicColourHistogram(int binWidth) {
        super(binWidth);
    }

    @Override
    void getRgbValues(int rgb, int[] rgbValues) {
        ColourHelper.getRgbValues(rgb, rgbValues);
    }
}
