package com.test.image.processors.corner.moravec;

import com.test.image.ImageDataProcessor;
import com.test.image.model.Direction;
import com.test.image.model.GrayScaleImage;

public final class MoravecCornerScoreEvaluator implements ImageDataProcessor {
    private final CornerConfig config = new CornerConfig();

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

    private int evaluateCornerScore(GrayScaleImage image, int x, int y) {
        int sum = 0;
        for (Direction direction : Direction.values()) {
            sum += evaluateCornerScore(image, x, y, direction);
        }

        return Math.round(sum/(float)Direction.values().length);
    }

    private int evaluateCornerScore(GrayScaleImage image, int x, int y, Direction direction) {
        final int xTopLeft = x - config.windowRadius;
        final int yTopLeft = y - config.windowRadius;
        final int side = 2*config.windowRadius + 1;
        final int xBottomRight = xTopLeft + side;
        final int yBottomRight = yTopLeft + side;

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

        return Math.round(sum/(float)(side*side));
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
