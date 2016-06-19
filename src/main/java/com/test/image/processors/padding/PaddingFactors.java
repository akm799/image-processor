package com.test.image.processors.padding;

public final class PaddingFactors {
    public final float left;
    public final float top;
    public final float right;
    public final float bottom;

    public PaddingFactors(float left, float top, float right, float bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        addPaddingStr("left",   left,   sb);
        addPaddingStr("top",    top,    sb);
        addPaddingStr("right",  right,  sb);
        addPaddingStr("bottom", bottom, sb);

        return sb.toString();
    }

    private void addPaddingStr(String title, float f, StringBuilder sb) {
        if (f != 0.0f) {
            sb.append(' ');
            sb.append(title);
            sb.append(": ");
            sb.append(toPercent(f));
        }
    }

    private String toPercent(float f) {
        return (Float.toString(100*f) + '%');
    }
}
