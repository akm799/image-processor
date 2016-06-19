package com.test.image.model;

import com.test.image.model.impl.ImageHistogram;
import org.junit.Assert;
import org.junit.Test;

public class HistogramTest {
    private final int[][] pixels = {
            {5, 1, 3},
            {2, 4, 0},
            {2, 2, 3}
    };
    private final GrayScaleImage input = GrayScaleImageFactory.instance(pixels);

    @Test
    public void shouldBuildHistogram() {
        final int[] expectedValues = {1, 2, 3, 4, 5};
        final float[] expectedProbabilities = {0.125f, 3/8f, 0.25f, 0.125f, 0.125f};

        final Histogram histogram = new ImageHistogram(input);
        Assert.assertEquals(expectedValues.length, histogram.getSize());
        Assert.assertArrayEquals(expectedValues, histogram.getValues());
        Assert.assertArrayEquals(expectedProbabilities, histogram.getProbabilities(), 0f);
    }

    @Test
    public void shouldBuildNormalizedHistogram() {
        final Histogram histogram = new ImageHistogram(input);

        float sum = 0;
        for (float p : histogram.getProbabilities()) {
            sum += p;
        }

        Assert.assertEquals(1f, sum, 0f);
    }
}
