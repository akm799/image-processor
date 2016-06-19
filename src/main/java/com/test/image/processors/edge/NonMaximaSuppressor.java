package com.test.image.processors.edge;

import com.test.image.model.Direction;
import com.test.image.model.Gradients;
import com.test.image.model.GrayScaleImage;
import com.test.image.model.ImageMetaData;

public final class NonMaximaSuppressor implements ImageGradientsDataProcessor {

    NonMaximaSuppressor() {}

    @Override
    public GrayScaleImage processGradients(Gradients gradients) {
        final GrayScaleImage suppressedNonMaximum = new GrayScaleImage(gradients.magnitudes.getWidth(), gradients.magnitudes.getHeight());
        suppressNonMaxima(gradients, suppressedNonMaximum);

        return suppressedNonMaximum;
    }

    private void suppressNonMaxima(Gradients gradients, GrayScaleImage outputImage) {
        final ImageMetaData magnitudes = gradients.magnitudes;
        final ImageMetaData directions = gradients.directions;

        final int width = outputImage.getWidth();
        final int height = outputImage.getHeight();
        for (int j=0 ; j<height ; j++) {
            for (int i=0 ; i<width ; i++) {
                final Direction direction = Direction.forAngle(directions.getPixelMetaData(i, j));
                if (direction != null) {
                    boolean positiveDirectionResult;
                    final int xp = i + direction.xOffset;
                    final int yp = j + direction.yOffset;
                    if (magnitudes.isInRange(xp, yp)) {
                        positiveDirectionResult = (magnitudes.getPixelMetaData(i, j) >= magnitudes.getPixelMetaData(xp, yp));
                    } else {
                        positiveDirectionResult = true;
                    }

                    boolean negativeDirectionResult;
                    final int xn = i - direction.xOffset;
                    final int yn = j - direction.yOffset;
                    if (magnitudes.isInRange(xn, yn)) {
                        negativeDirectionResult = (magnitudes.getPixelMetaData(i, j) >= magnitudes.getPixelMetaData(xn, yn));
                    } else {
                        negativeDirectionResult = true;
                    }

                    final boolean keep = (positiveDirectionResult && negativeDirectionResult);
                    if (keep) {
                        outputImage.setPixel(i, j, Math.round(magnitudes.getPixelMetaData(i, j)));
                    } else {
                        outputImage.setPixel(i, j, 0);
                    }
                }
            }
        }
    }
}
