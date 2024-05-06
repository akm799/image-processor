package com.test.image.processors.track.opt.mshift.histo;

import com.test.image.processors.track.opt.mshift.ColourHistogram;
import com.test.image.processors.track.opt.mshift.impl.histo.ColourHistogramFactory;
import com.test.image.util.ColourHelper;

public class NormalizedColourHistogramFillTest extends AbstractColourHistogramFillTest {

    @Override
    ColourHistogram instance(int binWidth) {
        return ColourHistogramFactory.normalizedInstance(binWidth);
    }

    @Override
    int getExampleRgb() {
        return ColourHelper.getRgb(52, 127, 210);
    }

    @Override
    void getExampleRgbBin(int[] bin) {
        bin[redIndex] = 6;
        bin[greenIndex] = 16;
        bin[blueIndex] = 27;
    }
}
