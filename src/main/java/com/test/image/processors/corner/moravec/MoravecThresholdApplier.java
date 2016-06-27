package com.test.image.processors.corner.moravec;

import com.test.image.ImageDataProcessor;
import com.test.image.model.GrayScaleImage;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public final class MoravecThresholdApplier implements ImageDataProcessor {
    private final CornerConfig config = new CornerConfig();

    @Override
    public GrayScaleImage processImage(GrayScaleImage image) {
        final GrayScaleImage output = new GrayScaleImage(image.getWidth(), image.getHeight());
        final Map<Integer, Counter> frequencies = findNonZeroFrequencies(image);
        final int threshold = findThreshold(frequencies);
        applyThreshold(image, threshold, output);

        return output;
    }

    private Map<Integer, Counter> findNonZeroFrequencies(GrayScaleImage image) {
        final Map<Integer, Counter> frequencies = new TreeMap<>(Collections.reverseOrder());
        for (int j=0 ; j<image.getHeight() ; j++) {
            for (int i=0 ; i<image.getWidth() ; i++) {
                final int value = image.getPixel(i, j);
                if (value > 1) { // 1 is the minimum possible result. Discount it.
                    if (frequencies.containsKey(value)) {
                        frequencies.get(value).increase();
                    } else {
                        frequencies.put(value, new Counter());
                    }
                }
            }
        }

        return frequencies;
    }

    // Goes down the frequency list (starting from out maximum value) and looks for a sudden frequency increase.
    // When this sudden increase is found, it is assumed this is an edge, rather than a corner, so the previous value
    // is returned.
    private int findThreshold(Map<Integer, Counter> frequencies) {
        int previousValue = 0;
        int previousFrequency = 0;
        for (Integer value : frequencies.keySet()) {
            final int frequency = frequencies.get(value).getCount();
            if (previousFrequency > 0) {
                final float ratio = frequency/previousFrequency;
                if (ratio > config.suddenIncreaseRatio) {
                    return previousValue;
                }
            }

            previousValue = value;
            previousFrequency = frequency;
        }

        return previousValue;
    }

    private void applyThreshold(GrayScaleImage input, int threshold, GrayScaleImage output) {
        for (int j=0 ; j<input.getHeight() ; j++) {
            for (int i=0 ; i<input.getWidth() ; i++) {
                final int value = input.getPixel(i, j);
                if (value >= threshold) {
                    output.setPixel(i, j, value);
                }
            }
        }
    }

    private static final class Counter {
        private int count;

        Counter() {
            count = 1;
        }

        void increase() {
            count++;
        }

        int getCount() {
            return count;
        }
    }
}
