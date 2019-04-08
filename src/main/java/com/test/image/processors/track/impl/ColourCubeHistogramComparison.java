package com.test.image.processors.track.impl;

import com.test.image.model.collections.IntCollection;
import com.test.image.processors.track.ColourCubeHistogram;

/**
 * Histogram representing the comparison of two histograms with the same dimensions.
 * Each bin size is the absolute difference between the sizes of corresponding bins
 * in the two histograms being compared.
 *
 * Created by Thanos Mavroidis on 07/04/2019.
 */
public final class ColourCubeHistogramComparison implements ColourCubeHistogram {
    private final int width;
    private final int height;
    private final int nSideDivs;

    private final int[] scores;

    public ColourCubeHistogramComparison(ColourCubeHistogram histogram, ColourCubeHistogram target) {
        checkCompatibility(histogram, target);

        width = histogram.imageWidth();
        height = histogram.imageHeight();
        nSideDivs = histogram.divisionsInSide();

        scores = buildScores(histogram, target);
    }

    private void checkCompatibility(ColourCubeHistogram histogram, ColourCubeHistogram target) {
        if (histogram == null) {
            throw new NullPointerException("No null histogram argument allowed.");
        }

        if (target == null) {
            throw new NullPointerException("No null target histogram argument allowed.");
        }

        if (histogram.imageWidth() != target.imageWidth()) {
            throw new IllegalArgumentException("Histogram image width argument, " + histogram.imageWidth() + ", is not equal to the target histogram image width argument, " + target.imageWidth() + ".");
        }

        if (histogram.imageHeight() != target.imageHeight()) {
            throw new IllegalArgumentException("Histogram image height argument, " + histogram.imageHeight() + ", is not equal to the target histogram image height argument, " + target.imageHeight() + ".");
        }

        if (histogram.divisionsInSide() != target.divisionsInSide()) {
            throw new IllegalArgumentException("Histogram number of divisions in cube side argument, " + histogram.divisionsInSide() + ", is not equal to the target histogram number of divisions in cube side argument, " + target.divisionsInSide() + ".");
        }
    }

    private int[] buildScores(ColourCubeHistogram histogram, ColourCubeHistogram target) {
        final int side = histogram.divisionsInSide();
        final int[] scores = new int[side*side*side];
        determineScores(histogram, target, scores);

        return scores;
    }

    private void determineScores(ColourCubeHistogram histogram, ColourCubeHistogram target, int[] scores) {
        for (int i=0 ; i<scores.length ; i++) {
            final int histogramScore = histogram.binSize(i);
            final int targetScore = target.binSize(i);
            if (histogramScore == 0 && targetScore == 0) {
                scores[i] = NO_SCORE; // If both bins being compared are empty, then no comparison is possible.
            } else {
                scores[i] = Math.abs(targetScore - histogramScore);
            }
        }
    }

    @Override
    public int imageWidth() {
        return width;
    }

    @Override
    public int imageHeight() {
        return height;
    }

    @Override
    public int divisionsInSide() {
        return nSideDivs;
    }

    /**
     * Returns the sum of all histogram bin sizes which do not have a
     * value of #ColourCubeHistogram.NO_SCORE.
     *
     * @return the sum of all histogram bin sizes which do not have a
     * value of #ColourCubeHistogram.NO_SCORE
     */
    @Override
    public int nPoints() {
        int totalScore = 0;
        for (int score : scores) {
            if (score != NO_SCORE) {
                totalScore += score;
            }
        }

        return totalScore;
    }

    /**
     * Returns the difference between the sizes of the corresponding bins
     * in the two histograms being compared or #ColourCubeHistogram.NO_SCORE
     * if both bins are empty.
     *
     * @param binIndex the index of the corresponding bins in the two
     *                 histograms being compared
     * @return the difference between the sizes of the corresponding bins
     * in the two histograms being compared or #ColourCubeHistogram.NO_SCORE
     * if both bins are empty
     */
    @Override
    public int binSize(int binIndex) {
        return scores[binIndex];
    }

    /**
     * Returns an empty IntCollection since a bin in this comparison
     * histogram does not contain any points.
     */
    @Override
    public IntCollection binPoints(int binIndex) {
        return IntCollection.EMPTY;
    }
}
