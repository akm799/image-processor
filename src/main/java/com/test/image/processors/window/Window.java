package com.test.image.processors.window;

import java.awt.*;

/**
 * Utility representation of a rectangular image window.
 *
 * Created by Thanos Mavroidis on 06/07/2019.
 */
public final class Window {
    public final int width;
    public final int height;
    public final int xMin;
    public final int xMax;
    public final int yMin;
    public final int yMax;

    public Window(Rectangle rectangle) {
        width = rectangle.width;
        height = rectangle.height;
        xMin = rectangle.x;
        xMax = rectangle.x + rectangle.width - 1;
        yMin = rectangle.y;
        yMax = rectangle.y + rectangle.height - 1;
    }

    @Override
    public String toString() {
        return "[topLeft=(" + xMin + ", " + yMin + "), width=" + width + ", height=" + height + "]";
    }
}
