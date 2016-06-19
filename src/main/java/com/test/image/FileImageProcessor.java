package com.test.image;

import java.io.File;
import java.io.IOException;

public interface FileImageProcessor {

    File processImage(File imageFile, String outputFilePath) throws IOException;
}
