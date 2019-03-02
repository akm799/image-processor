package com.test.image.processors.corner.moravec;

import com.test.image.ImageDataProcessor;
import com.test.image.model.Direction;
import com.test.image.model.GrayScaleImage;

/**
 * https://arxiv.org/pdf/1209.1558.pdf
 *
 * Look at "V. Moravec Corner Detection" inside the document at the link above.
 */
final class MoravecCornerScoreEvaluator implements ImageDataProcessor {

    MoravecCornerScoreEvaluator() {}

    @Override
    public GrayScaleImage processImage(GrayScaleImage image) {
        final GrayScaleImage output = new GrayScaleImage(image.getWidth(), image.getHeight());
        for (int j=0 ; j<image.getHeight() ; j++) {
            for (int i=0 ; i<image.getWidth() ; i++) {
                final int score = evaluateCornerScore(image, i, j);
                output.setPixel(i, j, score);
            }
        }

        return output;
    }

    // Compute the square difference sum for all possible directions and
    // then return the minimum value.
    private int evaluateCornerScore(GrayScaleImage image, int x, int y) {
        int min = Integer.MAX_VALUE;
        for (Direction direction : Direction.values()) {
            final int score = evaluateCornerScore(image, x, y, direction);
            if (score < min) {
                min = score;
            }
        }

        return min;
    }

    // The sum of the squares of pixel intensity differences between pixels in a 3x3 window
    // centred at the pixel (x, y) and the same window shifted by 1 pixel along the specified
    // direction.
    private int evaluateCornerScore(GrayScaleImage image, int x, int y, Direction direction) {
        final int xTopLeft = x - 1;
        final int yTopLeft = y - 1;
        final int xBottomRight = x + 1;
        final int yBottomRight = y + 1;

        final int xShift = direction.xOffset;
        final int yShift = direction.yOffset;

        int sum = 0;
        for (int j=yTopLeft ; j<=yBottomRight ; j++) {
            for (int i=xTopLeft ; i<=xBottomRight ; i++) {
                final int value = getValueSafe(image, i, j);
                final int shiftedValue = getValueSafe(image, i + xShift, j + yShift);
                final int diff = shiftedValue - value;
                sum += (diff * diff);
            }
        }

        return sum;
    }

    // If (x,y) falls outside the image boundaries, then return the closest boundary pixel. This can
    // happen for pixels near the image border, where part of the filter window will fall outside the
    // image area.
    private int getValueSafe(GrayScaleImage input, int x, int y) {
        final int xSafe = getIndexSafe(x, input.getWidth());
        final int ySafe = getIndexSafe(y, input.getHeight());

        return input.getPixel(xSafe, ySafe);
    }

    private int getIndexSafe(int index, int maxExclusive) {
        if (index < 0) {
            return 0;
        } else if (index >= maxExclusive) {
            return maxExclusive - 1;
        } else {
            return index;
        }
    }
}
