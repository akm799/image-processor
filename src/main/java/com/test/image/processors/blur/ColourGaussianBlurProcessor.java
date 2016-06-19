package com.test.image.processors.blur;

import com.test.image.AbstractFileImageProcessor;
import com.test.image.filter.ImageFilter;
import com.test.image.util.ColourHelper;

import java.awt.image.BufferedImage;

public final class ColourGaussianBlurProcessor extends AbstractFileImageProcessor {
    private final int radius;
    private final float sigma;

    private final RGBSum sum = new RGBSum(); // Use single sum object for efficiency.

    public ColourGaussianBlurProcessor(int radius, float sigma) {
        this.radius = radius;
        this.sigma = sigma;
    }

    @Override
    public BufferedImage processImage(BufferedImage image) {
        final BufferedImage outputImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        blurImage(image, outputImage);

        return outputImage;
    }

    private void blurImage(BufferedImage image, BufferedImage outputImage) {
        final ImageFilter filter = new GaussianFilter(radius, sigma);
        for (int j=0 ; j<image.getHeight() ; j++) {
            for (int i=0 ; i<image.getWidth() ; i++) {
                final int rgb = applyFilter(image, i, j, filter);
                outputImage.setRGB(i, j, rgb);
            }
        }
    }

    private int applyFilter(BufferedImage image, int xp, int yp, ImageFilter filter) {
        final int radius = filter.getRadius();
        final int x0 = xp - radius;
        final int y0 = yp - radius;
        final int xMax = xp + radius;
        final int yMax = yp + radius;

        sum.reset();
        for (int y=y0 ; y<=yMax ; y++) {
            final int yOffset = y - yp;
            for (int x=x0 ; x<=xMax ; x++) {
                final int xOffset = x - xp;
                final float weight = filter.getValue(xOffset, yOffset);
                final int rgb = getRGBSafe(image, x, y);
                sum.addWeightedRgb(weight, rgb);
            }
        }

        return sum.getRgb();
    }

    // If (x,y) falls outside the image boundaries, then mirror the pixel. This can happen for pixels
    // near the image border, where part of the filter window will fall outside the image area.
    private int getRGBSafe(BufferedImage image, int x, int y) {
        if (x < 0) {
            x = -x;
        } else if (x >= image.getWidth()) {
            x = 2*image.getWidth() - x - 1;
        }

        if (y < 0) {
            y = -y;
        } else if (y >= image.getHeight()) {
            y = 2*image.getHeight() - y - 1;
        }

        return image.getRGB(x, y);
    }

    @Override
    public String getDescription() {
        return ("Blurred the colour image using a Gaussian blur filter with radius " + radius + " and sigma " + sigma + ".");
    }

    // Sum used to get the normalized weighted average, at the end.
    private static final class RGBSum {
        private int red;
        private int green;
        private int blue;

        void reset() {
            red = 0;
            green = 0;
            blue = 0;
        }

        void addWeightedRgb(float weight, int rgb) {
            red += Math.round(weight*ColourHelper.getRed(rgb));
            green += Math.round(weight*ColourHelper.getGreen(rgb));
            blue += Math.round(weight*ColourHelper.getBlue(rgb));
        }

        int getRgb() {
            return ColourHelper.getRgb(red, green, blue);
        }
    }

    private static final class GaussianFilter implements ImageFilter {
        private final int radius;
        private final float[][] values;

        GaussianFilter(int radius, float sigma) {
            this.radius = radius;
            this.values = init(radius, sigma);
        }

        private float[][] init(int radius, float sigma) {
            final int side = 2*radius + 1;
            final float[][] values = new float[side][side];
            initValues(radius, sigma, values);

            return values;
        }

        private void initValues(int radius, float sigma, float[][] values) {
            final int side = values.length;
            final double twoSigmaSq = 2*sigma*sigma;

            float sumOfValues = 0;
            for (int j=0 ; j<side ; j++) {
                final int dy = radius - j;
                for (int i=0 ; i<side ; i++) {
                    final int dx = radius - i;
                    final double rSq = dx*dx + dy*dy;
                    values[j][i] = (float)Math.exp(-rSq/twoSigmaSq);
                    sumOfValues += values[j][i];
                }
            }

            // Normalize the values
            for (int j=0 ; j<side ; j++) {
                for (int i=0 ; i<side ; i++) {
                    values[j][i] /= sumOfValues;
                }
            }
        }

        @Override
        public int getRadius() {
            return radius;
        }

        @Override
        public float getValue(int xOffset, int yOffset) {
            return values[yOffset + radius][xOffset + radius];
        }
    }
}
