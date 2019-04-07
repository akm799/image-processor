package com.test.image.processors.track;

import com.test.image.model.collections.IntCollection;


/**
 * Created by Thanos Mavroidis on 07/04/2019.
 */
public interface ColourCubeHistogram {

    int imageWidth();

    int imageHeight();

    int divisionsInSide();

    int binScore(int binIndex);

    IntCollection binPoints(int binIndex);
}
