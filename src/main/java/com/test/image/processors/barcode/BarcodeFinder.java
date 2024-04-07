package com.test.image.processors.barcode;

import com.test.image.model.GrayScaleImage;
import com.test.image.util.GrayScaleImageHelper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Test implementation of a barcode scanning algorithm described in
 *
 * https://www.ijser.org/researchpaper/Robust-Algorithm-for-Developing-Barcode-Recognition-System-using-Web-cam.pdf
 *
 *
 */
public final class BarcodeFinder {
    private static final File OUTPUT_FILE = new File("/Users/thanos/Documents/Photos/Images/processed/barcode-area.png");

    public static void main(String[] args) throws Exception {
        final BarcodeFinder finder = new BarcodeFinder();
        final BufferedImage photo = ImageIO.read(new File("/Users/thanos/Documents/Photos/Images/raw/barcode.png"));

        OUTPUT_FILE.delete();
        finder.findBarcode(photo);
    }

    private final BarcodeFunctions functions = new BarcodeFunctions();

    public void findBarcode(BufferedImage photo) {
        final GrayScaleImage data = new GrayScaleImage(photo);
        final GrayScaleImage gradient = functions.gradient(data);
        final GrayScaleImage smooth = functions.smooth(gradient);
        final GrayScaleImage binary = functions.binary(smooth);
        debug(binary);
    }

    private void debug(GrayScaleImage data) {
        try {
            final BufferedImage debug = GrayScaleImageHelper.toBufferedImage(data);
            ImageIO.write(debug, "png", OUTPUT_FILE);
            System.out.println("Barcode finder output written to " + OUTPUT_FILE.getAbsolutePath());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
