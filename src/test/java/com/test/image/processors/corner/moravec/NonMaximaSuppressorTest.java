package com.test.image.processors.corner.moravec;

import com.test.image.ImageDataProcessor;
import com.test.image.model.GrayScaleImage;
import com.test.image.model.GrayScaleImageFactory;
import com.test.image.model.GrayScaleImageTestHelper;
import org.junit.Test;

public class NonMaximaSuppressorTest {

    @Test
    public void shouldSuppressNonMaxima() {
        final int[][] pixels = {
                {2, 1, 2, 0, 0},
                {2, 3, 1, 1, 2},
                {1, 1, 0, 1, 3},
                {2, 1, 2, 2, 0},
                {0, 3, 1, 0, 0}
        };

        final int[][] expected = {
                {0, 0, 0, 0, 0},
                {0, 3, 0, 0, 0},
                {0, 0, 0, 0, 3},
                {0, 0, 0, 0, 0},
                {0, 3, 0, 0, 0}
        };

        final GrayScaleImage input = GrayScaleImageFactory.instance(pixels);
        final ImageDataProcessor underTest = new NonMaximaSuppressor();

        final GrayScaleImage actual = underTest.processImage(input);
        GrayScaleImageTestHelper.assertPixels(expected, actual);
    }
}
