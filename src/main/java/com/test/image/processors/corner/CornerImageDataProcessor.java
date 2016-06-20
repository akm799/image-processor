package com.test.image.processors.corner;

import com.test.image.ImageDataProcessor;
import com.test.image.model.Constants;
import com.test.image.model.Gradients;
import com.test.image.model.GrayScaleImage;
import com.test.image.model.ImageMetaData;
import com.test.image.processors.edge.EdgeImageDataProcessor;

public final class CornerImageDataProcessor implements ImageDataProcessor {
    private final CornerConfig config = new CornerConfig();
    private final EdgeImageDataProcessor edgeProcessor = new EdgeImageDataProcessor();
    private final CornerScoreEvaluator cornerScoreEvaluator = new HarrisCornerScoreEvaluator(config.k);

    @Override
    public GrayScaleImage processImage(GrayScaleImage image) {
        final GrayScaleImage edgeImage = edgeProcessor.processImage(image);
        final Gradients gradients = edgeProcessor.getGradients();
        final ImageMetaData cornerScores = cornerScoreEvaluator.evaluateCornerScores(edgeImage, gradients);

        return buildCornersOnlyImage(cornerScores);
    }

    private GrayScaleImage buildCornersOnlyImage(ImageMetaData cornerScores) {
        final GrayScaleImage cornersOnly = new GrayScaleImage(cornerScores.getWidth(), cornerScores.getHeight());
        //TODO Perform non-max suppression at this point (?).
        for (int j=0 ; j<cornersOnly.getHeight() ; j++) {
            for (int i=0 ; i<cornersOnly.getWidth() ; i++) {
                if (cornerScores.getPixelMetaData(i, j) >= config.rThreshold) {
                    cornersOnly.setPixel(i, j, Constants.MAX_INTENSITY);
                } else {
                    cornersOnly.setPixel(i, j, 0);
                }
            }
        }

        return cornersOnly;
    }
}
