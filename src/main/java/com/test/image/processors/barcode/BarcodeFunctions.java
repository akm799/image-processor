package com.test.image.processors.barcode;

import com.test.image.model.GrayScaleImage;
import com.test.image.model.Kernel;
import com.test.image.processors.blur.KernelGenerator;

final class BarcodeFunctions {

    BarcodeFunctions() {}

    GrayScaleImage scaleDown(GrayScaleImage data) {
        final int w = data.getWidth();
        final int h = data.getHeight();
        final int ws = w/2 + w%2;
        final int hs = h/2 + h%2;
        final GrayScaleImage scaled = new GrayScaleImage(ws, hs);

        for (int j=0 ; j<=h ; j += 2) {
            for (int i=0 ; i<=w ; i += 2) {
                if (i<w && j<h) {
                    final int s = data.getPixel(i, j) + data.getPixel(i+ 1, j) + data.getPixelIndex(i, j+ 1) + data.getPixelIndex(i+1, j+1);
                    scaled.setPixel(i/2, j/2, Math.round(s/4f));
                } else {
                    //TODO
                }
            }
        }

        return scaled;
    }

    GrayScaleImage gradient(GrayScaleImage data) {
        final int w = data.getWidth();
        final int h = data.getHeight();
        final GrayScaleImage gradient = new GrayScaleImage(w, h);

        for (int j=0 ; j<h ; j++) {
            for (int i=0 ; i<w ; i++) {
                final int pxMinus = getSafePixel(data, w, h, i-1, j);
                final int pxPlus = getSafePixel(data, w, h, i+1, j);
                final int gx = Math.abs(pxPlus - pxMinus);

                final int pyMinus = getSafePixel(data, w, h, i, j-1);
                final int pyPlus = getSafePixel(data, w, h, i, j+1);
                final int gy = Math.abs(pyPlus - pyMinus);

                final int gd = gx - gy;
                gradient.setPixel(i, j, gd > 0 ? gd : 0);
            }
        }

        return gradient;
    }

    GrayScaleImage smooth(GrayScaleImage data) {
        final int kernelSize = 51;

        return gaussianFilter(data, kernelSize);
    }

    /**
     * <a href="https://nishatlea.medium.com/finding-out-the-values-of-a-gaussian-kernel-in-image-processing-9caaf213b4ab">Barcode Detection Reference Implementation</a>
     */
    private GrayScaleImage gaussianFilter(GrayScaleImage data, int size) {
        final Kernel kernel = (new KernelGenerator()).gaussianKernel(size);
        final GrayScaleImage smooth = new GrayScaleImage(data.getWidth(), data.getHeight());
        gaussianFilter(data, kernel, smooth);

        return smooth;
    }

    private void gaussianFilter(GrayScaleImage source, Kernel kernel, GrayScaleImage target) {
        final int w = source.getWidth();
        final int h = source.getHeight();
        for (int j=0 ; j<h ; j++) {
            for (int i=0 ; i<w ; i++) {
                final int c = kernel.applyMirror(source, w, h, i, j);
                target.setPixel(i, j, c);
            }
        }
    }

    GrayScaleImage binary(GrayScaleImage image) {
        final int threshold = 10;
        final int w = image.getWidth();
        final int h = image.getHeight();
        final GrayScaleImage binary = new GrayScaleImage(w, h);
        for (int j=0 ; j<h ; j++) {
            for (int i=0 ; i<w ; i++) {
                if (image.getPixel(i, j) > threshold) {
                    binary.setPixel(i, j, 255);
                }
            }
        }

        return binary;
    }

    private int getSafePixel(GrayScaleImage data, int w, int h, int x, int y) {
        final int xSafe = getSafe(w, x);
        final int ySafe = getSafe(h, y);

        return data.getPixel(xSafe, ySafe);
    }

    private int getSafe(int maxExclusive, int i) {
        if (i < 0) {
            return -i;
        } else if (i >= maxExclusive) {
            return maxExclusive - i + maxExclusive - 2;
        } else {
            return i;
        }
    }
}
