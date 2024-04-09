package com.test.image.model;

public final class Location {
    private int x = -1;
    private int y = -1;

    public boolean isNotEmpty() {
        return x >= 0 && y >= 0;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public void set(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void clear() {
        set(-1, -1);
    }
}
