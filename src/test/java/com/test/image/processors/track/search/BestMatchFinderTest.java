package com.test.image.processors.track.search;

import com.test.image.processors.window.Window;
import org.junit.Assert;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Thanos Mavroidis on 29/07/2019.
 */
public class BestMatchFinderTest {
    private final int pixelTolerance = 2;
    private final BestMatchFinder underTest = new BestMatchFinder();

    @Test
    public void shouldFindBestMatch() throws IOException {
        final BufferedImage image = ImageIO.read(new File("./src/test/resources/images/gato.jpg"));
        final Window targetWindow = new Window(new Rectangle(530, 225, 54, 60));

        final Window bestMatchWindow = underTest.findBestMatch(image, targetWindow);
        Assert.assertNotNull(bestMatchWindow);
        Assert.assertEquals(targetWindow.width, bestMatchWindow.width);
        Assert.assertEquals(targetWindow.height, bestMatchWindow.height);
        Assert.assertTrue(Math.abs(bestMatchWindow.xMin - targetWindow.xMin) <= pixelTolerance);
        Assert.assertTrue(Math.abs(bestMatchWindow.yMin - targetWindow.yMin) <= pixelTolerance);
    }
}
