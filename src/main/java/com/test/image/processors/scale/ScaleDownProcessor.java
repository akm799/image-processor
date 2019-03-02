package com.test.image.processors.scale;

import com.test.image.AbstractFileImageProcessor;
import com.test.image.util.ColourHelper;

import java.awt.image.BufferedImage;

public class ScaleDownProcessor extends AbstractFileImageProcessor {
    private static final int SCALE = 2;

    private final int[] buffer = new int[2*SCALE];

    @Override
    public String getDescription() {
        return "Scaled down image.";
    }

    @Override
    public BufferedImage processImage(BufferedImage image) {
        final BufferedImage outputImage = new BufferedImage(image.getWidth()/SCALE, image.getHeight()/SCALE, image.getType());
        scaleDownImage(image, outputImage);

        return outputImage;
    }

    private void scaleDownImage(BufferedImage image, BufferedImage outputImage) {
        for (int j=0 ; j<outputImage.getHeight() ; j++) {
            for (int i=0 ; i<outputImage.getWidth() ; i++) {
                final int rgb = avg(image, SCALE*i, SCALE*j);
                outputImage.setRGB(i, j, rgb);
            }
        }
    }

    private int avg(BufferedImage image, int outX, int outY) {
        buffer[0] = image.getRGB(outX,     outY);
        buffer[1] = image.getRGB(outX + 1, outY);
        buffer[2] = image.getRGB(outX,     outY + 1);
        buffer[3] = image.getRGB(outX + 1, outY + 1);

        return avg(buffer);
    }

    private int avg(int[] rgbValues) {
        int a = 0;
        int r = 0;
        int g = 0;
        int b = 0;

        final int n = rgbValues.length;
        for (int i=0 ; i<n ; i++) {
            a += ColourHelper.getAlpha(rgbValues[i]);
            r += ColourHelper.getRed(rgbValues[i]);
            g += ColourHelper.getGreen(rgbValues[i]);
            b += ColourHelper.getBlue(rgbValues[i]);
        }

        return ColourHelper.getRgb(r/n, g/n, b/n, a/n);
    }
}
