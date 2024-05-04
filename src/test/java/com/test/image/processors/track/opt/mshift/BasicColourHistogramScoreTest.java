package com.test.image.processors.track.opt.mshift;

import com.test.image.processors.track.opt.mshift.impl.ColourHistogramFactory;

public class BasicColourHistogramScoreTest extends AbstractColourHistogramScoreTest {

    @Override
    ColourHistogram instance(int binWidth) {
        return ColourHistogramFactory.basicInstance(binWidth);
    }
}
