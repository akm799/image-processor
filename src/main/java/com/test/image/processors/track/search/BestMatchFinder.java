package com.test.image.processors.track.search;

import com.test.image.processors.window.Window;

import java.awt.image.BufferedImage;

public interface BestMatchFinder {

    Window findBestMatch(BufferedImage targetImage, Window targetWindow, BufferedImage image);
}
