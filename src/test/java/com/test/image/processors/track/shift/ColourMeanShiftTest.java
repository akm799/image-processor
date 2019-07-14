package com.test.image.processors.track.shift;

import com.test.image.processors.track.shift.impl.ColourCubeDifferenceImpl;
import com.test.image.processors.track.shift.impl.MutableColourCubeHistogramImpl;
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

    private final int topLeftQuadrant = 0;
    private final int topRightQuadrantIndex = 1;
    private final int bottomLeftQuadrantIndex = 2;
    private final int bottomRightQuadrantIndex = 3;

    private final int red = ColourHelper.getRgb(255, 0, 0);
    private final int green = ColourHelper.getRgb(0, 255, 0);
    private final int blue = ColourHelper.getRgb(0, 0, 255);
    private final int white = ColourHelper.getRgb(255, 255, 255);

    @Test
    public void shouldShiftLeft() {
        final int initialColour = red;
        final int[] quadrantColours = {initialColour, green, initialColour, blue};
        final int xShift = -3;
        final int yShift = 0;
        testShiftTowardsMean(initialColour, quadrantColours, xShift, yShift);
    }

    @Test
    public void shouldShiftRight() {
        final int initialColour = red;
        final int[] quadrantColours = {green, initialColour, blue, initialColour};
        final int xShift = 2;
        final int yShift = 0;
        testShiftTowardsMean(initialColour, quadrantColours, xShift, yShift);
    }

    @Test
    public void shouldShiftUp() {
        final int initialColour = red;
        final int[] quadrantColours = {initialColour, initialColour, green, blue};
        final int xShift = 0;
        final int yShift = -3;
        testShiftTowardsMean(initialColour, quadrantColours, xShift, yShift);
    }

    @Test
    public void shouldShiftDown() {
        final int initialColour = red;
        final int[] quadrantColours = {green, blue, initialColour, initialColour};
        final int xShift = 0;
        final int yShift = 2;
        testShiftTowardsMean(initialColour, quadrantColours, xShift, yShift);
    }

    @Test
    public void shouldShiftTowardsTopLeftQuadrant() {
        final int initialColour = red;
        final int[] quadrantColours = {initialColour, white, green, blue};
        final int xShift = -3;
        final int yShift = -3;
        testShiftTowardsMean(initialColour, quadrantColours, xShift, yShift);
    }

    @Test
    public void shouldShiftTowardsTopRightQuadrant() {
        final int initialColour = red;
        final int[] quadrantColours = {white, initialColour, green, blue};
        final int xShift = 2;
        final int yShift = -3;
        testShiftTowardsMean(initialColour, quadrantColours, xShift, yShift);
    }

    @Test
    public void shouldShiftTowardsBottomLeftQuadrant() {
        final int initialColour = red;
        final int[] quadrantColours = {white, green, initialColour, blue};
        final int xShift = -3;
        final int yShift = 2;
        testShiftTowardsMean(initialColour, quadrantColours, xShift, yShift);
    }

    @Test
    public void shouldShiftTowardsBottomRightQuadrant() {
        final int initialColour = red;
        final int[] quadrantColours = {white, green, blue, initialColour};
        final int xShift = 2;
        final int yShift = 2;
        testShiftTowardsMean(initialColour, quadrantColours, xShift, yShift);
    }

    private void testShiftTowardsMean(int initialColour, int [] quadrantColours, int xShift, int yShift) {
        final ColourCubeHistogram initialWindow = defineInitialWindow(initialColour);
        final ColourCubeHistogram shiftedWindow = defineShiftedWindow(quadrantColours);
        final ColourCubeDifference comparison = new ColourCubeDifferenceImpl(initialWindow, shiftedWindow);

        // Shift from the shifted window back towards the initial window.
        final Point shift = ColourMeanShift.shift(shiftedWindow, comparison);
        Assert.assertNotNull(shift);
        Assert.assertEquals(xShift, shift.x);
        Assert.assertEquals(yShift, shift.y);
    }

    private ColourCubeHistogram defineInitialWindow(int rgb) {
        final MutableColourCubeHistogram window = new MutableColourCubeHistogramImpl(width, height, nDivsInSide);
        for (int i=0 ; i<width*height ; i++) {
            add(rgb, i, window);
        }

        return window;
    }

    private ColourCubeHistogram defineShiftedWindow(int [] quadrantColours) {
        final MutableColourCubeHistogram window = new MutableColourCubeHistogramImpl(width, height, nDivsInSide);

        for (int i=0 ; i<width*height ; i++) {
            final int quadrantIndex = findQuadrantIndex(i);
            final int colour = quadrantColours[quadrantIndex];
            add(colour, i, window);
        }

        return window;
    }

    private int findQuadrantIndex(int pixelIndex) {
        final int qx = (pixelIndex%width)/halfWidth;
        final int qy = (pixelIndex/width)/halfHeight;

        if (qx == 0 && qy == 0) {
            return topLeftQuadrant;
        } else if (qx == 1 && qy == 0) {
            return topRightQuadrantIndex;
        } else if (qx == 0 && qy == 1) {
            return bottomLeftQuadrantIndex;
        } else if (qx == 1 && qy == 1) {
            return bottomRightQuadrantIndex;
        } else {
            throw new IllegalArgumentException("Illegal pixel index: " + pixelIndex);
        }
    }
    
    private void add(int rgb, int pixelIndex, MutableColourCubeHistogram histogram) {
        histogram.add(pixelIndex, rgb);
    }
}
