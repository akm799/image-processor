package com.test.image.model.impl;

import com.test.image.model.GrayScaleImage;
import com.test.image.model.Histogram;

import java.util.*;

public final class ImageHistogram implements Histogram {
    private final int[] values;
    private final float[] probabilities;
    private final Map<Integer, Integer> indexes = new HashMap<>();

    public ImageHistogram(GrayScaleImage image) {
        final Set<Integer> intensities = findIntensities(image);
        values = computeValues(intensities);

        final int[] frequencies = computeFrequencies(image, values);
        probabilities = computeProbabilities(frequencies);
    }

    private Set<Integer> findIntensities(GrayScaleImage image) {
        final Set<Integer> intensities  = new TreeSet<>();
        for (int j=0 ; j<image.getHeight() ; j++) {
            for (int i=0 ; i<image.getWidth() ; i++) {
                final int value = image.getPixel(i, j);
                if (value > 0 && !intensities.contains(value)) {
                    intensities.add(value);
                }
            }
        }

        return intensities;
    }

    private int[] computeValues(Set<Integer> intensities) {
        final int[] values = new int[intensities.size()];

        int i = 0;
        for (Integer value : intensities) {
            indexes.put(value, i);
            values[i++] = value;
        }

        return values;
    }

    private int[] computeFrequencies(GrayScaleImage image, int[] values) {
        final int[] frequencies = new int[values.length];
        Arrays.fill(frequencies, 0);

        for (int j=0 ; j<image.getHeight() ; j++) {
            for (int i=0 ; i<image.getWidth() ; i++) {
                final int value = image.getPixel(i, j);
                if (value > 0) {
                    frequencies[indexes.get(value)]++;
                }
            }
        }

        return frequencies;
    }

    private float[] computeProbabilities(int[] frequencies) {
        final float total = computeTotal(frequencies);
        final float[] probabilities = new float[values.length];

        int i = 0;
        for (int frequency : frequencies) {
            probabilities[i++] = frequency/total;
        }

        return probabilities;
    }

    private float computeTotal(int[] frequencies) {
        int size = 0;
        for (int frequency : frequencies) {
            size += frequency;
        }

        return size;
    }

    public int getSize() {
        return values.length;
    }

    public int[] getValues() {
        return values;
    }

    public float[] getProbabilities() {
        return probabilities;
    }
}
