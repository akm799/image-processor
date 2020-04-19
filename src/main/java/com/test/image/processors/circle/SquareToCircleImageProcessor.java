package com.test.image.processors.circle;

import com.test.image.AbstractFileImageProcessor;

import java.awt.image.BufferedImage;

/**
 * Various transformations from square to circle and vice versa.
 * https://arxiv.org/pdf/1509.06344.pdf
 */
public final class SquareToCircleImageProcessor extends AbstractFileImageProcessor {

    @Override
    public String getDescription() {
        return "Transformed the square image into circular one.";
    }

    @Override
    public BufferedImage processImage(BufferedImage image) {
        final BufferedImage outputImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        transformToCircle(image, outputImage);

        return outputImage;
    }

    /**
     * Uses the elliptical transform in https://arxiv.org/pdf/1509.06344.pdf
     */
    private void transformToCircle(BufferedImage input, BufferedImage output) {
        final int w = input.getWidth();
        final int h = input.getHeight();
        final int wm1 = w - 1;
        final int hm1 = h - 1;
        final double xw = 2.0/w;
        final double yh = 2.0/h;

        double u, v;
        double x, y; // x-y range must be between -1 and 1
        double yf;
        int ip, jp; // x-y range must be between 0 and w-h
        for (int j=0 ; j<h ; j++) {
            y = j*yh - 1;
            yf = Math.sqrt(1 - y*y/2);
            for (int i=0 ; i<w ; i++) {
                x = i*xw - 1;

                u = x*yf;
                v = y*Math.sqrt(1 - x*x/2);
                ip = (int)Math.round((u + 1)/xw);
                jp = (int)Math.round((v + 1)/yh);

                if (ip >= w) {
                    ip = wm1;
                }

                if (jp >= h) {
                    jp = hm1;
                }

                output.setRGB(ip, jp, input.getRGB(i, j));
            }
        }
    }
}
