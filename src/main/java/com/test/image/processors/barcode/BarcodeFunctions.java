package com.test.image.processors.barcode;

import com.test.image.model.GrayScaleImage;
import com.test.image.model.Kernel;
import com.test.image.model.Location;
import com.test.image.processors.blur.KernelGenerator;

final class BarcodeFunctions {

    BarcodeFunctions() {}

    GrayScaleImage scaleDown(GrayScaleImage data) {
        final int w = data.getWidth();
        final int h = data.getHeight();
        final int ws = w/2 + w%2;
        final int hs = h/2 + h%2;
        final GrayScaleImage scaled = new GrayScaleImage(ws, hs);

        for (int j=0 ; j<h ; j += 2) {
            for (int i=0 ; i<w ; i += 2) {
                final int a = getAverage(data, w, h, i, j, i+1,  j,  i, j+1, i+1, j+1);
                scaled.setPixel(i/2, j/2, a);
            }
        }

        return scaled;
    }

    private int getAverage(GrayScaleImage data, int w, int h, int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4) {
        int s = 0;
        float c = 0f;

        if (0 <= x1 && x1 < w && 0 <= y1 && y1 < h) {
            s += data.getPixel(x1, y1);
            c++;
        }

        if (0 <= x2 && x2 < w && 0 <= y2 && y2 < h) {
            s += data.getPixel(x2, y2);
            c++;
        }

        if (0 <= x3 && x3 < w && 0 <= y3 && y3 < h) {
            s += data.getPixel(x3, y3);
            c++;
        }

        if (0 <= x4 && x4 < w && 0 <= y4 && y4 < h) {
            s += data.getPixel(x4, y4);
            c++;
        }

        return Math.round(s/c);
    }

    GrayScaleImage contrast(GrayScaleImage data) {
        final int w = data.getWidth();
        final int h = data.getHeight();
        final GrayScaleImage contrast = new GrayScaleImage(w, h);

        int max = 0;
        int min = 255;
        for (int j=0 ; j<h ; j++) {
            for (int i=0 ; i<w ; i++) {
                final int p = data.getPixel(i, j);

                if (p > max) {
                    max = p;
                }

                if (p < min) {
                    min = p;
                }
            }
        }

        final float d = max - min;
        for (int j=0 ; j<h ; j++) {
            for (int i=0 ; i<w ; i++) {
                final int p = data.getPixel(i, j);
                final int c = Math.round(255*(p - min)/d);
                contrast.setPixel(i, j, c);
            }
        }

        return contrast;
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

    Location max(GrayScaleImage data) {
        final int w = data.getWidth();
        final int h = data.getHeight();
        final Location maxPixel = new Location();

        int max = -1;
        for (int j=0 ; j<h ; j++) {
            for (int i=0 ; i<w ; i++) {
                final int v = data.getPixel(i, j);
                if (v > max) {
                    max = v;
                    maxPixel.set(i, j);
                }
            }
        }

        return maxPixel;
    }

    GrayScaleImage smooth(GrayScaleImage data) {
        final int kernelSize = 43;

        return gaussianFilter(data, kernelSize);
    }

    /**
     * <a href="https://nishatlea.medium.com/finding-out-the-values-of-a-gaussian-kernel-in-image-processing-9caaf213b4ab">Gaussian Kernel Calculation Reference</a>
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

    GrayScaleImage findBlob(GrayScaleImage data, int x0, int y0, int foregroundThreshold) {
        final int w = data.getWidth();
        final int h = data.getHeight();
        final Location pixel = new Location();
        final Labels labels = new Labels(w, h);

        labels.add(x0, y0);
        while (labels.hasPixels()) {
            if (labels.removeAndLabel(pixel)) {
                final int x = pixel.x();
                final int y = pixel.y();
                if (data.getPixel(x, y) >= foregroundThreshold) {
                    processAllNeighbourPixels(data, w, h, x, y, foregroundThreshold, pixel, labels);
                }
            }
        }

        final GrayScaleImage blob = new GrayScaleImage(w, h);
        for (int j=0 ; j<h ; j++) {
            for (int i=0 ; i<w ; i++) {
                if (labels.isLabelled(i, j)) {
                    blob.setPixel(i, j, 127);
                } else {
                    blob.setPixel(i, j, data.getPixel(i, j));
                }
            }
        }

        return blob;
    }

    private void processAllNeighbourPixels(GrayScaleImage data, int w, int h, int x, int y, int foregroundThreshold, Location pixel, Labels labels) {
        // East
        processNeighbourPixel(data, w, h, x, y, 1, 0, foregroundThreshold, pixel, labels);

        // North East
        processNeighbourPixel(data, w, h, x, y, 1, -1, foregroundThreshold, pixel, labels);

        // North
        processNeighbourPixel(data, w, h, x, y, 0, -1, foregroundThreshold, pixel, labels);

        // North West
        processNeighbourPixel(data, w, h, x, y, -1, -1, foregroundThreshold, pixel, labels);

        // West
        processNeighbourPixel(data, w, h, x, y, -1, 0, foregroundThreshold, pixel, labels);

        // South West
        processNeighbourPixel(data, w, h, x, y, -1, 1, foregroundThreshold, pixel, labels);

        // South
        processNeighbourPixel(data, w, h, x, y, 0, 1, foregroundThreshold, pixel, labels);

        // South East
        processNeighbourPixel(data, w, h, x, y, 1, 1, foregroundThreshold, pixel, labels);
    }

    private void processNeighbourPixel(GrayScaleImage data, int w, int h, int xc, int yc, int dx, int dy, int foregroundThreshold, Location pixel, Labels labels) {
        final int x = xc + dx;
        final int y = yc + dy;
        if (labels.isNotLabelled(x, y)) {
            getForegroundPixel(data, w, h, x, y, foregroundThreshold, pixel);
            if (pixel.isNotEmpty()) {
                labels.add(pixel);
            }
        }
    }

    private void getForegroundPixel(GrayScaleImage data, int w, int h, int x, int y, int foregroundThreshold, Location pixel) {
        if (x < 0 || x >= w || y < 0 || y >= h) {
            pixel.clear();
        } else if (data.getPixel(x, y) >= foregroundThreshold) {
            pixel.set(x, y);
        } else {
            pixel.clear();
        }
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
