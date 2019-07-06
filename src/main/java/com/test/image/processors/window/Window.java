package com.test.image.processors.window;

import java.awt.*;

/**
 * Utility represenation of a rectangular image window.
 *
 * Created by Thanos Mavroidis on 06/07/2019.
 */
public final class Window {
    final int xMin;
    final int xMax;
    final int yMin;
    final int yMax;

    Window(Rectangle rectangle) {
        xMin = rectangle.x;
        xMax = rectangle.x + rectangle.width - 1;
        yMin = rectangle.y;
        yMax = rectangle.y + rectangle.height - 1;
    }

    @Override
    public String toString() {
        return "[topLeft=(" + xMin + ", " + yMin + "), width=" + (xMax - xMin + 1) + ", height=" + (yMax - yMin + 1) + "]";
    }
}
