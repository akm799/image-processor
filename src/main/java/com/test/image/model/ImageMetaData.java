package com.test.image.model;


public final class ImageMetaData extends ImageDimensions {
    private final float[] pixelMetaData; // Float pixel values to be used as a buffer for processed intensity values (e.g. gradient directions).

    public ImageMetaData(int width, int height) {
        super(width, height);
        this.pixelMetaData = new float[width*height];
    }

    public float getPixelMetaData(int x, int y) {
        return pixelMetaData[y*width + x];
    }

    public void setPixelMetaData(int x, int y, float value) {
        pixelMetaData[y*width + x] = value;
    }
}
