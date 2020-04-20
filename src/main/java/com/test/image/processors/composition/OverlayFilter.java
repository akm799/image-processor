package com.test.image.processors.composition;

/**
 * Filter optionally used in overlays in order to determine if a pixel should be overlaid or ignored.
 */
public interface OverlayFilter {

    /**
     * Returns true if this pixel should be overlaid on top of the base image or false if it should be ignored.
     *
     * @param columnIndex the column index of the pixel in the image that is being overlaid on top of the base one
     * @param rowIndex the row index of the pixel in the image that is being overlaid on top of the base one
     * @return true if this pixel should be overlaid on top of the base image or false if it should be ignored
     */
    boolean includePixel(int columnIndex, int rowIndex);
}
