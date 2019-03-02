package com.test.image.processors.corner.moravec;

import com.test.image.ImageDataProcessor;
import com.test.image.model.Constants;
import com.test.image.model.Direction;
import com.test.image.model.GrayScaleImage;

import java.util.HashMap;
import java.util.Map;

@Deprecated
public final class MoravecCornerScoreEvaluator2 implements ImageDataProcessor {
    private final CornerConfig config = new CornerConfig();
    private final Map<Direction, Integer> directionalScores = new HashMap<>(Direction.values().length);
    private final Map<Direction, Direction[]> orthogonalDirections = new HashMap<>(Direction.values().length);

    private final Map<Direction, Integer> orthogonalScores = new HashMap<>(Direction.values().length);

    public MoravecCornerScoreEvaluator2() {
        orthogonalDirections.put(Direction.EAST, new Direction[]{Direction.SOUTH, Direction.NORTH});
        orthogonalDirections.put(Direction.NORTH_EAST, new Direction[]{Direction.SOUTH_EAST, Direction.NORTH_WEST});
        orthogonalDirections.put(Direction.NORTH, new Direction[]{Direction.EAST, Direction.WEST});
        orthogonalDirections.put(Direction.NORTH_WEST, new Direction[]{Direction.NORTH_EAST, Direction.SOUTH_WEST});
        orthogonalDirections.put(Direction.WEST, new Direction[]{Direction.NORTH, Direction.SOUTH});
        orthogonalDirections.put(Direction.SOUTH_WEST, new Direction[]{Direction.NORTH_WEST, Direction.SOUTH_EAST});
        orthogonalDirections.put(Direction.SOUTH, new Direction[]{Direction.WEST, Direction.EAST});
        orthogonalDirections.put(Direction.SOUTH_EAST, new Direction[]{Direction.SOUTH_WEST, Direction.NORTH_EAST});
    }

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
        for (Direction direction : Direction.values()) {
            final int score = evaluateCornerScore(image, x, y, direction);
            directionalScores.put(direction, score);
        }

        return evaluateCornerScore();
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

    //TODO Fix this methos.
    private int evaluateCornerScore() {
        evaluateOrthogonalScores();

        final int minOrthogonalDiffPerCent = 1;
        final int minValue = (int)Math.pow(0.01*Constants.MAX_INTENSITY, 2);
        for (Direction direction : directionalScores.keySet()) {
            final int value = directionalScores.get(direction);
            if (value > 0) {
                final int orthogonalDiff = Math.abs(value - orthogonalScores.get(direction));
                if ((100 * orthogonalDiff) / value < minOrthogonalDiffPerCent && value > minValue) {
                    return Constants.MAX_INTENSITY;
                }
            }
        }

        return 0;
    }

    private void evaluateOrthogonalScores() {
        for (Direction direction : directionalScores.keySet()) {
            final Direction[] orthogonal = orthogonalDirections.get(direction);
            final int score1 = directionalScores.get(orthogonal[0]);
            final int score2 = directionalScores.get(orthogonal[1]);
            final int orthogonalScore = Math.max(score1, score2);
            orthogonalScores.put(direction, orthogonalScore);
        }
    }
}
