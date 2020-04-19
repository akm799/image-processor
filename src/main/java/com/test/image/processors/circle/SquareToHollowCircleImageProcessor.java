package com.test.image.processors.circle;

import com.test.image.processors.chain.ChainImageProcessor;

import java.util.Arrays;

public final class SquareToHollowCircleImageProcessor extends ChainImageProcessor  {

    public SquareToHollowCircleImageProcessor(double centreRadiusFraction) {
        super(Arrays.asList(new SquareToCircleImageProcessor(), new RingImageProcessor(centreRadiusFraction)));
    }

    @Override
    public String getDescription() {
        return "Transformed the square image into circular one with an empty ring in the middle.";
    }
}
