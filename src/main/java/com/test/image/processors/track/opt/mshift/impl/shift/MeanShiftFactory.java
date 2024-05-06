package com.test.image.processors.track.opt.mshift.impl.shift;

import com.test.image.processors.track.opt.mshift.MeanShift;

public final class MeanShiftFactory {

    public static MeanShift instance(int deltaMin, int maxTries) {
        return new MeanShiftImpl(deltaMin, maxTries);
    }

    private MeanShiftFactory() {}
}
