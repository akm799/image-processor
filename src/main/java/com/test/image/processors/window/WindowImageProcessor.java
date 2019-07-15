package com.test.image.processors.window;

import com.test.image.AbstractFileImageProcessor;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Returns an output image with one or more rectangular windows
 * superimposed on the input image.
 *
 * Created by Thanos Mavroidis on 06/07/2019.
 */
public final class WindowImageProcessor extends AbstractFileImageProcessor {
    private final Collection<ColouredWindow> windows = new ArrayList<>();

    /**
     * @param window the window to superimpose when processing the image
     */
    public WindowImageProcessor(Rectangle window, int rgb) {
        this.windows.add(new ColouredWindow(window, rgb));
    }

    /**
     * @param windows the windows to superimpose when processing the image
     */
    public WindowImageProcessor(Collection<ColouredWindow> windows) {
        this.windows.addAll(windows);
    }

    @Override
    public String getDescription() {
        return "Superimposed windows on image.";
    }

    @Override
    public BufferedImage processImage(BufferedImage image) {
        checkAllWindowsAreWithinTheImage(image);
        return drawWBufferedImageWithWindows(image, windows);
    }

    private BufferedImage drawWBufferedImageWithWindows(BufferedImage input, Collection<ColouredWindow> windows) {
        final int width = input.getWidth();
        final int height = input.getHeight();
        final BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int j=0 ; j<height ; j++) {
            for (int i=0 ; i<width ; i++) {
                final int rgb = getOutputPixelColour(input, windows, i, j);
                output.setRGB(i, j, rgb);
            }
        }

        return output;
    }

    private int getOutputPixelColour(BufferedImage input, Collection<ColouredWindow> windows, int x, int y) {
        final ColouredWindow borderPixelWindow = isWindowBorderPixel(windows, x, y);
        if (borderPixelWindow == null) {
            return input.getRGB(x, y);
        } else {
            return borderPixelWindow.rgb;
        }
    }

    private ColouredWindow isWindowBorderPixel(Collection<ColouredWindow> windows, int x, int y) {
        for (ColouredWindow window : windows) {
            if (isWindowBorderPixel(window, x, y)) {
                return window;
            }
        }

        return null;
    }

    private boolean isWindowBorderPixel(Window window, int x, int y) {
        if (x == window.xMin && (window.yMin <= y && y <= window.yMax)) {
            return true;
        } else if (x == window.xMax && (window.yMin <= y && y <= window.yMax)) {
            return true;
        } else if (y == window.yMin && (window.xMin <= x && x <= window.xMax)) {
            return true;
        } else if (y == window.yMax && (window.xMin <= x && x <= window.xMax)) {
            return true;
        } else {
            return false;
        }
    }

    private void checkAllWindowsAreWithinTheImage(BufferedImage image) {
        final Window imageWindow = new Window(new Rectangle(0, 0, image.getWidth(), image.getHeight()));
        for (Window window : windows) {
            checkWindowIsWithinTheImage(window, imageWindow);
        }
    }

    private void checkWindowIsWithinTheImage(Window window, Window image) {
        if (window.xMin < image.xMin || window.xMax > image.xMax || window.yMin < image.yMin || window.yMax > image.yMax) {
            throw new IllegalArgumentException("Window " + window + " is not entirely within the image window " + image + ".");
        }
    }
}
