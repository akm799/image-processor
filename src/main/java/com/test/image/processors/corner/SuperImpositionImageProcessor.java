package com.test.image.processors.corner;

import com.test.image.ImageProcessor;
import com.test.image.model.Constants;
import com.test.image.model.GrayScaleImage;
import com.test.image.util.ColourHelper;

import java.awt.image.BufferedImage;

/**
 * Created by Thanos Mavroidis on 02/03/2019.
 */
final class SuperImpositionImageProcessor implements ImageProcessor {
    private final GrayScaleImage selectedPixels;

    SuperImpositionImageProcessor(GrayScaleImage selectedPixels) {
        this.selectedPixels = selectedPixels;
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
                final int normalizedCornerValue = selectedPixels.getPixel(i, j);
                if (normalizedCornerValue > 0) {
                    output.setRGB(i, j, colourForNormalizedValue(normalizedCornerValue));
                } else {
                    output.setRGB(i, j, image.getRGB(i, j));
                }
            }
        }
    }

    // Blue to red (cold-hot) colour: min=blue to max=red
    private int colourForNormalizedValue(int normalizedValue) {
        final int red = normalizedValue;
        final int green = 0;
        final int blue = Constants.MAX_INTENSITY - normalizedValue;

        return ColourHelper.getRgb(red, green, blue);
    }
}