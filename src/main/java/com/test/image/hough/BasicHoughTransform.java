package com.test.image.hough;

import com.test.image.model.GrayScaleImage;
import com.test.image.model.histograms.Histogram2D;
import com.test.image.model.histograms.Histogram2DFactory;

public class BasicHoughTransform implements HoughTransform {
    private int nThetaBins;
    private double thetaMin;
    private double thetaMax;
    private double[] sines;
    private double[] cosines;

    private double radiusMin;
    private double radiusMax;
    private int nRadiusBins;

    private int threshold;

    @Override
    public Histogram2D transform(GrayScaleImage image) {
        configureLimits(image);

        final Histogram2D histogram = Histogram2DFactory.instance(thetaMin, thetaMax, nThetaBins, radiusMin, radiusMax, nRadiusBins);
        fillHistogram(image, histogram);
        histogram.filter(threshold);

        return histogram;
    }

    private void configureLimits(GrayScaleImage image) {
        final HoughTransformConfig config = new HoughTransformConfig(image.getWidth(), image.getHeight());
        configureLimits(config);
    }

    private void configureLimits(HoughTransformConfig config) {
        thetaMin = config.thetaMin;
        thetaMax = config.thetaMax;
        nThetaBins = config.nThetaBins;
        sines = new double[nThetaBins];
        cosines = new double[nThetaBins];

        radiusMin = config.radiusMin;
        radiusMax = config.radiusMax;
        nRadiusBins = config.nRadiusBins;

        computeTrigValues();
    }

    private void computeTrigValues() {
        final double bw = (thetaMax - thetaMin)/nThetaBins;
        final double bw2 = bw/2;
        for (int i=0 ; i<nThetaBins ; i++) {
            final double t = thetaMin + i*bw + bw2;
            sines[i] = Math.sin(t);
            cosines[i] = Math.cos(t);
        }
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

    @Override
    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }
}
