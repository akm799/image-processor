package com.test.image.processors.barcode;

import com.test.image.model.GrayScaleImage;
import com.test.image.model.Location;
import com.test.image.util.GrayScaleImageHelper;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Test implementation of a barcode scanning algorithm described in
 * <a href="https://www.ijser.org/researchpaper/Robust-Algorithm-for-Developing-Barcode-Recognition-System-using-Web-cam.pdf">Barcode Scanner Algorithm Reference.</a>
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
        final GrayScaleImage smaller = functions.scaleDown(data);
        final GrayScaleImage gradient = functions.gradient(smaller);
        final Location max = functions.max(gradient);
        final GrayScaleImage smooth = functions.smooth(gradient);
        final GrayScaleImage binary = functions.binary(smooth, 10);
        final Rectangle blobBox = functions.findBlobBox(binary, max.x(), max.y(), 10);
        // Consider cutting only a smaller central portion of the blob box/rectangle to eliminate possible surrounding noise such s nearby letters.
        final GrayScaleImage cut = smaller.cut(blobBox);

        if (cut != null) {
            final GrayScaleImage contrast = functions.contrast(cut);
            final GrayScaleImage barcodeBinary = functions.binary(contrast, 127);
            final GrayScaleImage ideal = functions.ideal(barcodeBinary);
            debug(ideal);
        } else {
            debug(data);
        }
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
