package com.test.image.processors.track;

import com.test.image.model.collections.IntIterator;

import java.awt.*;

/**
 * Created by Thanos Mavroidis on 19/05/2019.
 */
public class ColourMeanShift {
    private static final float INFINITY_WEIGHT = 3f;

    public static Point shift(ColourCubeHistogram window, ColourCubeDifference comparison) {
        float sumX = 0;
        float sumY = 0;
        float sumWeight = 0;

        final int width = window.imageWidth();
        for (int i=0 ; i<window.nBins() ; i++) {
            if (comparison.hasBinDiff(i)) {
                final float diff = comparison.binDiff(i);
                final float weight = (diff == 0 ? INFINITY_WEIGHT : 1f/diff)*comparison.refBinWeight(i);
                final IntIterator points = window.binPoints(i).iterator();
                while (points.hasNext()) {
                    final int pixelIndex = points.next();
                    sumX += weight*(pixelIndex%width);
                    sumY += weight*(pixelIndex/width);
                    sumWeight += weight;
                }
            }
        }

        final int shiftedX = Math.round(sumX/sumWeight);
        final int shiftedY = Math.round(sumY/sumWeight);

        return new Point(shiftedX, shiftedY);
    }

    private ColourMeanShift() {}
}
