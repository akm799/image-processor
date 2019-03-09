package com.test.image.cluster;

import com.test.image.util.ColourHelper;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Thanos Mavroidis on 09/03/2019.
 */
public final class MeanShiftColourCluster {
    private int centre;
    private final double radius;
    private Collection<Integer> allPoints;
    private Collection<Integer> pointsInCluster;

    public MeanShiftColourCluster(int centre, double radius, Collection<Integer> points) {
        this.centre = centre;
        this.radius = radius;
        this.allPoints = new ArrayList<>(points);
        this.pointsInCluster = new ArrayList<>(points.size());

        initPoints();
    }

    private void initPoints() {
        keepPointsInCluster(centre);
        if (pointsInCluster.isEmpty()) {
            throw new IllegalArgumentException("Could not find any initial points inside the cluster.");
        }
    }

    public void cluster(int maxIterations) {
        int n = pointsInCluster.size();

        int i = 0;
        boolean notConverged = true;
        while (notConverged && i < maxIterations) {
            iterate();
            notConverged = (pointsInCluster.size() > n);
            n = pointsInCluster.size();
            i++;
        }
    }

    private void iterate() {
        centre = findMean(pointsInCluster);
        keepPointsInCluster(centre);
    }

    private void keepPointsInCluster(int centre) {
        final int rC = ColourHelper.getRed(centre);
        final int gC = ColourHelper.getGreen(centre);
        final int bC = ColourHelper.getBlue(centre);

        pointsInCluster.clear();
        for (int p : allPoints) {
            if (isInsideCluster(p, rC, gC, bC)) {
                pointsInCluster.add(p);
            }
        }
    }

    private boolean isInsideCluster(int point, int rC, int gC, int bC) {
        final int rD = ColourHelper.getRed(point) - rC;
        final int gD = ColourHelper.getGreen(point) - gC;
        final int bD = ColourHelper.getBlue(point) - bC;
        final double d = Math.sqrt(rD*rD + gD*gD + bD*bD);

        return (d <= radius);
    }

    private int findMean(Collection<Integer> points) {
        int rSum = 0;
        int gSum = 0;
        int bSum = 0;
        for (int p : points) {
            rSum += ColourHelper.getRed(p);
            gSum += ColourHelper.getGreen(p);
            bSum += ColourHelper.getBlue(p);
        }

        final int n = points.size();
        final int r = Math.round((float)rSum/n);
        final int g = Math.round((float)gSum/n);
        final int b = Math.round((float)bSum/n);

        return ColourHelper.getRgb(r, g, b);
    }

    public Collection<Integer> getPointsInCluster() {
        return pointsInCluster;
    }
}
