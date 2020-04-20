package com.test.image.processors.composition;

import com.test.image.AbstractFileImageProcessor;
import com.test.image.util.ColourHelper;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;

public class OverlayImageProcessor extends AbstractFileImageProcessor {
    private final boolean baseSymmetric;
    private final Collection<Overlay> overlays = new ArrayList<>();

    private int width;
    private int height;
    private int baseLeft;
    private int baseTop;

    public OverlayImageProcessor(Collection<Overlay> overlays, boolean baseSymmetric) {
        this.overlays.addAll(overlays);
        this.baseSymmetric = baseSymmetric;
    }

    @Override
    public String getDescription() {
        return ("Overlaid " + overlays.size() + " image" + (overlays.size() == 1 ? "." : "s."));
    }

    @Override
    public BufferedImage processImage(BufferedImage base) {
        setNewImageDimensions(base);
        final BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        copyBaseImage(base, outputImage);
        for (Overlay overlay : overlays) {
            copyOverlayImage(overlay, outputImage);
        }

        return outputImage;
    }

    private void setNewImageDimensions(BufferedImage base) {
        int maxLeft = 0;
        int maxRight = 0;
        int maxTop = 0;
        int maxBottom = 0;
        for (Overlay overlay : overlays) {
            maxLeft = Math.max(maxLeft, -overlay.left);
            maxRight = Math.max(maxRight, overlay.left + overlay.image.getWidth() - base.getWidth());
            maxTop = Math.max(maxTop, -overlay.top);
            maxBottom = Math.max(maxBottom, overlay.top + overlay.image.getHeight() - base.getHeight());
        }

        if (baseSymmetric) {
            final int extraSideWidth = Math.max(maxLeft, maxRight);
            width = base.getWidth() + 2*extraSideWidth;
            baseLeft = extraSideWidth;

            final int extraSideHeight = Math.max(maxTop, maxBottom);
            height = base.getHeight() + 2*extraSideHeight;
            baseTop = extraSideHeight;
        } else {
            width = base.getWidth() + maxLeft + maxRight;
            baseLeft = maxLeft;

            height = base.getHeight() + maxTop + maxBottom;
            baseTop = maxTop;
        }
    }

    private void copyBaseImage(BufferedImage base, BufferedImage outputImage) {
        for (int j=0 ; j<base.getHeight() ; j++) {
            for (int i=0 ; i<base.getWidth() ; i++) {
                final int rgb = base.getRGB(i, j);
                outputImage.setRGB(baseLeft + i, baseTop + j, rgb);
            }
        }
    }

    private void copyOverlayImage(Overlay overlay, BufferedImage image) {
        final int left = overlay.left + baseLeft;
        final int top = overlay.top + baseTop;
        for (int j=0 ; j<overlay.image.getHeight() ; j++) {
            for (int i=0 ; i<overlay.image.getWidth() ; i++) {
                final int rgb = overlay.image.getRGB(i, j);
                final int alpha = ColourHelper.getAlpha(rgb);
                if (alpha > 0) { //TODO Blend colour with correct alpha.
                    image.setRGB(left + i, top + j, rgb);
                }
            }
        }
    }
}