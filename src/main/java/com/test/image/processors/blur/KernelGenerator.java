package com.test.image.processors.blur;

import com.test.image.model.Kernel;

public final class KernelGenerator {
    private static final int MIN_SIZE = 3;

    public static void main(String[] args) {
        final KernelGenerator underTest = new KernelGenerator();
        print(underTest, 3);
        System.out.println();

        print(underTest, 5);
        System.out.println();

        print(underTest, 7);
        System.out.println();

        print(underTest, 31);
    }

    private static void print(KernelGenerator underTest, int size) {
        final Kernel kernel = underTest.gaussianKernel(size);
        System.out.println(kernel);
    }


    public Kernel gaussianKernel(int size) {
        if (size < MIN_SIZE) {
            throw new IllegalArgumentException("Illegal kernel size: " + size + ". It must be an odd number greater than or equal to " + MIN_SIZE + ".");
        }

        return new Kernel(gaussianValues(size));
    }

    private int[][] gaussianValues(int size) {
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
