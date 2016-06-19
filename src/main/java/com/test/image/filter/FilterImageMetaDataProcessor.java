package com.test.image.filter;

import com.test.image.ImageMetaDataProcessor;
import com.test.image.model.ImageMetaData;

public final class FilterImageMetaDataProcessor implements ImageMetaDataProcessor {
    private final ImageFilter imageFilter;
    private final int radius;

    public FilterImageMetaDataProcessor(ImageFilter imageFilter) {
        this.imageFilter = imageFilter;
        this.radius = imageFilter.getRadius();
    }

    @Override
    public ImageMetaData processImageMetaData(ImageMetaData input) {
        final ImageMetaData output = new ImageMetaData(input.getWidth(), input.getHeight());
        applyFilter(input, output);

        return output;
    }

    private void applyFilter(ImageMetaData input, ImageMetaData output) {
        for (int j=0 ; j<input.getHeight() ; j++) {
            for (int i=0 ; i<input.getWidth() ; i++) {
                final float value = applyFilterToPixel(input, i, j);
                output.setPixelMetaData(i, j, value);
            }
        }
    }

    private float applyFilterToPixel(ImageMetaData input, int xp, int yp) {
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
                final float value = getValueSafe(input, x, y);
                sum += weight*value;
            }
        }

        return sum;
    }

    // If (x,y) falls outside the image boundaries, then mirror the pixel. This can happen for pixels
    // near the image border, where part of the filter window will fall outside the image area.
    private float getValueSafe(ImageMetaData input, int x, int y) {
        if (x < 0) {
            x = -x;
        } else if (x >= input.getWidth()) {
            x = 2*input.getWidth() - x - 1;
        }

        if (y < 0) {
            y = -y;
        } else if (y >= input.getHeight()) {
            y = 2*input.getHeight() - y - 1;
        }

        return input.getPixelMetaData(x, y);
    }
}
