package com.test.image.processors.edge;

import com.test.image.model.GrayScaleImage;

public interface ThresholdFinder {

    int findThreshold(GrayScaleImage image);
}
