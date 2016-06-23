package com.test.image.processors.edge;

import com.test.image.ImageDataProcessor;
import com.test.image.model.Constants;
import com.test.image.model.GrayScaleImage;
import com.test.image.model.GrayScaleImageFactory;
import com.test.image.model.GrayScaleImageTestHelper;
import org.junit.Test;

public class HysterisisTest {
    private final int s = Constants.MAX_INTENSITY;
    private final int w = Constants.WEAK_PIXEL_INTENSITY;
    private final int k = Constants.MAX_INTENSITY;

    private ImageDataProcessor underTest = new HysterisisProcessor();

    @Test
    public void shouldPerformHysterisisAnalysis() {
        final int[][] pixels = {
                {w, w, 0, w, w},
                {0, s, w, 0, 0},
                {0, 0, w, w, s},
                {0, 0, w, 0, 0},
                {s, 0, 0, 0, 0}
        };

        final int[][] expected = {
                {k, k, 0, 0, 0},
                {0, k, k, 0, 0},
                {0, 0, k, k, k},
                {0, 0, 0, 0, 0},
                {k, 0, 0, 0, 0}
        };

        testHysterisis(pixels, expected);
    }

    private void testHysterisis(int[][] pixels, int[][] expected) {
        final GrayScaleImage input = GrayScaleImageFactory.instance(pixels);
        final GrayScaleImage output = underTest.processImage(input);
        GrayScaleImageTestHelper.assertPixels(expected, output);
    }
}
