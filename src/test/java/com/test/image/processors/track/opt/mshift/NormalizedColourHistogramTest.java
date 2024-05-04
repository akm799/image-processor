package com.test.image.processors.track.opt.mshift;

import com.test.image.processors.track.opt.mshift.impl.ColourHistogramFactory;
import com.test.image.util.ColourHelper;

public class NormalizedColourHistogramTest extends AbstractColourHistogramTest {

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
