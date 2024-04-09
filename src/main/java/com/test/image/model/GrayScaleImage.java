package com.test.image.model;

import com.test.image.util.ColourHelper;

import java.awt.*;
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

    private GrayScaleImage(int[] pixels, int width) {
        super(width, pixels.length/width);
        this.pixels = pixels;
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

    public int getPixelIndex(int x, int y) {
        return (y*width + x);
    }

    public int getPixel(int index) {
        return pixels[index];
    }

    public void setPixel(int index, int value) {
        pixels[index] = value;
    }

    public GrayScaleImage cut(Rectangle segment) {
        if (isInside(segment, this)) {
            final int maxY = segment.y + segment.height;
            final int[] pixels = new int[segment.width * segment.height];
            for (int j=segment.y ; j<maxY ; j++) {
                final int srcPos = segment.x + j*this.width;
                final int destPos = (j - segment.y) * segment.width;
                System.arraycopy(this.pixels, srcPos, pixels, destPos, segment.width);
            }

            return new GrayScaleImage(pixels, segment.width);
        } else {
            return null;
        }
    }

    private boolean isInside(Rectangle segment, GrayScaleImage data) {
        if (segment.x < 0 || segment.x >= data.width || segment.y < 0 || segment.y >= data.height) {
            return false;
        }

        return segment.x + segment.width <= data.width && segment.y + segment.height <= data.height;
    }
}
