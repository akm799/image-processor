package com.test.image.model;


public enum Direction {

    EAST       ( 1,   0,   -Constants.STEP,    Constants.STEP),
    NORTH_EAST ( 1,  -1,    Constants.STEP,  3*Constants.STEP),
    NORTH      ( 0,  -1,  3*Constants.STEP,  5*Constants.STEP),
    NORTH_WEST (-1,  -1,  5*Constants.STEP,  7*Constants.STEP),
    WEST       (-1,   0, -7*Constants.STEP,  7*Constants.STEP),
    SOUTH_WEST (-1,   1, -7*Constants.STEP, -5*Constants.STEP),
    SOUTH      ( 0,   1, -5*Constants.STEP, -3*Constants.STEP),
    SOUTH_EAST ( 1,   1, -3*Constants.STEP,   -Constants.STEP);

    public final int xOffset;
    public final int yOffset;
    private final float minAngle;
    private final float maxAngle;

    Direction(int xOffset, int yOffset, float minAngle, float maxAngle) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.minAngle = minAngle;
        this.maxAngle = maxAngle;
    }

    private boolean inRange(float angle) {
        return (minAngle <= angle && angle < maxAngle);
    }

    public static Direction forAngle(float angle) {
        if (Constants.WEST_LIMIT_POSITIVE <= angle && angle <= Constants.FLOAT_PI) {
            return WEST;
        } else if (-Constants.FLOAT_PI <= angle && angle < Constants.WEST_LIMIT_NEGATIVE) {
            return WEST;
        }

        for (Direction direction : values()) {
            if (direction != WEST && direction.inRange(angle)) {
                return direction;
            }
        }

        return null;
    }
}
