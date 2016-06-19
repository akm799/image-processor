package com.test.image.filter;

import com.test.image.ImageDataProcessor;
import com.test.image.model.GrayScaleImage;


public final class FilterImageDataProcessor implements ImageDataProcessor {
    private final ImageFilter imageFilter;
    private final int radius;

    public FilterImageDataProcessor(ImageFilter imageFilter) {
        this.imageFilter = imageFilter;
        this.radius = imageFilter.getRadius();
    }

    @Override
    public GrayScaleImage processImage(GrayScaleImage image) {
        final GrayScaleImage outputImage = new GrayScaleImage(image.getWidth(), image.getHeight());
        applyFilter(image, outputImage);

        return outputImage;
    }

    private void applyFilter(GrayScaleImage image, GrayScaleImage outputImage) {
        for (int j=0 ; j<image.getHeight() ; j++) {
            for (int i=0 ; i<image.getWidth() ; i++) {
                final int value = applyFilterToPixel(image, i, j);
                outputImage.setPixel(i, j, value);
            }
        }
    }

    private int applyFilterToPixel(GrayScaleImage image, int xp, int yp) {
        final int x0 = xp - radius;
        final int y0 = yp - radius;
        final int xMax = xp + radius;
        final int yMax = yp + radius;

        float sum = 0;
        for (int y=y0 ; y<=yMax ; y++) {
            final int yOffset = y - yp;
            for (int x=x0 ; x<=xMax ; x++) {
                final int xOffset = x - xp;
                final float weight = imageFilter.getValue(xOffset, yOffset);
                final int value = getValueSafe(image, x, y);
                sum += weight*value;
            }
        }

        return Math.round(sum);
    }

    // If (x,y) falls outside the image boundaries, then mirror the pixel. This can happen for pixels
    // near the image border, where part of the filter window will fall outside the image area.
    private int getValueSafe(GrayScaleImage image, int x, int y) {
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
