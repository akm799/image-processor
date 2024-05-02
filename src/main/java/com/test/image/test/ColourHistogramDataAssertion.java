package com.test.image.test;

@FunctionalInterface
public interface ColourHistogramDataAssertion {
    void assertData(int[][][] actualData);
}
