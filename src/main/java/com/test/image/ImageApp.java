package com.test.image;

import com.test.image.processors.blur.ColourGaussianBlurProcessor;
import com.test.image.processors.blur.GaussianBlurProcessor;
import com.test.image.processors.composition.Overlay;
import com.test.image.processors.composition.OverlayImageProcessor;
import com.test.image.processors.corner.CornerImageProcessor;
import com.test.image.processors.doc.DocScanImageProcessor;
import com.test.image.processors.edge.EdgeImageProcessor;
import com.test.image.processors.gray.GrayScaleImageProcessor;
import com.test.image.processors.padding.AddPaddingImageProcessor;
import com.test.image.processors.padding.PaddingFactors;
import com.test.image.processors.scale.ScaleDownProcessor;
import com.test.image.processors.sharp.UsmImageProcessor;
import com.test.image.processors.track.IterativeTrackImageProcessor;
import com.test.image.processors.track.TopSimilarityImageProcessor;
import com.test.image.processors.track.TrackImageProcessor;
import com.test.image.processors.var.VariationsImageProcessor;
import com.test.image.processors.window.ColouredWindow;
import com.test.image.processors.window.WindowImageProcessor;
import com.test.image.util.ColourHelper;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

public class ImageApp {

    private ImageApp() {}

    private FileImageProcessor getImageProcessor(String[] args) throws IOException {
        return getTopSimilarityImageProcessor();
    }

    private FileImageProcessor getTopSimilarityImageProcessor() throws IOException {
        final int red = ColourHelper.getRgb(255, 0, 0);
        final int green = ColourHelper.getRgb(0, 255, 0);

        final ColouredWindow targetWindow = new ColouredWindow(new Rectangle(530, 225, 54, 60), green);

        return new TopSimilarityImageProcessor(targetWindow, green, red);
    }

    private FileImageProcessor getTrackImageProcessor() throws IOException {
        final int red = ColourHelper.getRgb(255, 0, 0);
        final int green = ColourHelper.getRgb(0, 255, 0);
        final int blue = ColourHelper.getRgb(0, 0, 255);

        final Point deviation = new Point(-15, -15);
        final ColouredWindow initialWindow = new ColouredWindow(new Rectangle(530, 225, 54, 60), green);
        final ColouredWindow offCentreWindow = new ColouredWindow(new Rectangle(initialWindow.xMin + deviation.x, initialWindow.yMin + deviation.y, initialWindow.width, initialWindow.height), red);

        return new IterativeTrackImageProcessor(initialWindow, offCentreWindow, blue);
    }

    private FileImageProcessor getWindowImageProcessor() throws IOException {
        final int red = ColourHelper.getRgb(255, 0, 0);
        return new WindowImageProcessor(new Rectangle(530, 225, 54, 60), red);
    }

    private FileImageProcessor getDocumentScanImageProcessor() throws IOException {
        return new DocScanImageProcessor();
    }

    private FileImageProcessor getVariationsImageProcessor() throws IOException {
        return new VariationsImageProcessor();
    }

    private FileImageProcessor getSharpImageProcessor() throws IOException {
        return new UsmImageProcessor();
    }

    private FileImageProcessor getCornerImageProcessor() throws IOException {
        return new CornerImageProcessor();
    }

    private FileImageProcessor getEdgeImageProcessor() {
        return new EdgeImageProcessor();
    }

    private FileImageProcessor getScaleDownImageProcessor() {
        return new ScaleDownProcessor();
    }

    private FileImageProcessor getGaussianBlurImageProcessor() {
        return new GaussianBlurProcessor(10, 3.0f);
    }

    private FileImageProcessor getColourGaussianBlurImageProcessor() {
        return new ColourGaussianBlurProcessor(10, 3.0f);
    }

    private FileImageProcessor getAddPaddingImageProcessor() {
        final PaddingFactors paddingFactors = new PaddingFactors(0.25f, 0, 0, 0.5f);
        return new AddPaddingImageProcessor(paddingFactors);
    }

    private FileImageProcessor getCompositionImageProcessor(String[] args) throws IOException {
        final File inputFile = new File(args[0]);
        final BufferedImage baseImage = ImageIO.read(inputFile);
        final String overlayFile = "warning.png";
        final BufferedImage overlayImage = ImageIO.read(new File(inputFile.getParentFile(), overlayFile));
        
        final int left = baseImage.getWidth() - Math.round(overlayImage.getWidth()*0.57692308f);
        final int top = -Math.round(overlayImage.getHeight()*0.42307692f);
        final Overlay overlay = new Overlay(overlayImage, left, top);
        final Collection<Overlay> overlays = Arrays.asList(overlay);
        return new OverlayImageProcessor(overlays, true);
    }

    private FileImageProcessor getGrayScaleImageProcessor() {
        return new GrayScaleImageProcessor();
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.err.println("Invalid arguments.");
            System.out.println("com.test.image.ImageApp <input_image_file_path> <output_image_file_path>");
            System.exit(1);
        }

        final ImageApp imageApp = new ImageApp();
        final File output = imageApp.getImageProcessor(args).processImage(new File(args[0]), args[1]);
        System.out.println("Output written to " + output.getAbsolutePath());
    }
}
