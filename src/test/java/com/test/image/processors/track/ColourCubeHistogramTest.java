package com.test.image.processors.track;

import com.test.image.model.collections.IntCollection;
import com.test.image.processors.track.impl.MutableColourCubeHistogramImpl;
import com.test.image.util.ColourHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Thanos Mavroidis on 07/04/2019.
 */
public class ColourCubeHistogramTest {
    private final int nDivsInSide = 51;
    private final int nDivsInSideSq = nDivsInSide*nDivsInSide;
    private final int nBins = nDivsInSideSq*nDivsInSide;

    private final int width = 100;
    private final int height = 100;

    private MutableColourCubeHistogram underTest;

    @Before
    public void setUp() {
        underTest = new MutableColourCubeHistogramImpl(width, height, nDivsInSide);
    }

    @Test
    public void shouldReturnBinSize() {
        Assert.assertEquals(nBins, underTest.nBins());
    }

    @Test
    public void shouldCountColours() {
        final int binIndex1 = 0;
        add(0, 0, 0, 0, underTest);
        add(2, 3, 2, 1, underTest);
        Assert.assertEquals(2, underTest.binSize(binIndex1));
        assertBin(underTest.binPoints(binIndex1), 0, 1);

        final int binIndex2 = nDivsInSide - 1;
        add(255, 0, 0, 2, underTest);
        add(253, 2, 3, 3, underTest);
        Assert.assertEquals(2, underTest.binSize(binIndex2));
        assertBin(underTest.binPoints(binIndex2), 2, 3);

        final int binIndex3 = nDivsInSideSq - 1;
        add(255, 255, 0, 4, underTest);
        add(254, 253, 2, 5, underTest);
        Assert.assertEquals(2, underTest.binSize(binIndex3));
        assertBin(underTest.binPoints(binIndex3), 4, 5);

        final int binIndex4 = nBins - 1;
        add(255, 255, 255, 6, underTest);
        add(252, 254, 253, 7, underTest);
        Assert.assertEquals(2, underTest.binSize(binIndex4));
        assertBin(underTest.binPoints(binIndex4), 6, 7);

        final int binIndex5 = 25;
        add(129, 0, 0, 8, underTest);
        Assert.assertEquals(1, underTest.binSize(binIndex5));
        assertBin(underTest.binPoints(binIndex5), 8);

        final int binIndex6 = 25*(nDivsInSide + 1);
        add(129, 129, 0, 9, underTest);
        Assert.assertEquals(1, underTest.binSize(binIndex6));
        assertBin(underTest.binPoints(binIndex6), 9);

        final int binIndex7 = 25*(nDivsInSide + nDivsInSideSq + 1);
        add(129, 129, 129, 10, underTest);
        add(127, 129, 128, 11, underTest);
        Assert.assertEquals(2, underTest.binSize(binIndex7));
        assertBin(underTest.binPoints(binIndex7), 10, 11);

        // Check that all points we added are counted.
        Assert.assertEquals(12, underTest.nPoints());

        // Check that all other bins are empty.
        final Set<Integer> populatedBinIndexes = new HashSet(Arrays.asList(binIndex1, binIndex2, binIndex3, binIndex4, binIndex5, binIndex6, binIndex7));
        for (int i= 0 ; i<nBins ; i++) {
            if (!populatedBinIndexes.contains(i)) {
                Assert.assertEquals(0, underTest.binSize(i));
                Assert.assertNotNull(underTest.binPoints(i));
                Assert.assertTrue(underTest.binPoints(i).isEmpty());
            }
        }
    }

    private void add(int r, int g, int b, int pixelIndex, MutableColourCubeHistogram underTest) {
        underTest.add(pixelIndex, ColourHelper.getRgb(r, g, b));
    }

    private void assertBin(IntCollection actual, int...expected) {
        Assert.assertEquals(expected.length, actual.size());
        for (int i : expected) {
            Assert.assertTrue(actual.contains(i));
        }
    }
}
