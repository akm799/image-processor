package com.test.image.processors.edge;

import com.test.image.model.Gradients;
import com.test.image.model.GrayScaleImage;

public interface ImageGradientsDataProcessor {

    GrayScaleImage processGradients(Gradients gradients);
}
