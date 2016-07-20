package com.test.image.model.histograms;

public class Histogram2DFactory {

    public static Histogram2D instance(double xMin, double xMax, int nXBins, double yMin, double yMax, int nYBins) {
        return new Histogram2DImpl(xMin, xMax, nXBins, yMin, yMax, nYBins);
    }

    private Histogram2DFactory() {}
}
