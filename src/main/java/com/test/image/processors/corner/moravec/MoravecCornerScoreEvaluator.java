package com.test.image.processors.corner.moravec;

import com.test.image.model.Direction;
import com.test.image.model.GrayScaleImage;

public final class MoravecCornerScoreEvaluator implements CornerScoreEvaluator {
    private final CornerConfig config = new CornerConfig();

    @Override
    public GrayScaleImage evaluateCornerScores(GrayScaleImage image) {
        final GrayScaleImage output = new GrayScaleImage(image.getWidth(), image.getHeight());
        for (int j=0 ; j<image.getHeight() ; j++) {
            for (int i=0 ; i<image.getWidth() ; i++) {
                final int score = evaluateCornerScore(image, i, j);
                output.setPixel(i, j, score);
            }
        }

        return output;
    }

    private int evaluateCornerScore(GrayScaleImage image, int x, int y) {
        int sum = 0;
        for (Direction direction : Direction.values()) {
            sum += evaluateCornerScore(image, x, y, direction);
        }

        return sum;
    }

    private int evaluateCornerScore(GrayScaleImage image, int x, int y, Direction direction) {
        final int xTopLeft = x - config.windowRadius;
        final int yTopLeft = y - config.windowRadius;
        final int xBottomRight = xTopLeft + 2*config.windowRadius + 1;
        final int yBottomRight = yTopLeft + 2*config.windowRadius + 1;

        final int xShift = direction.xOffset*config.windowShift;
        final int yShift = direction.yOffset*config.windowShift;

        int sum = 0;
        for (int j=yTopLeft ; j<=yBottomRight ; j++) {
            for (int i=xTopLeft ; i<=xBottomRight ; i++) {
                final int value = getValueSafe(image, i, j);
                final int shiftedValue = getValueSafe(image, i + xShift, j + yShift);
                final int diff = value - shiftedValue;
                sum += diff*diff;
            }
        }

        return sum;
    }

    // If (x,y) falls outside the image boundaries, then mirror the pixel. This can happen for pixels
    // near the image border, where part of the filter window will fall outside the image area.
    private int getValueSafe(GrayScaleImage input, int x, int y) {
        if (x < 0) {
            x = -x;
        } else if (x >= input.getWidth()) {
            x = 2*input.getWidth() - x - 1;
        }

        if (y < 0) {
            y = -y;
        } else if (y >= input.getHeight()) {
            y = 2*input.getHeight() - y - 1;
        }

        return input.getPixel(x, y);
    }
}
