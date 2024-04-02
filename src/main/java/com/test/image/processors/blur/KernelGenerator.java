package com.test.image.processors.blur;

import com.test.image.model.Kernel;

public final class KernelGenerator {

    public static void main(String[] args) {
        final KernelGenerator kg = new KernelGenerator();

        final int[][] k1 = kg.generateKernel(3);
        final Kernel kernel1 = new Kernel(k1);
        System.out.println(kernel1);
        System.out.println();

        final int[][] k2 = kg.generateKernel(5);
        final Kernel kernel2 = new Kernel(k2);
        System.out.println(kernel2);
        System.out.println();

        final int[][] k3 = kg.generateKernel(7);
        final Kernel kernel3 = new Kernel(k3);
        System.out.println(kernel3);
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
