package com.test.image;

import com.test.image.model.GrayScaleImage;

/**
 * Created by Thanos Mavroidis on 06/03/2019.
 */
public interface CombinationImageDataProcessor {

    GrayScaleImage combineImages(GrayScaleImage image1, GrayScaleImage image2);
}
