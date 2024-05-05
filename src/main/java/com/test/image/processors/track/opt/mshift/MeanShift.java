package com.test.image.processors.track.opt.mshift;

import java.awt.*;

final class MeanShift {
    private static final int X_INDEX = 0;
    private static final int Y_INDEX = 1;

    private final ColourHistogram similarity;
    private final int[] centre = new int[2];

    private int[][] segmentPixels;

    MeanShift(ColourHistogram similarity) {
        this.similarity = similarity;
    }

    Rectangle shift(int[][] imagePixels, Rectangle start) {
        final Rectangle segment = new Rectangle(start);
        segmentPixels = new int[segment.height][segment.width];

        final int deltaMin = 5; // TODO Determine optimum size.
        final int maxTries = 100;

        int nTries = 0;
        boolean keepShifting = true;
        while (keepShifting) {
            final int delta = shiftSegment(imagePixels, segment);
            nTries++;

            keepShifting = (delta > deltaMin && nTries < maxTries);
        }

        return new Rectangle(segment);
    }

    private int shiftSegment(int[][] imagePixels, Rectangle segment) {
        similarity.findSimilarityCentre(segmentPixels, segment, centre);

        final int dx = centre[X_INDEX] - (segment.x + segment.width/2);
        final int dy = centre[Y_INDEX] - (segment.y + segment.height/2);
        if (outOfRange(imagePixels, segment, dx, dy)) {
            System.out.println("Stopped shifting because we will shift outside the image bounds.");
            return -1; // Stop shifting because we will shift outside the image bounds.
        }

        // Shift
        segment.x += dx;
        segment.y += dy;

        return Math.max(Math.abs(dx), Math.abs(dy));
    }

    private boolean outOfRange(int[][] imagePixels, Rectangle segment, int dx, int dy) {
        final int xc = segment.x + dx;
        final int yc = segment.y + dy;

        return xc < 0 || xc + segment.width >= imagePixels[0].length || yc < 0 || yc + segment.height >= imagePixels.length;
    }
}
