package com.test.image.filter;

import com.test.image.ImageDataProcessor;
import com.test.image.model.GrayScaleImage;
import com.test.image.util.edge.EdgeHandler;
import com.test.image.util.edge.EdgeHandlerFactory;

/**
 * https://en.wikipedia.org/wiki/Kernel_(image_processing)#Convolution
 */
public final class FilterImageDataProcessor implements ImageDataProcessor {
    private final int radius;
    private final ImageFilter imageFilter;
    private final EdgeHandler edgeHandler;

    public FilterImageDataProcessor(ImageFilter imageFilter) {
        this(imageFilter, EdgeHandlerFactory.mirrorInstance());
    }

    public FilterImageDataProcessor(ImageFilter imageFilter, EdgeHandler edgeHandler) {
        this.imageFilter = imageFilter;
        this.radius = imageFilter.getRadius();
        this.edgeHandler = edgeHandler;
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

    //TODO Investigate of we need to flip all rows and columns of the filter before applying it.
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
                final int value = edgeHandler.getPixelValueSafe(image, x, y);
                sum += weight*value;
            }
        }

        return Math.round(sum); //TODO Investigate if 'return Math.round(sum/((2*radius+1)*(2*radius+1)));' is a better option.
    }
}
