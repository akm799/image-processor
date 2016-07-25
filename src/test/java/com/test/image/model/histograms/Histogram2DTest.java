package com.test.image.model.histograms;

import org.junit.Assert;
import org.junit.Test;

public class Histogram2DTest {

    @Test
    public void shouldAddEntries() {
        final Histogram2D underTest = Histogram2DFactory.instance(0, 10, 10, 0, 5, 5);
        underTest.addEntry(0, 2.7, 1);
        underTest.addEntry(5, 1.6, 2);
        underTest.addEntry(9, 4.4, 3);

        final Bin2D[] bins = underTest.getBins();
        Assert.assertNotNull(bins);
        Assert.assertEquals(3, bins.length);

        assertBin(5.5, 1.5, 2, bins[0]);
        assertBin(0.5, 2.5, 1, bins[1]);
        assertBin(9.5, 4.5, 3, bins[2]);
    }

    @Test
    public void shouldAddEntriesInBinBoundary() {
        final Histogram2D underTest = Histogram2DFactory.instance(0, 10, 10, 0, 5, 5);
        underTest.addEntry(5, 2, 1);

        final Bin2D[] bins = underTest.getBins();
        Assert.assertNotNull(bins);
        Assert.assertEquals(1, bins.length);

        assertBin(5.5, 2.5, 1, bins[0]);
    }

    private void assertBin(double x, double y, int value, Bin2D bin) {
        assertBin(x, y, new int[]{value}, bin);
    }

    @Test
    public void shouldFilterEntries() {
        final Histogram2D underTest = Histogram2DFactory.instance(0, 10, 10, 0, 5, 5);
        underTest.addEntry(0, 2.7, 1);
        underTest.addEntry(5, 1.6, 2);
        underTest.addEntry(9, 4.4, 3);
        underTest.addEntry(5, 1.6, 4);
        underTest.addEntry(9, 4.4, 5);
        underTest.addEntry(5, 1.6, 6);
        underTest.addEntry(9, 4.4, 7);
        underTest.addEntry(5, 1.6, 8);

        underTest.filter(3);
        final Bin2D[] bins = underTest.getBins();
        Assert.assertNotNull(bins);
        Assert.assertEquals(2, bins.length);

        assertBin(5.5, 1.5, new int[]{2, 4, 6, 8}, bins[0]);
        assertBin(9.5, 4.5, new int[]{3, 5, 7},    bins[1]);
    }

    private void assertBin(double x, double y, int[] values, Bin2D bin) {
        Assert.assertNotNull(bin);
        Assert.assertEquals(x, bin.x(), 0);
        Assert.assertEquals(y, bin.y(), 0);
        Assert.assertNotNull(bin.entries());
        Assert.assertEquals(values.length, bin.entries().size());
        for (int i=0 ; i<values.length ; i++) {
            Assert.assertEquals(values[i], bin.entries().get(i));
        }
    }
}
