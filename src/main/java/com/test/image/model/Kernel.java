package com.test.image.model;

public final class Kernel {
    public static final int MIN_SIZE = 3;

    private final int n;
    private final int n2;
    private final float sum;
    private final int[][] k;

    public Kernel(int[][] k) {
        checkValues(k);

        this.n = k.length;
        this.n2 = n/2;
        this.sum = sum(k);
        this.k = new int[n][n];
        System.arraycopy(k, 0, this.k, 0, this.n);
    }

    private void checkValues(int[][] k) {
        if (k == null) {
            throw new IllegalArgumentException("Null input array.");
        }

        final int n = k.length;
        if (n == 0) {
            throw new IllegalArgumentException("Empty input array.");
        }

        if (n < MIN_SIZE) {
            throw new IllegalArgumentException("Illegal input array size: " + n + ". It must be an odd number greater than or equal to " + MIN_SIZE + ".");
        }

        if (n%2 == 0) {
            throw new IllegalArgumentException("Illegal input array size: " + n + ". It must be an odd number.");
        }

        for (int[] r : k) {
            if (r == null || r.length != n) {
                throw new IllegalArgumentException("The input array is not a square one.");
            }

            for (int v : r) {
                if (v < 0) {
                    throw new IllegalArgumentException("Illegal kernel cell value " + v + ". It cannot be negative.");
                }
            }
        }
    }

    private int sum(int[][] k) {
        int sum = 0;
        for (int j=0 ; j<n ; j++) {
            for (int i=0 ; i<n ; i++) {
                sum += k[j][i];
            }
        }

        return sum;
    }

    /**
     * Applies this kernel to the input image for the input pixel and mirrors
     * any pixels that fall outside the input image.
     */
    public int applyMirror(GrayScaleImage image, int w, int h, int x, int y) {
        final int minX = x - n2;
        final int minY = y - n2;

        int t = 0;
        for (int j=0 ; j<n ; j++) {
            final int yp = minY + j;
            for (int i=0 ; i<n ; i++) {
                t += k[j][i] * getSafePixel(image, w, h, minX + i, yp);
            }
        }

        return Math.round(t/sum);
    }

    /**
     * Applies this kernel to the input image for the input pixel but ignores
     * any pixels that fall outside the input image.
     */
    public int applyStrict(GrayScaleImage image, int w, int h, int x, int y) {
        final int minX = x - n2;
        final int minY = y - n2;

        int t = 0;
        for (int j=0 ; j<n ; j++) {
            final int yp = minY + j;
            if (0 <= yp && yp < h) {
                for (int i=0 ; i<n ; i++) {
                    final int xp = minX + i;
                    if (0 <= xp && xp < w) {
                        t += k[j][i] * getSafePixel(image, w, h, xp, yp);
                    }
                }
            }
        }

        return Math.round(t/sum);
    }

    private int getSafePixel(GrayScaleImage data, int w, int h, int x, int y) {
        final int xSafe = getSafe(w, x);
        final int ySafe = getSafe(h, y);

        return data.getPixel(xSafe, ySafe);
    }

    private int getSafe(int maxExclusive, int i) {
        if (i < 0) {
            return -i;
        } else if (i >= maxExclusive) {
            return maxExclusive - i + maxExclusive - 2;
        } else {
            return i;
        }
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        toString(sb);

        return sb.toString();
    }

    private void toString(StringBuffer sb) {
        final String[][] s = toStrings(k);

        for (int j=0 ; j<n ; j++) {
            for (int i=0 ; i<n ; i++) {
                sb.append(s[j][i]);
                sb.append(' ');
            }
            sb.append('\n');
        }
    }

    private String[][] toStrings(int[][] k) {
        final int maxLen = maxLen(k);
        final String[][] result = new String[n][n];
        final StringBuffer sb = new StringBuffer(maxLen);
        for (int j=n-1 ; j>=0 ; j--) {
            for (int i=0 ; i<n ; i++) {
                leftPad(k[j][i], maxLen, sb);
                result[j][i] = sb.toString();
            }
        }

        return result;
    }

    private int maxLen(int[][] k) {
        int m = -1;
        for (int j=0 ; j<n ; j++) {
            for (int i=0 ; i<n ; i++) {
                if (k[j][i] > m) {
                    m = k[j][i];
                }
            }
        }

        return Integer.toString(m).length();
    }

    private void leftPad(int v, int maxLen, StringBuffer sb) {
        sb.delete(0, maxLen);

        final String s = Integer.toString(v);
        final int np = maxLen - s.length();
        for (int i=0 ; i<np ; i++) {
            sb.append(' ');
        }
        sb.append(s);
    }
}
