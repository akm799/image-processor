package com.test.image.processors.track;

import com.test.image.processors.track.impl.ColourCubeDifferenceImpl;
import com.test.image.processors.track.impl.MutableColourCubeHistogramImpl;
import com.test.image.util.ColourHelper;
import org.junit.Assert;
import org.junit.Test;


/**
 * Created by Thanos Mavroidis on 12/06/2019.
 */
public class ColourCubeDifferenceTest {
    private final int nDivsInSide = 51;
    private final int nDivsInSideSq = nDivsInSide*nDivsInSide;
    private final int nBins = nDivsInSideSq*nDivsInSide;

    private final int width = 100;
    private final int height = 100;

    @Test
    public void shouldCompareHistograms() {
        final MutableColourCubeHistogram reference = new MutableColourCubeHistogramImpl(width, height, nDivsInSide);
        final MutableColourCubeHistogram histogram = new MutableColourCubeHistogramImpl(width, height, nDivsInSide);
        Assert.assertEquals(histogram.nPoints(), reference.nPoints());

        final int[] referenceBinIndexes = populateReferenceHistogram(reference);
        final int[] histogramBinIndexes = populateHistogram(histogram);
        Assert.assertArrayEquals(referenceBinIndexes, histogramBinIndexes);
        final int[] binIndexes = referenceBinIndexes;

        final ColourCubeDifference underTest = new ColourCubeDifferenceImpl(reference, histogram);

        Assert.assertEquals(histogram.nBins(), reference.nBins());
        Assert.assertEquals(histogram.nBins(), underTest.nBins());
        Assert.assertEquals(reference.nBins(), underTest.nBins());

        Assert.assertEquals(histogram.imageWidth(), underTest.imageWidth());
        Assert.assertEquals(reference.imageWidth(), underTest.imageWidth());
        Assert.assertEquals(histogram.imageHeight(), underTest.imageHeight());
        Assert.assertEquals(reference.imageHeight(), underTest.imageHeight());
        Assert.assertEquals(histogram.divisionsInSide(), underTest.divisionsInSide());
        Assert.assertEquals(reference.divisionsInSide(), underTest.divisionsInSide());

        Assert.assertTrue(underTest.hasBinDiff(binIndexes[0]));
        Assert.assertTrue(underTest.hasBinDiff(binIndexes[1]));
        Assert.assertTrue(underTest.hasBinDiff(binIndexes[2]));
        Assert.assertTrue(underTest.hasBinDiff(binIndexes[3]));
        Assert.assertTrue(underTest.hasBinDiff(binIndexes[4]));
        Assert.assertFalse(underTest.hasBinDiff(binIndexes[5]));
        Assert.assertTrue(underTest.hasBinDiff(binIndexes[6]));

        // Differences
        Assert.assertEquals(0f, underTest.binDiff(binIndexes[0]), 0f);
        Assert.assertEquals(1f, underTest.binDiff(binIndexes[1]), 0f); // Reference 2, Histogram 4, Diff = |4-2|/2 = 1
        Assert.assertEquals(0f, underTest.binDiff(binIndexes[2]), 0f);
        Assert.assertEquals(0f, underTest.binDiff(binIndexes[3]), 0f);
        Assert.assertEquals(0f, underTest.binDiff(binIndexes[4]), 0f);
        Assert.assertEquals(ColourCubeDifference.NO_COMPARISON, underTest.binDiff(binIndexes[5]), 0f); // Reference 0, Histogram 1, No comparison possible because the reference has zero points.
        Assert.assertEquals(1/3f, underTest.binDiff(binIndexes[6]), 0f); // Reference 3, Histogram 2, Diff = |2-3|/3 = 1/3

        // Weights
        final float nPoints = reference.nPoints();
        for (int i=0 ; i<reference.nBins() ; i++) {
            Assert.assertEquals(reference.binSize(i)/nPoints, underTest.refBinWeight(i), 0f);
        }

        // Comparison possible or not possible results
        for (int i=0 ; i<reference.nBins() ; i++) {
            final Boolean populated = (reference.binSize(i) > 0);
            Assert.assertEquals(populated, underTest.hasBinDiff(i));
            if (populated) {
                Assert.assertTrue(underTest.binDiff(i) >= 0f);
            } else {
                Assert.assertEquals(ColourCubeDifference.NO_COMPARISON, underTest.binDiff(i), 0f);
            }
        }
    }

    private int[] populateReferenceHistogram(MutableColourCubeHistogram histogram) {
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

    private int[] populateHistogram(MutableColourCubeHistogram histogram) {
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

    private void add(int r, int g, int b, int pixelIndex, MutableColourCubeHistogram underTest) {
        underTest.add(pixelIndex, ColourHelper.getRgb(r, g, b));
    }
}
