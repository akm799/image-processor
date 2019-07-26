package com.test.image.processors.track.shift;

import com.test.image.processors.track.shift.impl.MutableColourCubeHistogramImpl;
import com.test.image.util.ColourHelper;
import org.junit.Assert;
import org.junit.Test;

import java.awt.*;

/**
 * Created by Thanos Mavroidis on 26/07/2019.
 */
public class ColourSimilarityTest {
    private final int width = 10;
    private final int height = width;
    private final int nDivsInSide = 51;

    private final int red = ColourHelper.getRgb(255, 0, 0);
    private final int white = ColourHelper.getRgb(255, 255, 255);

    @Test
    public void shouldFindSimilarityInHorizontalShifts() {
        final Rectangle highScoreWindow = new Rectangle();
        highScoreWindow.x = 0;
        highScoreWindow.y = 0;
        highScoreWindow.width = width/2;
        highScoreWindow.height = height;

        final Rectangle lowScoreWindow = new Rectangle();
        lowScoreWindow.x = 0;
        lowScoreWindow.y = 0;
        lowScoreWindow.width = width/4;
        lowScoreWindow.height = height;

        testSimilarities(highScoreWindow, lowScoreWindow);
    }

    @Test
    public void shouldFindSimilarityInDiagonalShifts() {
        final Rectangle highScoreWindow = new Rectangle();
        highScoreWindow.x = 0;
        highScoreWindow.y = 0;
        highScoreWindow.width = width/2;
        highScoreWindow.height = height/2;

        final Rectangle lowScoreWindow = new Rectangle();
        lowScoreWindow.x = 0;
        lowScoreWindow.y = 0;
        lowScoreWindow.width = width/4;
        lowScoreWindow.height = height/4;

        testSimilarities(highScoreWindow, lowScoreWindow);
    }

    @Test
    public void shouldFindSimilarityInVerticalShifts() {
        final Rectangle highScoreWindow = new Rectangle();
        highScoreWindow.x = 0;
        highScoreWindow.y = 0;
        highScoreWindow.width = width;
        highScoreWindow.height = height/2;

        final Rectangle lowScoreWindow = new Rectangle();
        lowScoreWindow.x = 0;
        lowScoreWindow.y = 0;
        lowScoreWindow.width = width;
        lowScoreWindow.height = height/4;

        testSimilarities(highScoreWindow, lowScoreWindow);
    }

    private void testSimilarities(Rectangle highScoreWindow, Rectangle lowScoreWindow) {
        final Rectangle referenceWindow = new Rectangle();
        referenceWindow.x = 0;
        referenceWindow.y = 0;
        referenceWindow.width = width;
        referenceWindow.height = height;
        final ColourCubeHistogram referenceHistogram = defineColourDistribution(white, referenceWindow, red);

        final ColourCubeHistogram highScoreHistogram = defineColourDistribution(white, highScoreWindow, red);

        final ColourCubeHistogram lowScoreHistogram = defineColourDistribution(white, lowScoreWindow, red);

        final float highScore = ColourSimilarity.findSimilarity(referenceHistogram, highScoreHistogram);
        final float lowScore = ColourSimilarity.findSimilarity(referenceHistogram, lowScoreHistogram);

        Assert.assertTrue(highScore > lowScore);
    }

    private ColourCubeHistogram defineColourDistribution(int backgroundColour, Rectangle window, int windowColour) {
        final MutableColourCubeHistogram colourDistribution = new MutableColourCubeHistogramImpl(width, height, nDivsInSide);
        for (int i=0 ; i<width*height ; i++) {
            if (isInsideWindow(i, window)) {
                add(windowColour, i, colourDistribution);
            } else {
                add(backgroundColour, i, colourDistribution);
            }
        }

        return colourDistribution;
    }

    private boolean isInsideWindow(int pixelIndex, Rectangle window) {
        final int x = pixelIndex%width;
        final int y = pixelIndex/width;

        return (x >= window.x && x < window.x + window.width) && (y >= window.y && y < window.y + window.height);
    }

    private void add(int rgb, int pixelIndex, MutableColourCubeHistogram histogram) {
        histogram.add(pixelIndex, rgb);
    }
}
