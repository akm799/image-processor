package com.test.image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public abstract class AbstractFileImageProcessor implements FileImageProcessor, ImageProcessor{
    private final NumberFormat numberFormat = new DecimalFormat("#.###");

    @Override
    public File processImage(File imageFile, String outputFilePath) throws IOException {
        final String outputImageType = getImageTypeForImageFile(outputFilePath);
        final BufferedImage image = ImageIO.read(imageFile);

        final long t0 = System.currentTimeMillis();
        final BufferedImage processedImage = processImage(image);
        final long duration = System.currentTimeMillis() - t0;
        System.out.println(getDescription() + " Duration " + numberFormat.format(duration/1000.0) + " seconds.");

        final File processedImageFile = new File(outputFilePath);
        ImageIO.write(processedImage, outputImageType, processedImageFile);

        return processedImageFile;
    }

    private String getImageTypeForImageFile(String outputFilePath) {
        final int dotIndex = outputFilePath.lastIndexOf('.');
        if (dotIndex == -1 || dotIndex == outputFilePath.length() - 1) {
            throw new IllegalArgumentException("Invalid output image file path (without extension): " + outputFilePath);
        }

        return outputFilePath.substring(dotIndex + 1);
    }
}
