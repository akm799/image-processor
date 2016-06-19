package com.test.image.model;

public final class GradientProducts {
    public final int width;
    public final int height;
    public final GrayScaleImage horizontalSquared;
    public final GrayScaleImage verticalSquared;
    public final GrayScaleImage mixedProduct;

    public GradientProducts(Gradients gradients) {
        width = gradients.width;
        height = gradients.height;
        horizontalSquared = new GrayScaleImage(width, height);
        verticalSquared = new GrayScaleImage(width, height);
        mixedProduct = new GrayScaleImage(width, height);

        fillProductValues(gradients);
    }

    private void fillProductValues(Gradients gradients) {
        for (int j=0 ; j<height ; j++) {
            for (int i=0 ; i<width ; i++) {
                final int gx = gradients.horizontal.getPixel(i, j);
                final int gy = gradients.vertical.getPixel(i, j);
                horizontalSquared.setPixel(i, j, gx*gx);
                verticalSquared.setPixel(i, j, gy*gy);
                mixedProduct.setPixel(i, j, gx*gy);
            }
        }
    }
}
