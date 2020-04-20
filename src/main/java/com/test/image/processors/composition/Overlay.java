package com.test.image.processors.composition;

import java.awt.image.BufferedImage;

public class Overlay {
    public final int left;
    public final int top;
    public final BufferedImage image;
    public final OverlayFilter filter;

    public Overlay(BufferedImage image, int left, int top) {
        this(image, left, top, null);
    }

    public Overlay(BufferedImage image, int left, int top, OverlayFilter filter) {
        this.image = image;
        this.left = left;
        this.top = top;
        this.filter = filter;
    }
}
