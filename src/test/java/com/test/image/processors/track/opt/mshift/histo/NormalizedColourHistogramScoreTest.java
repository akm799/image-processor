package com.test.image.processors.track.opt.mshift.histo;

import com.test.image.processors.track.opt.mshift.ColourHistogram;
import com.test.image.processors.track.opt.mshift.impl.histo.ColourHistogramFactory;

public class NormalizedColourHistogramScoreTest extends AbstractColourHistogramScoreTest {

    @Override
    ColourHistogram instance(int binWidth) {
        return ColourHistogramFactory.normalizedInstance(binWidth);
    }
}
