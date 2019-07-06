package com.test.image.processors.window;

import com.test.image.AbstractFileImageProcessor;
import com.test.image.util.ColourHelper;

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
    private static final int DEFAULT_WINDOW_COLOUR = ColourHelper.getRgb(255, 0, 0);

    private final Collection<Window> windows = new ArrayList();

    private int windowColour = DEFAULT_WINDOW_COLOUR;

    /**
     * @param window the window to superimpose when processing the image
     */
    public WindowImageProcessor(Rectangle window) {
        this.windows.add(new Window(window));
    }

    /**
     * @param windows the windows to superimpose when processing the image
     */
    public WindowImageProcessor(Collection<Rectangle> windows) {
        for (Rectangle window: windows) {
            this.windows.add(new Window(window));
        }
    }

    @Override
    public String getDescription() {
        return "Superimposed windows on image.";
    }

    /**
     * Sets the colour that the superimposed windows will have.
     *
     * @param windowColour the colour that the superimposed windows will have
     */
    public void setWindowColour(int windowColour) {
        this.windowColour = windowColour;
    }

    /**
     * Restores the colour that the superimposed windows will have to the default colour (red).
     */
    public void restoreDefaultWindowColour() {
        this.windowColour = DEFAULT_WINDOW_COLOUR;
    }

    @Override
    public BufferedImage processImage(BufferedImage image) {
        checkAllWindowsAreWithinTheImage(image);
        return drawWBufferedImageWithWindows(image, windows);
    }

    private BufferedImage drawWBufferedImageWithWindows(BufferedImage input, Collection<Window> windows) {
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

    private int getOutputPixelColour(BufferedImage input, Collection<Window> windows, int x, int y) {
        if (isWindowBorderPixel(windows, x, y)) {
            return windowColour;
        } else {
            return input.getRGB(x, y);
        }
    }

    private boolean isWindowBorderPixel(Collection<Window> windows, int x, int y) {
        for (Window window : windows) {
            if (isWindowBorderPixel(window, x, y)) {
                return true;
            }
        }

        return false;
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
