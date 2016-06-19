package com.test.image;

import com.test.image.processors.edge.EdgeImageProcessor;

import java.io.File;
import java.io.IOException;

public final class ImageAppBatch {

    private ImageAppBatch() {}

    private FileImageProcessor getImageProcessor(String[] args) throws IOException {
        return new EdgeImageProcessor();
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.err.println("Invalid arguments.");
            System.out.println("com.test.image.ImageAppBatch <input_image_folder_path> <output_image_folder_path>");
            System.exit(1);
        }

        final File inputDir = new File(args[0]);
        final File outputDir = new File(args[1]);
        if (!inputDir.isDirectory() || !outputDir.isDirectory()) {
            System.err.println("Invalid arguments.");
            System.out.println("com.test.image.ImageAppBatch <input_image_folder_path> <output_image_folder_path>");
            System.err.println("<input_image_folder_path> and/or <output_image_folder_path> are not existing, valid directories.");
            System.exit(1);
        }

        int nProcessed = 0;
        final ImageAppBatch imageAppBatch = new ImageAppBatch();
        final FileImageProcessor fileImageProcessor = imageAppBatch.getImageProcessor(args);
        for (File input : inputDir.listFiles()) {
            final String outputFilePath = buildOutputFile(input, outputDir);
            final File output = fileImageProcessor.processImage(input, outputFilePath);
            System.out.println("Output written to " + output.getAbsolutePath());
            nProcessed++;
        }
        System.out.println("Processed " + nProcessed + " files and written the output to " + outputDir);
    }

    private static String buildOutputFile(File input, File outputDir) {
        final File output = new File(outputDir, input.getName());
        return output.getAbsolutePath();
    }
}
