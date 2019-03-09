package com.test.image.util.edge.impl;

import com.test.image.model.GrayScaleImage;
import com.test.image.util.edge.EdgeHandler;

/**
 * Implementations that uses the last boundary pixel when the requested
 * pixel falls outside the image boundary.
 *
 * Created by Thanos Mavroidis on 09/03/2019.
 */
public final class ExtendEdgeHandler implements EdgeHandler {

    @Override
    public int getPixelValueSafe(GrayScaleImage image, int x, int y) {
        if (x < 0) {
            x = 0;
        } else if (x >= image.getWidth()) {
            x = image.getWidth() - 1;
        }

        if (y < 0) {
            y = 0;
        } else if (y >= image.getHeight()) {
            y = image.getHeight() - 1;
        }

        return image.getPixel(x, y);
    }
}
