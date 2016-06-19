package com.test.image.processors.padding;

import com.test.image.AbstractFileImageProcessor;

import java.awt.image.BufferedImage;

public final class AddPaddingImageProcessor extends AbstractFileImageProcessor {
    private final PaddingFactors factors;

    public AddPaddingImageProcessor(PaddingFactors factors) {
        this.factors = factors;
    }

    @Override
    public BufferedImage processImage(BufferedImage image) {
        final int originalWidth = image.getWidth();
        final int extraWidthLeft = Math.round(originalWidth*factors.left);
        final int extraWidthRight = Math.round(originalWidth*factors.right);
        final int width = (originalWidth + extraWidthLeft + extraWidthRight);

        final int originalHeight = image.getHeight();
        final int extraHeightTop = Math.round(originalHeight*factors.top);
        final int extraHeightBottom = Math.round(originalHeight*factors.bottom);
        final int height = (originalHeight + extraHeightTop + extraHeightBottom);

        final BufferedImage outputImage = new BufferedImage(width, height, image.getType());
        addPadding(image, extraWidthLeft, extraHeightTop, outputImage);

        return outputImage;
    }

    private void addPadding(BufferedImage image, int extraWidthLeft, int extraHeightTop, BufferedImage outputImage) {
        for (int j=0 ; j<image.getHeight() ; j++) {
            for (int i=0 ; i<image.getWidth() ; i++) {
                final int rgb = image.getRGB(i, j);
                outputImage.setRGB(i + extraWidthLeft, j + extraHeightTop, rgb);
            }
        }
    }

    @Override
    public String getDescription() {
        return ("Added padding to the image. Padding (% of original width): " + factors);
    }
}
