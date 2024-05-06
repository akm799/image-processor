package com.test.image.processors.track.opt.mshift;

import java.awt.*;

public interface MeanShift {
    Rectangle shift(ColourHistogram similarity, int[][] imagePixels, Rectangle start);
}
