package com.test.image.processors.corner.moravec;

import com.test.image.model.GrayScaleImage;

public interface CornerScoreEvaluator {

    GrayScaleImage evaluateCornerScores(GrayScaleImage image);
}
