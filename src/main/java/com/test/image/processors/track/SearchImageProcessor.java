package com.test.image.processors.track;

import com.test.image.AbstractFileImageProcessor;
import com.test.image.ImageProcessor;
import com.test.image.processors.track.search.BestMatchFinder;
import com.test.image.processors.window.ColouredWindow;
import com.test.image.processors.window.Window;
import com.test.image.processors.window.WindowImageProcessor;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Collection;

/**
 * Created by Thanos Mavroidis on 28/07/2019.
 */
public final class SearchImageProcessor extends AbstractFileImageProcessor {
    private final int bestMatchColour;
    private final ColouredWindow targetWindow;

    public SearchImageProcessor(ColouredWindow targetWindow, int bestMatchColour) {
        this.targetWindow = targetWindow;
        this.bestMatchColour = bestMatchColour;
    }

    @Override
    public String getDescription() {
        return "Found best match with the input window.";
    }

    @Override
    public BufferedImage processImage(BufferedImage image) {
        final Window bestMatchWindow = (new BestMatchFinder()).findBestMatch(image, targetWindow, image);
        final Collection<ColouredWindow> windows = Arrays.asList(targetWindow, new ColouredWindow(bestMatchWindow, bestMatchColour));
        final ImageProcessor windowImageProcessor = new WindowImageProcessor(windows);

        return windowImageProcessor.processImage(image);
    }
}
