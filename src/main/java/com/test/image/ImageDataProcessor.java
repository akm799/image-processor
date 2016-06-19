package com.test.image;


import com.test.image.model.GrayScaleImage;

public interface ImageDataProcessor {

    GrayScaleImage processImage(GrayScaleImage image);
}
