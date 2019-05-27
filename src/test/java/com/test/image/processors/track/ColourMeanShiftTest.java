package com.test.image.processors.track;

import com.test.image.processors.track.impl.ColourCubeHistogramComparison;
import com.test.image.processors.track.impl.MutableColourCubeHistogramImpl;
import com.test.image.util.ColourHelper;
import org.junit.Assert;
import org.junit.Test;

import java.awt.*;

/**
 * Created by Thanos Mavroidis on 19/05/2019.
 */
public class ColourMeanShiftTest {
    private final int nDivsInSide = 51;

    private final int width = 10;
    private final int height = width;
    private final int halfWidth = width/2;
    private final int halfHeight = height/2;

    private final int firstQuadrantIndex = 0;
    private final int secondQuadrantIndex = 1;
    private final int thirdQuadrantIndex = 2;
    private final int fourthQuadrantIndex = 3;

    @Test
    public void shouldShiftTowardsMean() {
        final ColourCubeHistogram target = defineTargetWindow(); // Target window is all red.
        final ColourCubeHistogram window = defineWindow(); // Window has a red bottom right quadrant and different colours everywhere else.
        final ColourCubeHistogram comparison = new ColourCubeHistogramComparison(window, target);

        final Point newCentre = ColourMeanShift.shift(window, comparison);
        Assert.assertNotNull(newCentre);
        Assert.assertEquals(7, newCentre.x); // New centre is at the centre of the bottom right quadrant.
        Assert.assertEquals(7, newCentre.y); // New centre is at the centre of the bottom right quadrant.
    }

    private ColourCubeHistogram defineTargetWindow() {
        final MutableColourCubeHistogram window = new MutableColourCubeHistogramImpl(width, height, nDivsInSide);
        for (int i=0 ; i<width*height ; i++) {
            addRed(i, window);
        }

        return window;
    }

    private ColourCubeHistogram defineWindow() {
        final MutableColourCubeHistogram window = new MutableColourCubeHistogramImpl(width, height, nDivsInSide);

        for (int i=0 ; i<width*height ; i++) {
            switch (findQuadrantIndex(i)) {
                case firstQuadrantIndex:
                    addWite(i, window);
                    break;

                case secondQuadrantIndex:
                    addGreen(i, window);
                    break;

                case thirdQuadrantIndex:
                    addBlue(i, window);
                    break;

                case fourthQuadrantIndex:
                    addRed(i, window);
                    break;

                default: throw new IllegalArgumentException("Illegal quadrant index: " + findQuadrantIndex(i));
            }
        }

        return window;
    }

    private int findQuadrantIndex(int pixelIndex) {
        final int qx = (pixelIndex%width)/halfWidth;
        final int qy = (pixelIndex/width)/halfHeight;

        if (qx == 0 && qy == 0) {
            return firstQuadrantIndex;
        } else if (qx == 1 && qy == 0) {
            return secondQuadrantIndex;
        } else if (qx == 0 && qy == 1) {
            return thirdQuadrantIndex;
        } else if (qx == 1 && qy == 1) {
            return fourthQuadrantIndex;
        } else {
            throw new IllegalArgumentException("Illegal pixel index: " + pixelIndex);
        }
    }

    private void addRed(int pixelIndex, MutableColourCubeHistogram histogram) {
        add(255, 0, 0, pixelIndex, histogram);
    }

    private void addGreen(int pixelIndex, MutableColourCubeHistogram histogram) {
        add(0, 255, 0, pixelIndex, histogram);
    }

    private void addBlue(int pixelIndex, MutableColourCubeHistogram histogram) {
        add(0, 0, 255, pixelIndex, histogram);
    }

    private void addWite(int pixelIndex, MutableColourCubeHistogram histogram) {
        add(255, 255, 255, pixelIndex, histogram);
    }

    private void add(int r, int g, int b, int pixelIndex, MutableColourCubeHistogram histogram) {
        histogram.add(pixelIndex, ColourHelper.getRgb(r, g, b));
    }
}
