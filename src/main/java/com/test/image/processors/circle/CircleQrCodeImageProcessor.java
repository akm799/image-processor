package com.test.image.processors.circle;


import com.test.image.AbstractFileImageProcessor;
import com.test.image.processors.composition.Overlay;
import com.test.image.processors.composition.OverlayFilter;
import com.test.image.processors.composition.OverlayImageProcessor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

public final class CircleQrCodeImageProcessor extends AbstractFileImageProcessor {
    private final String logoFileName;

    public CircleQrCodeImageProcessor(String logoFileName) {
        this.logoFileName = logoFileName;
    }

    @Override
    public String getDescription() {
        return "Transformed QR code image to a circular one with a logo in the middle.";
    }

    @Override
    public BufferedImage processImage(BufferedImage image) {
        try {
            final BufferedImage logo = ImageIO.read(new File(logoFileName));

            return toCircleWithLogo(image, logo);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private BufferedImage toCircleWithLogo(BufferedImage qrCode, BufferedImage logo) {
        final double centreRadiusFraction = computeCentreRadiusFraction(qrCode, logo);
        final BufferedImage hollowCircleQrCode = toHollowCircle(qrCode, centreRadiusFraction);

        return placeLogoInCentre(hollowCircleQrCode, logo);
    }

    private double computeCentreRadiusFraction(BufferedImage qrCode, BufferedImage logo) {
        final double r = qrCode.getWidth()/2.0;

        return logo.getWidth()/(2*r);
    }

    private BufferedImage toHollowCircle(BufferedImage qrCode, double centreRadiusFraction) {
        return new SquareToHollowCircleImageProcessor(centreRadiusFraction).processImage(qrCode);
    }

    private BufferedImage placeLogoInCentre(BufferedImage hollowCircleQrCode, BufferedImage logo) {
        final Collection<Overlay> overlays = computeOverlay(hollowCircleQrCode, logo);

        return (new OverlayImageProcessor(overlays, true)).processImage(hollowCircleQrCode);
    }

    private Collection<Overlay> computeOverlay(BufferedImage hollowCircleQrCode, BufferedImage logo) {
        final double cx = hollowCircleQrCode.getWidth()/2.0;
        final double cy = hollowCircleQrCode.getHeight()/2.0;
        final double r = logo.getWidth()/2.0;
        final int left = (int)Math.round(cx - r);
        final int top = (int)Math.round(cy - r);

        final OverlayFilter circleFilter = new OverlayFilter() {
            @Override
            public boolean includePixel(int columnIndex, int rowIndex) {
                final double x = columnIndex - r;
                final double y = rowIndex - r;

                return Math.sqrt(x*x + y*y) < r;
            }
        };

        final Overlay logoOverlay = new Overlay(logo, left, top, circleFilter);

        return Arrays.asList(logoOverlay);
    }
}
