package com.test.image.processors.sharp;

import com.test.image.processors.combination.AbstractCombinationImageDataProcessor;

/**
 * Created by Thanos Mavroidis on 06/03/2019.
 */
final class SubtractImageDataProcessor extends AbstractCombinationImageDataProcessor {

    SubtractImageDataProcessor() {}

    @Override
    protected final int combineValues(int v1, int v2) {
        return v1 - v2;
    }
}
