package com.test.image.processors.track.search;

import com.test.image.processors.window.Window;
import org.junit.Assert;
import org.junit.Test;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Created by Thanos Mavroidis on 28/07/2019.
 */
public class WindowsIteratorTest {

    @Test
    public void shouldIterateWithExactFit() {
        final int windowWidth = 5;
        final int windowHeight = 5;
        final BufferedImage image = buildTestImage(10, 10);
        final Window window = buildWindow(3, 3, windowWidth, windowHeight);

        final Collection<Window> expected = new ArrayList(4);
        expected.add(buildWindow(0, 0, windowWidth, windowHeight));
        expected.add(buildWindow(5, 0, windowWidth, windowHeight));
        expected.add(buildWindow(0, 5, windowWidth, windowHeight));
        expected.add(buildWindow(5, 5, windowWidth, windowHeight));

        final Iterator<Window> underTest = new WindowsIterator(image, window);
        testWindowIteration(expected, underTest);
    }

    @Test
    public void shouldIterateWithInexactXFit() {
        final int windowWidth = 5;
        final int windowHeight = 5;
        final BufferedImage image = buildTestImage(12, 10);
        final Window window = buildWindow(3, 3, windowWidth, windowHeight);

        final Collection<Window> expected = new ArrayList(6);
        expected.add(buildWindow(0, 0, windowWidth, windowHeight));
        expected.add(buildWindow(5, 0, windowWidth, windowHeight));
        expected.add(buildWindow(7, 0, windowWidth, windowHeight));
        expected.add(buildWindow(0, 5, windowWidth, windowHeight));
        expected.add(buildWindow(5, 5, windowWidth, windowHeight));
        expected.add(buildWindow(7, 5, windowWidth, windowHeight));

        final Iterator<Window> underTest = new WindowsIterator(image, window);
        testWindowIteration(expected, underTest);
    }

    @Test
    public void shouldIterateWithInexactYFit() {
        final int windowWidth = 5;
        final int windowHeight = 5;
        final BufferedImage image = buildTestImage(10, 12);
        final Window window = buildWindow(3, 3, windowWidth, windowHeight);

        final Collection<Window> expected = new ArrayList(6);
        expected.add(buildWindow(0, 0, windowWidth, windowHeight));
        expected.add(buildWindow(5, 0, windowWidth, windowHeight));
        expected.add(buildWindow(0, 5, windowWidth, windowHeight));
        expected.add(buildWindow(5, 5, windowWidth, windowHeight));
        expected.add(buildWindow(0, 7, windowWidth, windowHeight));
        expected.add(buildWindow(5, 7, windowWidth, windowHeight));

        final Iterator<Window> underTest = new WindowsIterator(image, window);
        testWindowIteration(expected, underTest);
    }

    @Test
    public void shouldIterateWithInexactXYFit() {
        final int windowWidth = 5;
        final int windowHeight = 5;
        final BufferedImage image = buildTestImage(12, 12);
        final Window window = buildWindow(3, 3, windowWidth, windowHeight);

        final Collection<Window> expected = new ArrayList(9);
        expected.add(buildWindow(0, 0, windowWidth, windowHeight));
        expected.add(buildWindow(5, 0, windowWidth, windowHeight));
        expected.add(buildWindow(7, 0, windowWidth, windowHeight));
        expected.add(buildWindow(0, 5, windowWidth, windowHeight));
        expected.add(buildWindow(5, 5, windowWidth, windowHeight));
        expected.add(buildWindow(7, 5, windowWidth, windowHeight));
        expected.add(buildWindow(0, 7, windowWidth, windowHeight));
        expected.add(buildWindow(5, 7, windowWidth, windowHeight));
        expected.add(buildWindow(7, 7, windowWidth, windowHeight));

        final Iterator<Window> underTest = new WindowsIterator(image, window);
        testWindowIteration(expected, underTest);
    }

    private BufferedImage buildTestImage(int width, int height) {
        return new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }

    private Window buildWindow(int left, int top, int width, int height) {
        final Rectangle rectangle = new Rectangle();
        rectangle.x = left;
        rectangle.y = top;
        rectangle.width = width;
        rectangle.height = height;

        return new Window(rectangle);
    }

    private void testWindowIteration(Collection<Window> expected, Iterator<Window> underTest) {
        final Iterator<Window> iterator = expected.iterator();

        while (iterator.hasNext()) {
            Assert.assertTrue(underTest.hasNext());
            assertEquals(iterator.next(), underTest.next());
        }
    }

    private void assertEquals(Window expected, Window actual) {
        Assert.assertNotNull(actual);
        Assert.assertEquals(expected.xMin, actual.xMin);
        Assert.assertEquals(expected.width, actual.width);
        Assert.assertEquals(expected.yMin, actual.yMin);
        Assert.assertEquals(expected.height, actual.height);
    }
}
