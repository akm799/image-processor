package com.test.image.util.edge.impl;

import com.test.image.model.GrayScaleImage;
import com.test.image.util.edge.EdgeHandler;

/**
 * Implementations that mirrors the requested pixel is it happens
 * to lie outside the image boundary.
 *
 * Created by Thanos Mavroidis on 09/03/2019.
 */
public final class MirrorEdgeHandler implements EdgeHandler {

    @Override
    public int getPixelValueSafe(GrayScaleImage image, int x, int y) {
        if (x < 0) {
            x = -x;
        } else if (x >= image.getWidth()) {
            x = 2*image.getWidth() - x - 1;
        }

        if (y < 0) {
            y = -y;
        } else if (y >= image.getHeight()) {
            y = 2*image.getHeight() - y - 1;
        }

        return image.getPixel(x, y);
    }
}
