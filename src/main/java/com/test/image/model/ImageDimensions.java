package com.test.image.model;

public class ImageDimensions {
    protected final int width;
    protected final int height;

    protected ImageDimensions(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public final int getWidth() {
        return width;
    }

    public final int getHeight() {
        return height;
    }

    public final boolean isInRange(int x, int y) {
        return (0 <= x && x < width) && (0 <= y && y < height);
    }
}
