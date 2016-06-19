package com.test.image.filter;

public interface ImageFilter {

    int getRadius();

    float getValue(int xOffset, int yOffset);
}
