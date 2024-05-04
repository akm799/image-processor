package com.test.image.processors.track.opt.mshift;

import com.test.image.processors.track.opt.mshift.impl.ColourHistogramFactory;
import com.test.image.util.ColourHelper;

public class BasicColourHistogramFillTest extends AbstractColourHistogramFillTest {

    @Override
    ColourHistogram instance(int binWidth) {
        return ColourHistogramFactory.basicInstance(binWidth);
    }

    @Override
    int getExampleRgb() {
        return ColourHelper.getRgb(54, 127, 210);
    }

    @Override
    void getExampleRgbBin(int[] bin) {
        bin[redIndex] = 10;
        bin[greenIndex] = 25;
        bin[blueIndex] = 42;
    }
}
