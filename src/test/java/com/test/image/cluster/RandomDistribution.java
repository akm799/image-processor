package com.test.image.cluster;

import java.util.Random;

/**
 * Utility class to return random variables distributed according to a pre-defined function.
 *
 * Created by Thanos Mavroidis on 10/03/2019.
 */
abstract class RandomDistribution {
    private final Random random = new Random(System.currentTimeMillis());

    private final double min;
    private final double range;
    private final double sumOfWidths;
    private final double[] binWidths;

    /**
     * @param min the minimum value that can be produced by this random generator
     * @param max the maximum value that can be produced by this random generator
     */
    RandomDistribution(double min, double max) {
        this(10000, min, max);
    }

    /**
     * @param n the number of bins used internally for the distribution function
     * @param min the minimum value that can be produced by this random generator
     * @param max the maximum value that can be produced by this random generator
     */
    RandomDistribution(int n, double min, double max) {
        checkArguments(n, min, max);

        this.min = min;
        this.range = max - min;
        this.binWidths = new double[n];
        this.sumOfWidths = generateBinWidths();
    }

    private void checkArguments(int n, double min, double max) {
        if (n < 1) {
            throw new IllegalArgumentException("Illegal number of bins argument: " + n);
        }

        if (min >= max) {
            throw new IllegalArgumentException("Illegal range arguments: min=" + min + ", max=" + max);
        }
    }

    private double generateBinWidths() {
        final double w = 1.0/binWidths.length;

        double sum = 0;
        for (int i=0 ; i<binWidths.length ; i++) {
            binWidths[i] = distribution(w*i);
            if (binWidths[i] < 0) {
                throw new IllegalStateException("Negative bin width returned (" + binWidths[i] + ") for bin with index " + i + ". Distribution function argument x=" + (w*i));
            }

            sum += binWidths[i];
        }

        return sum;
    }

    /**
     * The function according to which, our random variables will be distributed.
     *
     * @param x distribution function argument which will always be between zero and one.
     * @return the distribution function value which must never be negative.
     */
    abstract double distribution(double x);

    /**
     * Returns a random variable in the range specified in the constructor and distributed
     * by the distribution function defined by {@link #distribution(double)}.
     *
     * @return a random variable in the range specified in the constructor and distributed
     * by the distribution function defined by {@link #distribution(double)}
     */
    final double nextInDist() {
        final double x = random.nextDouble();
        final int index = findBinIndex(x);

        return generateValueInBinWithIndex(index);
    }

    private int findBinIndex(double x) {
        final double xt = x*sumOfWidths;

        double binMax;
        double binMin = 0;
        for (int i=0 ; i<binWidths.length ; i++) {
            binMax = binMin + binWidths[i];
            if (binMin <= xt && xt <= binMax) {
                return i;
            }

            binMin = binMax;
        }

        throw new IllegalArgumentException("Could not find bin index for x=" + x);
    }

    private double generateValueInBinWithIndex(int index) {
        return min + index*range/binWidths.length;
    }
}
