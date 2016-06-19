package com.test.image.processors.edge;

import com.test.image.model.GrayScaleImage;
import com.test.image.model.Histogram;
import com.test.image.model.impl.ImageHistogram;

/**
 * Implementation of Otsu's image thresholding method.
 *
 * https://en.wikipedia.org/wiki/Otsu%27s_method
 */
public final class OtsuThresholdFinder implements ThresholdFinder {

    OtsuThresholdFinder() {}

    public int findThreshold(GrayScaleImage image) {
        final Histogram histogram = new ImageHistogram(image);
        final int[] values = histogram.getValues();

        double max = 0;
        int thresholdIndex = 0;
        for (int t=0 ; t<values.length ; t++) {
            final double sigmaSquareB = sigmaSquareB(t, histogram);
            if (sigmaSquareB > max) {
                max = sigmaSquareB;
                thresholdIndex = t;
            }
        }

        return values[thresholdIndex];
    }

    private double sigmaSquareB(int t, Histogram histogram) {
        final double omegaZero = omegaZero(t, histogram);
        final double omegaOne = omegaOne(t, histogram);
        final double meuZero = meuZero(t, omegaZero, histogram);
        final double meuOne = meuOne(t, omegaOne, histogram);

        return omegaZero*omegaOne*Math.pow((meuZero - meuOne), 2);
    }

    private double omegaZero(int t, Histogram histogram) {
        return omega(0, t, histogram);
    }

    private double omegaOne(int t, Histogram histogram) {
        return omega(t, histogram.getSize(), histogram);
    }

    private double omega(int start, int end, Histogram histogram) {
        final float[] p = histogram.getProbabilities();

        float sum = 0;
        for (int i=start ; i<end ; i++) {
            sum += p[i];
        }

        return sum;
    }

    private double meuZero(int t, double omegaZero, Histogram histogram) {
        return meu(0, t, omegaZero, histogram);
    }

    private double meuOne(int t, double omegaOne, Histogram histogram) {
        return meu(t, histogram.getSize(), omegaOne, histogram);
    }

    private double meu(int start, int end, double omega, Histogram histogram) {
        final int[] v = histogram.getValues();
        final float[] p = histogram.getProbabilities();

        float sum = 0;
        for (int i=start ; i<end ; i++) {
            sum += v[i]*p[i];
        }

        return sum/omega;
    }
}
