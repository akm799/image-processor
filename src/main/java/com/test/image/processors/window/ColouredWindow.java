package com.test.image.processors.window;

import java.awt.*;

/**
 * Created by Thanos Mavroidis on 15/07/2019.
 */
public final class ColouredWindow extends Window {
    public final int rgb;

    public ColouredWindow(Rectangle rectangle, int rgb) {
        super(rectangle);

        this.rgb = rgb;
    }
}
