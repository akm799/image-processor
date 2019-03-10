package com.test.image.cluster;

import com.test.image.model.Constants;
import com.test.image.model.collections.IntArrayList;
import com.test.image.model.collections.IntCollection;
import com.test.image.model.collections.IntIterator;
import com.test.image.util.ColourHelper;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

/**
 * Created by Thanos Mavroidis on 09/03/2019.
 */
public final class MeanShiftColourClusterAltTest {
    private final double piOver2 = Math.PI/2;
    private final Random random = new Random(System.currentTimeMillis());

    @Test
    public void shouldFindCluster() {
        final int smallClusterRadius = 10;
        final int smallClusterCentre = ColourHelper.getRgb(127, 127, 127);
        final IntCollection smallCluster = generatePointsInCluster(smallClusterCentre, smallClusterRadius, 1000);
        Assert.assertEquals(1000, smallCluster.size());

        final int largeClusterRadius = 10;
        final int largeClusterCentre = ColourHelper.getRgb(127 + smallClusterRadius + largeClusterRadius + 2, 127, 127);
        final IntCollection largeCluster = generatePointsInCluster(largeClusterCentre, largeClusterRadius, 10000);
        Assert.assertEquals(10000, largeCluster.size());

        assertNoCommonElements(smallCluster, largeCluster);

        final IntCollection allPoints = new IntArrayList(smallCluster.size() + largeCluster.size());
        allPoints.addAll(smallCluster);
        allPoints.addAll(largeCluster);

        // Define a cluster window centred exactly between the 2 test clusters defined above.
        final int radius = 10;
        final int centre = ColourHelper.getRgb(127 + smallClusterRadius + 1, 127, 127);
        final MeanShiftColourClusterAlt underTest = new MeanShiftColourClusterAlt(centre, radius, allPoints);

        underTest.cluster(1000);
        final IntCollection clusterPoints = underTest.getPointsInCluster();

        Assert.assertTrue(!clusterPoints.isEmpty());

        // The cluster finder should converge towards the large cluster (since it is the larger).
        final double fraction = (double)clusterPoints.size()/largeCluster.size();
        Assert.assertTrue(fraction > 0.998);

        final int commonCount = countNoCommonElements(largeCluster, clusterPoints);
        Assert.assertEquals(clusterPoints.size(), commonCount);
    }

    private IntCollection generatePointsInCluster(int centre, double maxRadius, int n) {
        final int r = ColourHelper.getRed(centre);
        final int g = ColourHelper.getGreen(centre);
        final int b = ColourHelper.getBlue(centre);
        checkArguments(r, g, b, maxRadius);

        final RandomDistribution oneMinusXDist = new RandomDistribution(0, maxRadius) {
            @Override
            double distribution(double x) {
                return 1 - x;
            }
        };

        final IntCollection points = new IntArrayList(n);
        for (int i=0 ; i<n ; i++) {
            final double radius = oneMinusXDist.nextInDist();
            points.add(generatePointInCluster(r, g, b, radius));
        }

        return points;
    }

    private void checkArguments(int r, int g, int b, double radius) {
        checkLimits(r, radius);
        checkLimits(g, radius);
        checkLimits(b, radius);
    }

    private void checkLimits(int c, double radius) {
        final int min = (int)Math.floor(c - radius);
        final int max = (int)Math.ceil(c + radius);

        if (min < 0 || max > Constants.MAX_INTENSITY) {
            throw new IllegalArgumentException("Cluster parameters can include points outside the limits [0, " + Constants.MAX_INTENSITY + "].");
        }
    }

    private int generatePointInCluster(int r, int g, int b, double radius) {
        final double phi = randomAngle(-Math.PI, Math.PI);
        final double theta = randomAngle(-piOver2, piOver2);
        final double radiusProjectionInXZ = radius*Math.cos(theta);
        final double x = radiusProjectionInXZ*Math.cos(phi);
        final double y = radius*Math.sin(theta);
        final double z = radiusProjectionInXZ*Math.sin(phi);

        final int red = r + (int)Math.round(x);
        final int green = g + (int)Math.round(y);
        final int blue = b + (int)Math.round(z);

        return ColourHelper.getRgb(red, green, blue);
    }

    private double randomAngle(double min, double max) {
        return (max - min)*random.nextDouble() + min;
    }

    private void assertNoCommonElements(IntCollection cluster1, IntCollection cluster2) {
        final IntIterator i1 = cluster1.iterator();
        while (i1.hasNext()) {
            final int p1 = i1.next();

            final IntIterator i2 = cluster2.iterator();
            while (i2.hasNext()) {
                final int p2 = i2.next();
                Assert.assertFalse(p1 == p2);
            }
        }
    }

    private int countNoCommonElements(IntCollection cluster1, IntCollection cluster2) {
        int count = 0;

        final IntIterator i1 = cluster1.iterator();
        while (i1.hasNext()) {
            final int p1 = i1.next();

            final IntIterator i2 = cluster2.iterator();
            while (i2.hasNext()) {
                final int p2 = i2.next();
                if (p1 == p2) {
                    count++;
                    break;
                }
            }
        }

        return count;
    }
}
