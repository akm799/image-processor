package com.test.image.util.edge;

import com.test.image.model.GrayScaleImage;

/**
 * Interface for accessing individual image pixel intensity values even if the
 * pixels lie outside the image boundaries.
 *
 * Created by Thanos Mavroidis on 09/03/2019.
 */
public interface EdgeHandler {

    /**
     * Returns the intensity value for the (x, y) pixel of the input image
     * even if (x, y) lies outside the image boundaries. Implementations of
     * this interface should return a reasonable intensity values for pixels
     * that lie outside the image boundaries.
     *
     * @param image the input image
     * @param x the image column index. which may be outside the image boundary
     * @param y the image row index. which may be outside the image boundary
     *
     * @return the intensity value of pixel even if it lies outside the image
     * boundaries
     */
    int getPixelValueSafe(GrayScaleImage image, int x, int y);
}
