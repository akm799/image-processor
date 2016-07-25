package com.test.image.hough;

import com.test.image.model.Constants;
import com.test.image.model.GrayScaleImage;
import com.test.image.model.collections.IntList;
import com.test.image.model.histograms.Bin2D;
import com.test.image.model.histograms.Histogram2D;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class HoughTransformTest {
    private final int width = 10;
    private final int height = 10;
    private GrayScaleImage image = new GrayScaleImage(width, height);

    private HoughTransform underTest;

    @Test
    public void shouldFindNegativeObliqueLine() {
        setUpNegativeObliqueLine();
        underTest = new BasicHoughTransform(10);

        final Histogram2D histogram = underTest.transform(image);
        Assert.assertNotNull(histogram);
        final Bin2D[] bins = histogram.getBins();
        Assert.assertNotNull(bins);
        Assert.assertEquals(1, bins.length);

        final double x = Math.PI/4;
        final double y = Math.sqrt(50);
        final int size = 10;
        Assert.assertEquals(x, bins[0].x(), 0.0);
        Assert.assertEquals(y, bins[0].y(), 0.001);

        final IntList entries = bins[0].entries();
        Assert.assertNotNull(entries);
        Assert.assertEquals(size, entries.size());

        final int[] indexes = {0, 11, 22, 33, 44, 55, 66, 77, 88, 99};
        for (int i=0 ; i<size ; i++) {
            Assert.assertEquals(indexes[i], entries.get(i));
        }
    }

    private void setUpNegativeObliqueLine() {
        for (int j=0 ; j<height ; j++) {
            for (int i=0 ; i<width ; i++) {
                if (i == j) {
                    image.setPixel(i, j, Constants.MAX_INTENSITY);
                }
            }
        }
    }

    @Test
    public void shouldFindPositiveObliqueLine() {
        setUpPositiveObliqueLine();
        underTest = new BasicHoughTransform(2);

        final Histogram2D histogram = underTest.transform(image);
        Assert.assertNotNull(histogram);
        final Bin2D[] bins = histogram.getBins();
        Assert.assertNotNull(bins);
        Assert.assertEquals(1, bins.length);

        final double x = 3*Math.PI/4;
        final double y = 0;
        final int size = 10;
        Assert.assertEquals(x, bins[0].x(), 0.0);
        Assert.assertEquals(y, bins[0].y(), 0.001);

        final IntList entries = bins[0].entries();
        Assert.assertNotNull(entries);
        Assert.assertEquals(size, entries.size());

        final int[] indexes = {9, 18, 27, 36, 45, 54, 63, 72, 81, 90};
        for (int i=0 ; i<size ; i++) {
            Assert.assertEquals(indexes[i], entries.get(i));
        }
    }

    private void setUpPositiveObliqueLine() {
        final int maxJ = height - 1;
        for (int j=0 ; j<height ; j++) {
            for (int i=0 ; i<width ; i++) {
                if (i == j) {
                    image.setPixel(i, maxJ - j, Constants.MAX_INTENSITY);
                }
            }
        }
    }
}
