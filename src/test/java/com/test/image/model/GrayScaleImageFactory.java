package com.test.image.model;

public class GrayScaleImageFactory {

    public static GrayScaleImage instance(int[][] pixels) {
        final GrayScaleImage image = new GrayScaleImage(pixels[0].length, pixels.length);
        for (int j=0 ; j<pixels.length ; j++) {
            for (int i=0 ; i<pixels[j].length ; i++) {
                image.setPixel(i, j, pixels[j][i]);
            }
        }

        return image;
    }

    private GrayScaleImageFactory() {}
}
