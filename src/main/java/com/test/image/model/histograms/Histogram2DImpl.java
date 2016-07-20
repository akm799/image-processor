package com.test.image.model.histograms;

import com.test.image.model.collections.IntArrayList;
import com.test.image.model.collections.IntList;


public final class Histogram2DImpl implements Histogram2D {
    private static final int INITIAL_CAPACITY = 128;
    private static final int CAPACITY_INCREMENT = 512;

    private final int nXBins;
    private final int nYBins;

    private final double dx;
    private final double dy;
    private final IntList[][] entries;

    Histogram2DImpl(double xMin, double xMax, int nXBins, double yMin, double yMax, int nYBins) {
        checkArguments(xMin, xMax, nXBins, yMin, yMax, nYBins);
        this.nXBins = nXBins;
        this.nYBins = nYBins;

        this.dx = (xMax - xMin)/nXBins;
        this.dy = (yMax - yMin)/nYBins;
        this.entries = new IntList[nYBins][nXBins];
    }

    private void checkArguments(double xMin, double xMax, int nXBins, double yMin, double yMax, int nYBins) {
        checkArguments(xMin, xMax, nXBins, "X");
        checkArguments(yMin, yMax, nYBins, "Y");
    }

    private void checkArguments(double min, double max, int nBins, String rangeName) {
        if (max <= min) {
            throw new IllegalArgumentException(rangeName + "-range error: maximum value (" + max + ") cannot be less than or equal to the minimum value (" + min + ").");
        }

        if (nBins <= 1) {
            throw new IllegalArgumentException(rangeName + "-range error: number of bins (" + nBins + ") cannot be less than or equal to 1.");
        }
    }

    @Override
    public void addEntry(double x, double y, int value) {
        final int ix = (int)Math.floor(x/dx);
        final int iy = (int)Math.floor(y/dy);
        if (entries[iy][ix] == null) {
            entries[iy][ix] = new IntArrayList(INITIAL_CAPACITY, CAPACITY_INCREMENT);
        }

        entries[iy][ix].add(value);
    }

    @Override
    public void filter(int minEntries) {
        for (int iy=0 ; iy<nYBins ; iy++) {
            for (int ix=0 ; ix<nXBins ; ix++) {
                if (entries[iy][ix] != null && entries[iy][ix].size() < minEntries) {
                    entries[iy][ix] = null;
                }
            }
        }
    }

    @Override
    public Bin2D[] getBins() {
        final int nBins = countBins();
        final Bin2D[] bins = new Bin2D[nBins];
        fillBins(bins);

        return bins;
    }

    private int countBins() {
        int nCount = 0;
        for (int iy=0 ; iy<nYBins ; iy++) {
            for (int ix=0 ; ix<nXBins ; ix++) {
                if (entries[iy][ix] != null) {
                    nCount++;
                }
            }
        }

        return nCount;
    }

    private void fillBins(Bin2D[] bins) {
        final double dxHalf = dx/2;
        final double dyHalf = dy/2;

        int i = 0;
        for (int iy=0 ; iy<nYBins ; iy++) {
            for (int ix=0 ; ix<nXBins ; ix++) {
                if (entries[iy][ix] != null) {
                    final double x = ix*dx + dxHalf;
                    final double y = iy*dy + dyHalf;
                    bins[i++] = new Bin2DImpl(x, y, entries[iy][ix]);
                }
            }
        }
    }

    private static final class Bin2DImpl implements Bin2D {
        private final double x;
        private final double y;
        private final IntList entries;

        private Bin2DImpl(double x, double y, IntList entries) {
            this.x = x;
            this.y = y;
            this.entries = entries;
        }

        @Override
        public double x() {
            return x;
        }

        @Override
        public double y() {
            return y;
        }

        @Override
        public IntList entries() {
            return entries;
        }
    }
}
