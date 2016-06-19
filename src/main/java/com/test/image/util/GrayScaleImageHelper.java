package com.test.image.util;

import com.test.image.model.GrayScaleImage;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

public class GrayScaleImageHelper {

    public static BufferedImage toBufferedImage(GrayScaleImage grayScaleImage) {
        final int width = grayScaleImage.getWidth();
        final int height = grayScaleImage.getHeight();
        final BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        final byte[] data = ((DataBufferByte)image.getRaster().getDataBuffer()).getData();
        for (int j=0 ; j<height ; j++) {
            for (int i=0 ; i<width ; i++) {
                data[j*width + i] = (byte)grayScaleImage.getPixel(i, j);
            }
        }

        return image;
    }

    private GrayScaleImageHelper() {}
}
