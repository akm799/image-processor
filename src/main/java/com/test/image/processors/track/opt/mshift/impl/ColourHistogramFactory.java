package com.test.image.processors.track.opt.mshift.impl;

import com.test.image.processors.track.opt.mshift.ColourHistogram;

public final class ColourHistogramFactory {

    public ColourHistogram basicInstance(int binWidth) {
        return new BasicColourHistogram(binWidth);
    }

    public ColourHistogram normalizedInstance(int nBins, int binWidth) {
        return new NormalizedColourHistogram(nBins, binWidth);
    }

    private ColourHistogramFactory() {}
}
