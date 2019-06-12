package com.test.image.processors.track;

import com.test.image.processors.track.impl.ColourCubeHistogramComparison;
import com.test.image.processors.track.impl.MutableColourCubeHistogramImpl;
import com.test.image.util.ColourHelper;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Thanos Mavroidis on 13/04/2019.
 */
@Deprecated
public class ColourCubeHistogramComparisonTest {
    private final int nDivsInSide = 51;
    private final int nDivsInSideSq = nDivsInSide*nDivsInSide;
    private final int nBins = nDivsInSideSq*nDivsInSide;

    private final int width = 100;
    private final int height = 100;

    @Test
    public void shouldCompareHistograms() {
        final MutableColourCubeHistogram firstHistogram = new MutableColourCubeHistogramImpl(width, height, nDivsInSide);
        final MutableColourCubeHistogram secondHistogram = new MutableColourCubeHistogramImpl(width, height, nDivsInSide);
        Assert.assertEquals(firstHistogram.nPoints(), secondHistogram.nPoints());
        final int nPoints = firstHistogram.nPoints();

        final int[] firstHistogramBinIndexes = populateFirstHistogram(firstHistogram);
        final int[] secondHistogramBinIndexes = populateSecondHistogram(secondHistogram);
        Assert.assertArrayEquals(firstHistogramBinIndexes, secondHistogramBinIndexes);
        final int[] binIndexes = firstHistogramBinIndexes;

        final ColourCubeHistogram underTest = new ColourCubeHistogramComparison(firstHistogram, secondHistogram);

        Assert.assertEquals(firstHistogram.nBins(), secondHistogram.nBins());
        Assert.assertEquals(firstHistogram.nBins(), underTest.nBins());
        Assert.assertEquals(secondHistogram.nBins(), underTest.nBins());

        Assert.assertEquals(firstHistogram.imageWidth(), underTest.imageWidth());
        Assert.assertEquals(secondHistogram.imageWidth(), underTest.imageWidth());
        Assert.assertEquals(firstHistogram.imageHeight(), underTest.imageHeight());
        Assert.assertEquals(secondHistogram.imageHeight(), underTest.imageHeight());
        Assert.assertEquals(firstHistogram.divisionsInSide(), underTest.divisionsInSide());
        Assert.assertEquals(secondHistogram.divisionsInSide(), underTest.divisionsInSide());

        Assert.assertEquals(0, underTest.binSize(binIndexes[0]));
        Assert.assertEquals(2, underTest.binSize(binIndexes[1])); // First 4, Second 2, Diff 2
        Assert.assertEquals(0, underTest.binSize(binIndexes[2]));
        Assert.assertEquals(0, underTest.binSize(binIndexes[3]));
        Assert.assertEquals(0, underTest.binSize(binIndexes[4]));
        Assert.assertEquals(ColourCubeHistogram.NO_SCORE, underTest.binSize(binIndexes[5])); // First 1, Second 0, Diff NO_SCORE because the second is 0.
        Assert.assertEquals(1, underTest.binSize(binIndexes[6])); // First 2, Second 3, Diff 1

        final int sumOfAllPopulatedBinsInBothHistograms = 3;
        Assert.assertEquals(sumOfAllPopulatedBinsInBothHistograms, underTest.nPoints());

        final Set<Integer> populatedBinIndexes = findPopulatedIndexesInBothHistograms(firstHistogram, secondHistogram);
        for (int i=0 ; i<nPoints ; i++) {
            if (populatedBinIndexes.contains(i)) {
                Assert.assertFalse(underTest.binSize(i) == ColourCubeHistogram.NO_SCORE);
            } else {
                Assert.assertTrue(underTest.binSize(i) == ColourCubeHistogram.NO_SCORE);
            }
        }
    }

    private int[] populateFirstHistogram(MutableColourCubeHistogram histogram) {
        final int binIndex1 = 0;
        add(0, 0, 0, 0, histogram);
        add(2, 3, 2, 1, histogram);

        final int binIndex2 = nDivsInSide - 1;
        add(255, 0, 0, 2, histogram);
        add(253, 2, 3, 3, histogram);
        add(254, 3, 2, 4, histogram);
        add(253, 3, 1, 5, histogram);

        final int binIndex3 = nDivsInSideSq - 1;
        add(255, 255, 0, 6, histogram);
        add(254, 253, 2, 7, histogram);

        final int binIndex4 = nBins - 1;
        add(255, 255, 255, 8, histogram);
        add(252, 254, 253, 9, histogram);

        final int binIndex5 = 25;
        add(129, 0, 0, 10, histogram);

        final int binIndex6 = 25*(nDivsInSide + 1);
        add(129, 129, 0, 11, histogram);

        final int binIndex7 = 25*(nDivsInSide + nDivsInSideSq + 1);
        add(129, 129, 129, 12, histogram);
        add(127, 129, 128, 13, histogram);

        return new int[] {binIndex1, binIndex2, binIndex3, binIndex4, binIndex5, binIndex6, binIndex7};
    }

    private int[] populateSecondHistogram(MutableColourCubeHistogram histogram) {
        final int binIndex1 = 0;
        add(0, 0, 0, 0, histogram);
        add(2, 3, 2, 1, histogram);

        final int binIndex2 = nDivsInSide - 1;
        add(255, 0, 0, 2, histogram);
        add(253, 2, 3, 3, histogram);

        final int binIndex3 = nDivsInSideSq - 1;
        add(255, 255, 0, 4, histogram);
        add(254, 253, 2, 5, histogram);

        final int binIndex4 = nBins - 1;
        add(255, 255, 255, 6, histogram);
        add(252, 254, 253, 7, histogram);

        final int binIndex5 = 25;
        add(129, 0, 0, 8, histogram);

        // Bin 6 has no values in this (2nd) histogram.
        final int binIndex6 = 25*(nDivsInSide + 1);

        final int binIndex7 = 25*(nDivsInSide + nDivsInSideSq + 1);
        add(129, 129, 129, 9, histogram);
        add(127, 129, 128, 10, histogram);
        add(128, 129, 129, 11, histogram);

        return new int[] {binIndex1, binIndex2, binIndex3, binIndex4, binIndex5, binIndex6, binIndex7};
    }

    private void add(int r, int g, int b, int pixelIndex, MutableColourCubeHistogram underTest) {
        underTest.add(pixelIndex, ColourHelper.getRgb(r, g, b));
    }

    private Set<Integer> findPopulatedIndexesInBothHistograms(ColourCubeHistogram firstHistogram, ColourCubeHistogram secondHistogram) {
        Assert.assertEquals(firstHistogram.nBins(), secondHistogram.nBins());

        final Set<Integer> populatedBinIndexes = new HashSet(firstHistogram.nBins());
        for (int i=0 ; i<firstHistogram.nBins() ; i++) {
            if (firstHistogram.binSize(i) > 0 && secondHistogram.binSize(i) > 0) {
                populatedBinIndexes.add(i);
            }
        }

        return populatedBinIndexes;
    }
}
