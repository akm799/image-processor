package com.test.image.hough;

public class HoughTransformConfig {
    final int nThetaBins;
    final double thetaMin = 0;
    final double thetaMax = Math.PI;

    final double radiusMin = 0;
    final double radiusMax;
    final int nRadiusBins;

    public HoughTransformConfig(int width, int height) {
        radiusMax = Math.sqrt(width*width + height*height);
        nRadiusBins = 2*(int)Math.ceil(radiusMax);

        final double dTheta = dTheta(width, height);
        nThetaBins = (int)Math.ceil((thetaMax - thetaMin)/dTheta);
    }

    private double dTheta(int width, int height) {
        final double thetaCentral = angle(height, width);
        final double thetaPlus = angle(height - 1, width - 2) - thetaCentral;
        final double thetaMinus = thetaCentral - angle(height - 2, width - 1);

        return Math.min(thetaPlus, thetaMinus)/2;
    }

    private double angle(double dy, double dx) {
        return Math.atan(dy/dx);
    }
}
