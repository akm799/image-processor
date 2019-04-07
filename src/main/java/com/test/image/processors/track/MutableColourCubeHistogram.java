package com.test.image.processors.track;

/**
 * Created by Thanos Mavroidis on 07/04/2019.
 */
public interface MutableColourCubeHistogram extends ColourCubeHistogram {

    void add(int pixelIndex, int rgb);
}
