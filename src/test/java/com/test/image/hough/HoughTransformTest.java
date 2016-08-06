package com.test.image.hough;

import com.test.image.model.Constants;
import com.test.image.model.GrayScaleImage;
import com.test.image.model.collections.IntList;
import com.test.image.model.histograms.Bin2D;
import com.test.image.model.histograms.Histogram2D;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class HoughTransformTest {
    private final int width = 10;
    private final int height = 10;

    private GrayScaleImage image;
    private HoughTransform underTest;

    @Before
    public void setUp() {
        image = new GrayScaleImage(width, height);
    }

    //TODO Investigate the serious inaccuracies with the negative oblique line.
    @Test
    public void shouldFindNegativeObliqueLine() {
        setUpNegativeObliqueLine();
        underTest = new BasicHoughTransform();
        underTest.setThreshold(5);

        final Histogram2D histogram = underTest.transform(image);
        Assert.assertNotNull(histogram);
        final Bin2D[] bins = histogram.getBins();
        Assert.assertNotNull(bins);

        Assert.assertEquals(10, bins.length);

        final double x = Math.PI/4;
        final double y = Math.sqrt(50);
        final int size = 10;
        final Integer[] indexesArray = {0, 11, 22, 33, 44, 55, 66, 77, 88, 99};
        final List<Integer> indexes = Arrays.asList(indexesArray);
//        System.out.println("\nNegative Oblique");
        for (Bin2D bin : bins) {
//            System.out.println("(" + 180*bin.x()/Math.PI + ", " + bin.y() + ")=" + bin.entries().size());
            Assert.assertEquals(x, bin.x(), 0.1);
            Assert.assertEquals(y, bin.y(), 0.25);

            final IntList entries = bin.entries();
            Assert.assertNotNull(entries);
            Assert.assertTrue(size >= entries.size());

            for (int i=0 ; i<entries.size() ; i++) {
                Assert.assertTrue(indexes.contains(entries.get(i)));
            }
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
        underTest = new BasicHoughTransform();
        underTest.setThreshold(10);

        final Histogram2D histogram = underTest.transform(image);
        Assert.assertNotNull(histogram);
        final Bin2D[] bins = histogram.getBins();
        Assert.assertNotNull(bins);
        Assert.assertEquals(1, bins.length);

        final double x = 3*Math.PI/4;
        final double y = 0;
        final int size = 10;
        Assert.assertEquals(x, bins[0].x(), 0.025);
        Assert.assertEquals(y, bins[0].y(), 0.25);

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

    @Test
    public void shouldFindHorizonalLine() {
        setUpHorizontalLine(5);
        underTest = new BasicHoughTransform();
        underTest.setThreshold(10);

        final Histogram2D histogram = underTest.transform(image);
        Assert.assertNotNull(histogram);
        final Bin2D[] bins = histogram.getBins();
        Assert.assertNotNull(bins);
        Assert.assertEquals(2, bins.length);
        final double x = Math.PI/2;
        final double y = 5.5;
        final int size = 10;
        final int[] indexes = {40, 41, 42, 43, 44, 45, 46, 47, 48, 49};
        for (Bin2D bin : bins) {
            Assert.assertEquals(x, bin.x(), 0.25);
            Assert.assertEquals(y, bin.y(), 0.25);

            final IntList entries = bin.entries();
            Assert.assertNotNull(entries);
            Assert.assertEquals(size, entries.size());

            for (int i=0 ; i<size ; i++) {
                Assert.assertEquals(indexes[i], entries.get(i));
            }
        }
    }

    private void setUpHorizontalLine(int y) {
        final int maxJ = height - 1;
        for (int i=0 ; i<width ; i++) {
            image.setPixel(i, maxJ - y, Constants.MAX_INTENSITY);
        }
    }

    @Test
    public void shouldFindVerticalLine() {
        setUpVerticalLine(5);
        underTest = new BasicHoughTransform();
        underTest.setThreshold(10);

        final Histogram2D histogram = underTest.transform(image);
        Assert.assertNotNull(histogram);
        final Bin2D[] bins = histogram.getBins();
        Assert.assertNotNull(bins);
        Assert.assertEquals(1, bins.length);

        final double x = 0;
        final double y = 5.5;
        final int size = 10;
        Assert.assertEquals(x, bins[0].x(), 0.025);
        Assert.assertEquals(y, bins[0].y(), 0.25);

        final IntList entries = bins[0].entries();
        Assert.assertNotNull(entries);
        Assert.assertEquals(size, entries.size());

        final int[] indexes = {5, 15, 25, 35, 45, 55, 65, 75, 85, 95};
        for (int i=0 ; i<size ; i++) {
            Assert.assertEquals(indexes[i], entries.get(i));
        }
    }

    private void setUpVerticalLine(int x) {
        for (int j=0 ; j<height ; j++) {
            image.setPixel(x, j, Constants.MAX_INTENSITY);
        }
    }
}
