package com.test.image.processors.circle;

import com.test.image.AbstractFileImageProcessor;

import java.awt.image.BufferedImage;

/**
 * Image processor that places an empty ring at the middle of the input square image.
 * This placement, will result in a square output image greater in size than the input
 * image, so as to accommodate the empty ring placed in the middle.
 */
public final class RingImageProcessor extends AbstractFileImageProcessor {
    private final double centreRadiusFraction;

    /**
     * The radius of the ring placed in the middle of the input image will be equal to the half-size of the input square
     * image times the constructor input argument.
     *
     * @param centreRadiusFraction the fraction of the input image half side which will yield the radius of the ring
     */
    public RingImageProcessor(double centreRadiusFraction) {
        if (centreRadiusFraction <= 0) {
            throw new IllegalArgumentException("'centreRadiusFraction' argument cannot be zero or less but it is " + centreRadiusFraction);
        }

        this.centreRadiusFraction = centreRadiusFraction;
    }

    @Override
    public String getDescription() {
        return "Placed an empty ring at the middle of the input square image.";
    }

    @Override
    public BufferedImage processImage(BufferedImage image) {
        final BufferedImage outputImage = buildEnlargedOutputImage(image);
        placeRingInTheMiddle(image, outputImage);

        return outputImage;
    }

    private BufferedImage buildEnlargedOutputImage(BufferedImage input) {
        final int increase = (int)Math.ceil(centreRadiusFraction*Math.min(input.getWidth(), input.getHeight()));
        final int increasedWidth = input.getWidth() + increase;
        final int increasedHeight = input.getHeight() + increase;

        return new BufferedImage(increasedWidth, increasedHeight, input.getType());
    }

    //TODO Investigate why for non-square images the output is not symmetric.
    private void placeRingInTheMiddle(BufferedImage input, BufferedImage output) {
        final int w = input.getWidth();
        final int h = input.getHeight();

        final double r = Math.min(w, h)/2.0;
        final double a = r*centreRadiusFraction;
        final double aSq = a*a;

        final int ow = output.getWidth();
        final int oh = output.getHeight();
        final int owm1 = ow - 1;
        final int ohm1 = oh - 1;
        final double orx = ow/2.0;
        final double ory = oh/2.0;

        int ip, jp; // x-y range must be between 0 and ow-oh
        double dx, dy;
        for (int j=0 ; j<h ; j++) {
            final double y = h - j - r;
            final double ySq = y*y;
            for (int i=0 ; i<w ; i++) {
                final double x = i - r;

                if (x != 0.0) {
                    dx = a / Math.sqrt(1 + ySq/(x*x));
                    dy = Math.sqrt(aSq - dx*dx);
                } else {
                    dx = 0;
                    dy = a;
                }

                final double xp = (x >= 0) ? x + dx : x - dx;
                final double yp = (y >= 0) ? y + dy : y - dy;

                ip = (int)Math.round(xp + orx);
                jp = oh - (int)Math.round(yp + ory);

                if (ip < 0) {
                    ip = 0;
                } else if (ip >= ow) {
                    ip = owm1;
                }

                if (jp < 0) {
                    jp = 0;
                } else if (jp >= oh) {
                    jp = ohm1;
                }

                output.setRGB(ip, jp, input.getRGB(i, j));
            }
        }
    }
}
