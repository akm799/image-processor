package com.test.image.model;

public final class Kernel {
    private final int sum;
    private final int[][] k;

    public Kernel(int[][] k) {
        this.sum = sum(k);
        final int n = k[0].length;
        this.k = new int[n][n];
        System.arraycopy(k, 0, this.k, 0, n);
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

    public int getSize() {
        return k[0].length;
    }

    public int getValue(int i, int j) {
        return k[j][i];
    }

    public int getSum() {
        return sum;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        toString(sb);

        return sb.toString();
    }

    private void toString(StringBuffer sb) {
        final String[][] s = toStrings(k);
        final int n = s[0].length;

        // Top
        for (int j=n-1 ; j>=0 ; j--) {
            // Top Left
            for (int i=n-1 ; i>0 ; i--) {
                sb.append(s[j][i]);
                sb.append(' ');
            }

            // Top Right
            for (int i=0 ; i<n ; i++) {
                sb.append(s[j][i]);
                sb.append(' ');
            }

            sb.append('\n');
        }

        // Bottom
        for (int j=1 ; j<n ; j++) {
            // Bottom Left
            for (int i=n-1 ; i>0 ; i--) {
                sb.append(s[j][i]);
                sb.append(' ');
            }

            // Bottom Right
            for (int i=0 ; i<n ; i++) {
                sb.append(s[j][i]);
                sb.append(' ');
            }

            sb.append('\n');
        }
    }

    private String[][] toStrings(int[][] k) {
        final int n = k[0].length;
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
