package com.test.image.processors.track.search;

import com.test.image.processors.track.search.impl.BasicBestMatchFinder;
import com.test.image.processors.track.search.impl.RecyclingBestMatchFinder;
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

    @Test
    public void shouldFindBestMatchWithBasicFinder() throws IOException {
        testBestMatch(new BasicBestMatchFinder());
    }

    @Test
    public void shouldFindBestMatchWithRecyclingFinder() throws IOException {
        testBestMatch(new RecyclingBestMatchFinder());
    }

    private void testBestMatch(BestMatchFinder underTest) throws IOException {
        final BufferedImage targetImage = ImageIO.read(new File("./src/test/resources/images/gato.jpg"));
        final Window targetWindow = new Window(new Rectangle(530, 225, 54, 60));
        final BufferedImage image = targetImage;

        final Window bestMatchWindow = underTest.findBestMatch(targetImage, targetWindow, image);
        Assert.assertNotNull(bestMatchWindow);
        Assert.assertEquals(targetWindow.width, bestMatchWindow.width);
        Assert.assertEquals(targetWindow.height, bestMatchWindow.height);
        Assert.assertTrue(Math.abs(bestMatchWindow.xMin - targetWindow.xMin) <= pixelTolerance);
        Assert.assertTrue(Math.abs(bestMatchWindow.yMin - targetWindow.yMin) <= pixelTolerance);
    }
}
