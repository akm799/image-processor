package com.test.image.processors.corner;

import com.test.image.model.GradientProducts;
import com.test.image.model.Gradients;
import com.test.image.model.GrayScaleImage;
import com.test.image.model.ImageMetaData;

/**
 * Harris corner detection implementation where no image smoothing (Gaussian or other) is applied.
 * This should worked on images where adge detection has already been applied.
 *
 * http://www.cse.psu.edu/~rtc12/CSE486/lecture06.pdf
 */
public final class HarrisCornerScoreEvaluator implements CornerScoreEvaluator {
    private final float k;

    public HarrisCornerScoreEvaluator(float k) {
        this.k = k;
    }

    @Override
    public ImageMetaData evaluateCornerScores(GrayScaleImage image, Gradients gradients) {
        final ImageMetaData scores = new ImageMetaData(gradients.width, gradients.height);

        final int[] h = new int[3]; // 2x2 Harris matrix is symmetric for the (0,1) and (1,0) elements so we use one value to represent both.
        final GradientProducts products = new GradientProducts(gradients);
        for (int j=0 ; j<products.height ; j++) {
            for (int i=0 ; i<products.width ; i++) {
                evaluateMatrix(i, j, image, products, h);
                final int trace = trace(h);
                final float r = det(h) - k*trace*trace;
                scores.setPixelMetaData(i, j, r);
            }
        }

        return scores;
    }

    private void evaluateMatrix(int i, int j, GrayScaleImage image, GradientProducts products, int[] h) {
        final int v = image.getPixel(i, j);
        h[0] = v*products.horizontalSquared.getPixel(i, j); // (0, 0) value
        h[1] = v*products.mixedProduct.getPixel(i, j);      // (0, 1) and (1, 0) values, since they are equal
        h[2] = v*products.verticalSquared.getPixel(i, j);   // (1, 1) value
    }

    private int det(int[] h) {
        return h[0]*h[2] - 2*h[1];
    }

    private int trace(int[] h) {
        return h[0] + h[2];
    }
}
