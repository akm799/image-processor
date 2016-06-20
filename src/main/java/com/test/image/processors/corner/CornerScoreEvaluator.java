package com.test.image.processors.corner;

import com.test.image.model.Gradients;
import com.test.image.model.GrayScaleImage;
import com.test.image.model.ImageMetaData;

public interface CornerScoreEvaluator {

    ImageMetaData evaluateCornerScores(GrayScaleImage image, Gradients gradients);
}
