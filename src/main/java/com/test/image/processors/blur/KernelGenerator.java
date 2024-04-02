package com.test.image.processors.blur;

import com.test.image.model.Kernel;

public final class KernelGenerator {

    public static void main(String[] args) {
        final KernelGenerator kg = new KernelGenerator();
        print(kg, 3);
        System.out.println();

        print(kg, 5);
        System.out.println();

        print(kg, 7);
        System.out.println();

        print(kg, 31);
    }

    private static void print(KernelGenerator kg, int size) {
        final int[][] k = kg.generateKernel(size);
        final Kernel kernel = new Kernel(k);
        System.out.println(kernel);
    }

    private int[][] generateKernel(int size) {
        final int r = size/2 + size%2;
        final double s = r/3.0;

        final double[][] v = new double[r][r];
        for (int j=0 ; j<r ; j++) {
            for (int i=0 ; i<r ; i++) {
                v[j][i] = g(s, i, j);
            }
        }

        final double n = v[r-1][r-1];
        final int[][] k = new int[r][r];
        for (int j=0 ; j<r ; j++) {
            for (int i=0 ; i<r ; i++) {
                k[j][i] = (int)Math.round(v[j][i]/n);
            }
        }

        return k;
    }

    private double g(double s, double x, double y) {
        final double c = 2*s*s;

        return Math.exp(-(x*x + y*y)/c)/(Math.PI*c);
    }
}
