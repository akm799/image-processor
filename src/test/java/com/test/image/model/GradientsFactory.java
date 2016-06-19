package com.test.image.model;

public class GradientsFactory {

    private GradientsFactory() {}

    public static Gradients instance(ImageMetaData magnitudes, ImageMetaData directions) {
        final GrayScaleImage horizontal = new GrayScaleImage(magnitudes.getWidth(), magnitudes.getHeight());
        final GrayScaleImage vertical = new GrayScaleImage(magnitudes.getWidth(), magnitudes.getHeight());
        final Gradients gradients = new Gradients(horizontal, vertical);
        for (int j=0 ; j<magnitudes.getHeight() ; j++) {
            for (int i=0 ; i<magnitudes.getWidth() ; i++) {
                final float magnitude = magnitudes.getPixelMetaData(i, j);
                final float angle = directions.getPixelMetaData(i, j);
                gradients.magnitudes.setPixelMetaData(i, j, magnitude);
                gradients.directions.setPixelMetaData(i, j, angle);

                final int gx = (int)Math.round(magnitude*Math.cos(angle));
                final int gy = (int)Math.round(magnitude*Math.sin(angle));
                gradients.horizontal.setPixel(i, j, gx);
                gradients.vertical.setPixel(i, j, gy);
            }
        }

        return gradients;
    }
}
