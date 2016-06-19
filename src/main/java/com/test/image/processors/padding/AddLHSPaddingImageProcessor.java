package com.test.image.processors.padding;

import com.test.image.AbstractFileImageProcessor;

import java.awt.image.BufferedImage;

public final class AddLHSPaddingImageProcessor extends AbstractFileImageProcessor {
    private final float extraWidthFactor;

    public AddLHSPaddingImageProcessor(float extraWidthFactor) {
        this.extraWidthFactor = extraWidthFactor;
    }

    @Override
    public BufferedImage processImage(BufferedImage image) {
        final int width = image.getWidth();
        final int extraWidth = Math.round(width*extraWidthFactor);
        final BufferedImage outputImage = new BufferedImage(width + extraWidth, image.getHeight(), image.getType());
        addLHSPadding(image, outputImage);

        return outputImage;
    }

    private void addLHSPadding(BufferedImage image, BufferedImage outputImage) {
        final int widthDiff = outputImage.getWidth() - image.getWidth();
        for (int j=0 ; j<image.getHeight() ; j++) {
            for (int i=0 ; i<image.getWidth() ; i++) {
                final int rgb = image.getRGB(i, j);
                outputImage.setRGB(i + widthDiff, j, rgb);
            }
        }
    }

    @Override
    public String getDescription() {
        return ("Added padding to the LHS of the image (" + (100*extraWidthFactor) + "% of the original image width).");
    }
}
