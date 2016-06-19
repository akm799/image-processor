package com.test.image;

import java.awt.image.BufferedImage;

public interface ImageProcessor {

    String getDescription();

    BufferedImage processImage(BufferedImage image);
}
