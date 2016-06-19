package com.test.image.filter;

import com.test.image.filter.sobel.SobelImageFilterFactory;
import com.test.image.model.GrayScaleImage;
import com.test.image.model.GrayScaleImageFactory;

import org.junit.Assert;
import org.junit.Test;

public class SobelFilterTest {
    private final int[][] pixels3x3 = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
    };

    private final int[][] pixels5x5 = {
            { 1,  2,  3,  4,  5},
            { 6,  7,  8,  9, 10},
            {11, 12, 13, 14, 15},
            {16, 17, 18, 19, 20},
            {21, 22, 23, 24, 25}
    };

    @Test
    public void shouldApplyHorizontal3x3Filter() {
        final ImageFilter underTest = SobelImageFilterFactory.getHorizontal3x3Instance();
        testFilterApplication(pixels3x3, underTest, 8);
    }

    @Test
    public void shouldApplyVertical3x3Filter() {
        final ImageFilter underTest = SobelImageFilterFactory.getVertical3x3Instance();
        testFilterApplication(pixels3x3, underTest, 24);
    }

    @Test
    public void shouldApplyHorizontal5x5Filter() {
        final ImageFilter underTest = SobelImageFilterFactory.getHorizontal5x5Instance();
        testFilterApplication(pixels5x5, underTest, 128);
    }

    @Test
    public void shouldApplyVertical5x5Filter() {
        final ImageFilter underTest = SobelImageFilterFactory.getVertical5x5Instance();
        testFilterApplication(pixels5x5, underTest, 640);
    }

    private void testFilterApplication(int [][] pixels, ImageFilter underTest, int centralPixelExpectedValue) {
        final GrayScaleImage input = GrayScaleImageFactory.instance(pixels);
        final FilterImageDataProcessor imageProcessor = new FilterImageDataProcessor(underTest);

        final GrayScaleImage output = imageProcessor.processImage(input);
        Assert.assertNotNull(output);
        Assert.assertEquals(input.getWidth(), output.getWidth());
        Assert.assertEquals(input.getHeight(), output.getHeight());

        final int x = (output.getWidth() - 1)/2;
        final int y = (output.getHeight() - 1)/2;
        Assert.assertEquals(centralPixelExpectedValue, output.getPixel(x, y));
    }
}
