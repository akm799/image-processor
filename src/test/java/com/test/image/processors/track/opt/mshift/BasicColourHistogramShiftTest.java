package com.test.image.processors.track.opt.mshift;

import com.test.image.processors.track.opt.mshift.impl.ColourHistogramFactory;

public class BasicColourHistogramShiftTest extends AbstractColourHistogramShiftTest {
    @Override
    ColourHistogram instance(int binWidth) {
        return ColourHistogramFactory.basicInstance(binWidth);
    }
}