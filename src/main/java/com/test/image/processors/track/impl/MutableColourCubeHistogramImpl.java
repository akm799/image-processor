package com.test.image.processors.track.impl;

import com.test.image.model.collections.IntArrayList;
import com.test.image.model.collections.IntCollection;
import com.test.image.processors.track.MutableColourCubeHistogram;
import com.test.image.util.ColourHelper;

/**
 * Created by Thanos Mavroidis on 07/04/2019.
 */
public final class MutableColourCubeHistogramImpl implements MutableColourCubeHistogram {
    private static final int MAX_COLOUR_VALUE_INT = 255;
    private static final float MAX_COLOUR_VALUE = (float)MAX_COLOUR_VALUE_INT;

    private final int width;
    private final int height;

    private final int nSideDivs;
    private final int nSideDivsSq;

    private final int nBins;
    private final IntCollection[] bins;
    private final float binSide;

    public MutableColourCubeHistogramImpl(int width, int height, int nSideDivs) {
        this.width = width;
        this.height = height;
        this.nSideDivs = nSideDivs;

        this.nSideDivsSq = nSideDivs*nSideDivs;
        this.nBins = nSideDivsSq*nSideDivs;
        this.binSide = MAX_COLOUR_VALUE/nSideDivs;
        this.bins = new IntArrayList[nBins];
    }

    @Override
    public int imageWidth() {
        return width;
    }

    @Override
    public int imageHeight() {
        return height;
    }

    @Override
    public int divisionsInSide() {
        return nSideDivs;
    }

    @Override
    public int binScore(int binIndex) {
        if (bins[binIndex] == null) {
            return 0;
        } else {
            return bins[binIndex].size();
        }
    }

    @Override
    public IntCollection binPoints(int binIndex) {
        if (bins[binIndex] == null) {
            return IntCollection.EMPTY;
        } else {
            return bins[binIndex];
        }
    }

    @Override
    public void add(int pixelIndex, int rgb) {
        final int rIndex = findSideBinIndex(ColourHelper.getRed(rgb));
        final int gIndex = findSideBinIndex(ColourHelper.getGreen(rgb));
        final int bIndex = findSideBinIndex(ColourHelper.getBlue(rgb));

        int binIndex = bIndex*nSideDivsSq + gIndex*nSideDivs + rIndex;
        if (bins[binIndex] == null) {
            bins[binIndex] = new IntArrayList();
        }

        bins[binIndex].add(pixelIndex);
    }

    private int findSideBinIndex(int value) {
        if (value == MAX_COLOUR_VALUE_INT) { // Include the 255 value in the last bin.
            return nSideDivs - 1;
        } else {
            return (int) (value/binSide);

        }
    }
}
