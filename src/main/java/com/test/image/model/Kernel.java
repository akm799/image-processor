package com.test.image.model;

public final class Kernel {
    public static final int MIN_SIZE = 3;

    private final int sum;
    private final int[][] k;

    public Kernel(int[][] k) {
        checkValues(k);

        this.sum = sum(k);
        final int n = k[0].length;
        this.k = new int[n][n];
        System.arraycopy(k, 0, this.k, 0, n);
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
            throw new IllegalArgumentException("The input array of size " + n + " is less than the minimum allowed size of " + MIN_SIZE + ".");
        }

        if (n%2 == 0) {
            throw new IllegalArgumentException("The size " + n + " of the input array is not an odd number.");
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
        final int n = k[0].length;
        for (int j=0 ; j<n ; j++) {
            for (int i=0 ; i<n ; i++) {
                sum += k[j][i];
            }
        }

        return sum;
    }

    public int apply(GrayScaleImage image, int x, int y) {
        return 0; //TODO
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        toString(sb);

        return sb.toString();
    }

    private void toString(StringBuffer sb) {
        final String[][] s = toStrings(k);

        final int n = k.length;
        for (int j=0 ; j<n ; j++) {
            for (int i=0 ; i<n ; i++) {
                sb.append(s[j][i]);
                sb.append(' ');
            }
            sb.append('\n');
        }
    }

    private String[][] toStrings(int[][] k) {
        final int n = k.length;
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
        final int n = k[0].length;
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
