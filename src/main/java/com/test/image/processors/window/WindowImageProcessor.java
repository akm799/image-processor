package com.test.image.processors.window;

import com.test.image.AbstractFileImageProcessor;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
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
        return drawBufferedImageWithWindows(image, windows);
    }

    private BufferedImage drawBufferedImageWithWindows(BufferedImage input, Collection<ColouredWindow> windows) {
        final BufferedImage output = deepCopy(input);
        for (ColouredWindow window : windows) {
            drawBufferedImageWithWindows(output, window);
        }

        return output;
    }

    // https://stackoverflow.com/questions/3514158/how-do-you-clone-a-bufferedimage
    private BufferedImage deepCopy(BufferedImage source) {
        final ColorModel colourModel = source.getColorModel();
        final boolean isAlphaPremultiplied = colourModel.isAlphaPremultiplied();
        final WritableRaster raster = source.copyData(null);

        return new BufferedImage(colourModel, raster, isAlphaPremultiplied, null);
    }

    private void drawBufferedImageWithWindows(BufferedImage output, ColouredWindow window) {
        final int rgb = window.rgb;

        // Horizontal window lines
        for (int i=window.xMin ; i<=window.xMax ; i++) {
            output.setRGB(i, window.yMin, rgb);
            output.setRGB(i, window.yMax, rgb);
        }

        // Vertical window lines
        for (int j=window.yMin ; j<=window.yMax; j++) {
            output.setRGB(window.xMin, j, rgb);
            output.setRGB(window.xMax, j, rgb);
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
