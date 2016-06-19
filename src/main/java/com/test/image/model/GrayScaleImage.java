package com.test.image.model;

import com.test.image.util.ColourHelper;

import java.awt.image.BufferedImage;

public final class GrayScaleImage extends ImageDimensions {
    private final int[] pixels; // Integer pixel values to be used as a buffer for processed intensity values (e.g. gradients) which can be over the normal intensity limits.

    public GrayScaleImage(int width, int height) {
        super(width, height);
        this.pixels = new int[width*height];
    }

    public GrayScaleImage(ImageMetaData metaData) {
        this(metaData.getWidth(), metaData.getHeight());
        setPixels(metaData);
    }

    private void setPixels(ImageMetaData metaData) {
        for (int j=0 ; j<height ; j++) {
            for (int i=0 ; i<width ; i++) {
                setPixel(i, j, Math.round(metaData.getPixelMetaData(i, j)));
            }
        }
    }

    public GrayScaleImage(BufferedImage colourImage) {
        this(colourImage.getWidth(), colourImage.getHeight());
        setPixels(colourImage);
    }

    private void setPixels(BufferedImage colourImage) {
        for (int j=0 ; j<height ; j++) {
            for (int i=0 ; i<width ; i++) {
                final int grayScaleValue = ColourHelper.toGrayScale(colourImage.getRGB(i, j));
                setPixel(i, j, grayScaleValue);
            }
        }
    }

    public int getPixel(int x, int y) {
        return pixels[y*width + x];
    }

    public void setPixel(int x, int y, int value) {
        pixels[y*width + x] = value;
    }
}
