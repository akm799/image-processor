package com.test.image.processors.track;

import com.test.image.model.collections.IntCollection;


/**
 * Created by Thanos Mavroidis on 07/04/2019.
 */
public interface ColourCubeHistogram {
    int NO_SCORE = -1;

    int imageWidth();

    int imageHeight();

    int divisionsInSide();

    int nPoints();

    int nBins();

    int binSize(int binIndex);

    IntCollection binPoints(int binIndex);
}
