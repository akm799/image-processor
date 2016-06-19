package com.test.image.processors.edge;

import com.test.image.model.Gradients;
import com.test.image.model.GrayScaleImage;

/**
 * Gradients processor that simply copies the gradient magnitudes to an output image without processing them in any way.
 */
public class IdentityGradientsProcessor implements ImageGradientsDataProcessor {

    IdentityGradientsProcessor() {}

    @Override
    public GrayScaleImage processGradients(Gradients gradients) {
        final GrayScaleImage outputImage = new GrayScaleImage(gradients.magnitudes.getWidth(), gradients.magnitudes.getHeight());
        for (int j=0 ; j<outputImage.getHeight() ; j++) {
            for (int i=0 ; i<outputImage.getWidth() ; i++) {
                outputImage.setPixel(i, j, Math.round(gradients.magnitudes.getPixelMetaData(i, j)));
            }
        }

        return outputImage;
    }
}
