package com.test.image.processors.corner;

import com.test.image.ImageProcessor;
import com.test.image.model.GrayScaleImage;

import java.awt.image.BufferedImage;

/**
 * Created by Thanos Mavroidis on 02/03/2019.
 */
final class SuperImpositionImageProcessor implements ImageProcessor {
    private final int superImpositionColour;
    private final GrayScaleImage selectedPixels;

    SuperImpositionImageProcessor(GrayScaleImage selectedPixels, int superImpositionColour) {
        this.selectedPixels = selectedPixels;
        this.superImpositionColour = superImpositionColour;
    }

    @Override
    public String getDescription() {
        return "Superimposes pixels on the input image.";
    }

    @Override
    public BufferedImage processImage(BufferedImage image) {
        checkDimensions(image);

        final BufferedImage output = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        superImposePixels(image, output);

        return output;
    }

    private void checkDimensions(BufferedImage image) {
        if (image.getWidth() != selectedPixels.getWidth() || image.getHeight() != selectedPixels.getHeight()) {
            throw new IllegalArgumentException("Incompatible dimensions of the coloured input image (" + image.getWidth() + ", " + image.getHeight() + ") and the superposition gray-scale image (" + selectedPixels.getWidth() + ", " + selectedPixels.getHeight() + ").");
        }
    }

    private void superImposePixels(BufferedImage image, BufferedImage output) {
        for (int j=0 ; j<image.getHeight() ; j++) {
            for (int i=0 ; i<image.getWidth() ; i++) {
                if (selectedPixels.getPixel(i, j) > 0) {
                    output.setRGB(i, j, superImpositionColour);
                } else {
                    output.setRGB(i, j, image.getRGB(i, j));
                }
            }
        }
    }
}
