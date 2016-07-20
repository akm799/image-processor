package com.test.image.model.histograms;

import com.test.image.model.collections.IntList;

public interface Bin2D {

    double x();

    double y();

    IntList entries();
}
