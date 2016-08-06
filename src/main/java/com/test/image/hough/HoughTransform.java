package com.test.image.hough;

import com.test.image.model.GrayScaleImage;
import com.test.image.model.histograms.Histogram2D;

public interface HoughTransform {

    void setThreshold(int threshold);

    Histogram2D transform(GrayScaleImage image);
}
