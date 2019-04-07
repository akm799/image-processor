package com.test.image.processors.doc;

import com.test.image.cluster.ColourRange;
import com.test.image.cluster.ColourRangeData;
import com.test.image.model.GrayScaleImage;

/**
 * Created by Thanos Mavroidis on 10/03/2019.
 */
final class CentreColourFinder {
    private final float fraction = 0.15f;

    private final int threshold;

    CentreColourFinder(int threshold) {
        this.threshold = threshold;
    }

    ColourRange findCentreColour(GrayScaleImage image) {
        final int w = image.getWidth();
        final int h = image.getHeight();

        final int cx = w/2;
        final int cy = h/2;
        final int subWidth = Math.round(fraction*w);
        final int subHeight = Math.round(fraction*h);

        final int left = cx - subWidth/2;
        final int top = cy - subHeight/2;
        final int right = cx + subWidth/2;
        final int bottom = cy + subHeight/2;

        return findColour(image, left, top, right, bottom);
    }

    private ColourRange findColour(GrayScaleImage image, int left, int top, int right, int bottom) {
        int sum = 0;
        int count = 0;
        for (int j=top ; j<bottom ; j++) {
            for (int i=left ; i<right ; i++) {
                final int value = image.getPixel(i, j);
                if (value <= threshold) {
                    sum += value;
                    count++;
                }
            }
        }

        final float average = (float)sum/count;

        count = 0;
        float sumDiffSq = 0f;
        for (int j=top ; j<bottom ; j++) {
            for (int i=left ; i<right ; i++) {
                final int value = image.getPixel(i, j);
                if (value <= threshold) {
                    final float d = value - average;
                    sumDiffSq += d*d;
                    count++;
                }
            }
        }

        final double sd = Math.sqrt(sumDiffSq/(count - 1));

        return new ColourRangeData(Math.round(average), sd);
    }
}
