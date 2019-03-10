package com.test.image.cluster;

import com.test.image.model.Constants;
import com.test.image.util.ColourHelper;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

/**
 * Created by Thanos Mavroidis on 09/03/2019.
 */
public final class MeanShiftColourClusterTest {
    private final double piOver2 = Math.PI/2;
    private final Random random = new Random(System.currentTimeMillis());

    @Test
    public void shouldFindCluster() {
        final int radius1 = 10;
        final int centre1 = ColourHelper.getRgb(127, 127, 127);
        final Collection<Integer> cluster1 = generatePointsInCluster(centre1, radius1, 1000);
        Assert.assertEquals(1000, cluster1.size());

        final int radius2 = 10;
        final int centre2 = ColourHelper.getRgb(127 + radius1 + radius2 + 2, 127, 127);
        final Collection<Integer> cluster2 = generatePointsInCluster(centre2, radius2, 10000);
        Assert.assertEquals(10000, cluster2.size());

        assertNoCommonElements(cluster1, cluster2);

        final Collection<Integer> allPoints = new ArrayList(cluster1.size() + cluster2.size());
        allPoints.addAll(cluster1);
        allPoints.addAll(cluster2);

        final int radius = 10;
        final int centre = ColourHelper.getRgb(127 + radius1 + 1, 127, 127);
        final MeanShiftColourCluster underTest = new MeanShiftColourCluster(centre, radius, allPoints);

        underTest.cluster(1000);
        final Collection<Integer> clusterPoints = underTest.getPointsInCluster();
        Assert.assertTrue(!clusterPoints.isEmpty());
    }

    private Collection<Integer> generatePointsInCluster(int centre, double maxRadius, int n) {
        final int r = ColourHelper.getRed(centre);
        final int g = ColourHelper.getGreen(centre);
        final int b = ColourHelper.getBlue(centre);
        checkArguments(r, g, b, maxRadius);

        final Collection<Integer> points = new ArrayList<>(n);
        for (int i=0 ; i<n ; i++) {
            final double radius = maxRadius*random.nextDouble();
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

    private void assertNoCommonElements(Collection<Integer> cluster1, Collection<Integer> cluster2) {
        for (int p1 : cluster1) {
            for (int p2 : cluster2) {
                Assert.assertFalse(p1 == p2);
            }
        }
    }
}
