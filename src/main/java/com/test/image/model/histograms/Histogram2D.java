package com.test.image.model.histograms;


public interface Histogram2D {

    void addEntry(double x, double y, int value);

    void filter(int minEntries);

    Bin2D[] getBins();
}
