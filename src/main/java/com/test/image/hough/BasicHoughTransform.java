package com.test.image.hough;

import com.test.image.model.GrayScaleImage;
import com.test.image.model.histograms.Histogram2D;
import com.test.image.model.histograms.Histogram2DFactory;

public class BasicHoughTransform implements HoughTransform {
    private final int nThetaBins = 90;
    private final double thetaMin = 0;
    private final double thetaMax = Math.PI;
    private final double[] theta = new double[nThetaBins];
    private final double[] sines = new double[nThetaBins];
    private final double[] cosines = new double[nThetaBins];
    private final double radiusMin = 0;
    private final int nRadiusBinsFactor = 1000;

    private final int threshold;

    private double radiusMax;
    private int nRadiusBins;

    public BasicHoughTransform(int threshold) {
        this.threshold = threshold;
        init();
    }

    private void init() {
        final double bw = (thetaMax - thetaMin)/nThetaBins;
        final double bw2 = bw/2;
        for (int i=0 ; i<nThetaBins ; i++) {
            final double t = thetaMin + i*bw + bw2;
            theta[i] = t;
            sines[i] = Math.sin(t);
            cosines[i] = Math.cos(t);
        }
    }

    @Override
    public Histogram2D transform(GrayScaleImage image) {
        radiusMax = computeMaxRadius(image);
        nRadiusBins = nRadiusBinsFactor*(int)radiusMax;
        final Histogram2D histogram = Histogram2DFactory.instance(thetaMin, thetaMax, nThetaBins, radiusMin, radiusMax, nRadiusBins);
        fillHistogram(image, histogram);
        histogram.filter(threshold);

        return histogram;
    }

    private double computeMaxRadius(GrayScaleImage image) {
        final int w = image.getWidth();
        final int h = image.getHeight();

        return Math.ceil(Math.sqrt(w*w + h*h));
    }

    private void fillHistogram(GrayScaleImage image, Histogram2D histogram) {
        final int w = image.getWidth();
        final int h = image.getHeight();
        final double hMax = h - 0.5;

        double x, y;
        for (int j=0 ; j<h ; j++) {
            y = hMax - j;
            for (int i=0 ; i<w ; i++) {
                x = i + 0.5;
                if (image.getPixel(i, j) > 0) {
                    for (int k=0 ; k<nThetaBins ; k++) {
                        final double radius = x*cosines[k] + y*sines[k];
                        if (radius > 0) {
                            histogram.addEntry(k, radius, image.getPixelIndex(i, j));
                        }
                    }
                }
            }
        }
    }
}
