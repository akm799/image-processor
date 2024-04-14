package com.test.image.processors.track.opt.mshift.impl;

import com.test.image.processors.track.opt.mshift.ColourHistogram;

public final class ColourHistogramFactory {

    public static ColourHistogram basicInstance(int binWidth) {
        return new BasicColourHistogram(binWidth);
    }

    public static ColourHistogram normalizedInstance(int nBins, int binWidth) {
        return new NormalizedColourHistogram(nBins, binWidth);
    }

    private ColourHistogramFactory() {}
}
