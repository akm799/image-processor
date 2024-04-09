package com.test.image.processors.barcode;

import com.test.image.model.GrayScaleImage;
import org.junit.Assert;
import org.junit.Test;

public class BarcodeFunctionsTest {
    private final boolean debugPrint = false;

    private final BarcodeFunctions underTest = new BarcodeFunctions();

    @Test
    public void shouldFindSquareBlobWhenStartingFromTheCentre() {
        rectangularBlobTest(9, 9, 3, 3, 3, 3, 4, 4);
    }

    @Test
    public void shouldFindSquareBlobWhenStartingFromTheEdge() {
        rectangularBlobTest(11, 11, 3, 3, 3, 3, 3, 3);
    }

    @Test
    public void shouldFindRectangularBlobWhenStartingFromTheCentre() {
        rectangularBlobTest(9, 9, 3, 3, 3, 5, 4, 6);
    }

    @Test
    public void shouldFindRectangularBlobWhenStartingFromTheEdge() {
        rectangularBlobTest(11, 11, 3, 3, 3, 5, 5, 7);
    }

    private void rectangularBlobTest(int width, int height, int blobLeft, int blobTop, int blobWidth, int blobHeight, int x0, int y0) {
        final GrayScaleImage image = imageWithRectangularBlob(width, height, blobLeft, blobTop, blobWidth, blobHeight);
        final GrayScaleImage processed = underTest.markBlob(image, x0, y0, 10, 127);

        if (debugPrint) {
            printBinary(image);
            printBinary(processed);
        }

        Assert.assertNotNull(processed);
        assertBlob(processed, blobLeft, blobTop, blobWidth, blobHeight);
    }

    private GrayScaleImage imageWithRectangularBlob(int width, int height, int blobLeft, int blobTop, int bloWidth, int blobHeight) {
        final GrayScaleImage image = new GrayScaleImage(width, height);
        addRectangularBlob(image, blobLeft, blobTop, bloWidth, blobHeight);

        return image;
    }

    private void addRectangularBlob(GrayScaleImage image, int blobLeft, int blobTop, int bloWidth, int blobHeight) {
        final int width  = image.getWidth();
        final int height = image.getHeight();
        final int blobRight = blobLeft + bloWidth;
        final int blobBottom = blobTop + blobHeight;

        for (int j=0 ; j<height; j++) {
            final boolean inVerticalRange = (blobTop <= j && j < blobBottom);
            for (int i=0 ; i<width ; i++) {
                if (inVerticalRange && blobLeft <= i && i < blobRight) {
                    image.setPixel(i, j, 255);
                }
            }
        }
    }

    private void assertBlob(GrayScaleImage image, int blobLeft, int blobTop, int bloWidth, int blobHeight) {
        final int width = image.getWidth();
        final int height = image.getHeight();
        final int blobRight = blobLeft + bloWidth;
        final int blobBottom = blobTop + blobHeight;

        for (int j=0 ; j<height; j++) {
            final boolean inVerticalRange = (blobTop <= j && j < blobBottom);
            for (int i=0 ; i<width ; i++) {
                final int v = image.getPixel(i, j);
                if (inVerticalRange && blobLeft <= i && i < blobRight) {
                    Assert.assertEquals("(" + i + ", " + j + ")=" + v + " and not 127.", 127, v);
                } else {
                    Assert.assertEquals("(" + i + ", " + j + ")=" + v + " and not 0.", 0, v);
                }
            }
        }
    }

    private void printBinary(GrayScaleImage image) {
        final int width = image.getWidth();
        final int height = image.getHeight();

        System.out.print(" ");
        for (int i=0 ; i<width ; i++) { System.out.print('_'); }
        System.out.println();

        for (int j=0 ; j<height; j++) {
            System.out.print('|');
            for (int i=0 ; i<width ; i++) {
                if (image.getPixel(i, j) > 0) {
                    System.out.print(image.getPixel(i, j)/100);
                } else {
                    System.out.print(' ');
                }
            }
            System.out.println('|');
        }

        System.out.print(" ");
        for (int i=0 ; i<width ; i++) { System.out.print('-'); }
        System.out.println();
    }
}
