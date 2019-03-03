package com.test.image.processors.corner;

import com.test.image.ImageProcessor;
import com.test.image.model.Constants;
import com.test.image.model.GrayScaleImage;
import com.test.image.util.ColourHelper;

import java.awt.image.BufferedImage;

/**
 * Produces an output image which is a copy of the input image with pixels from the constructor-input
 * gray scale image data superimposed. Each pixel in the constructor-input image will result in a
 * (superimposed) pixel with a colour ranging from red to blue proportional to the intensity of the
 * gray scale constructor-input image.
 *
 * Created by Thanos Mavroidis on 02/03/2019.
 */
final class SuperImpositionImageProcessor implements ImageProcessor {
    private final GrayScaleImage selectedPixels;

    SuperImpositionImageProcessor(GrayScaleImage selectedPixels) {
        this.selectedPixels = (new Normalizer()).processImage(selectedPixels); // Normalize the data in the input image.
    }

    @Override
    public String getDescription() {
        return "Superimposes pixels on the input image.";
    }

    @Override
    public BufferedImage processImage(BufferedImage image) {
        checkDimensions(image);

        final BufferedImage output = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
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
