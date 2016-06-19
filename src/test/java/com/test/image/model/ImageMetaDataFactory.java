package com.test.image.model;

public class ImageMetaDataFactory {

    public static final ImageMetaData instance(float[][] pixelMetaData) {
        final ImageMetaData metaData = new ImageMetaData(pixelMetaData[0].length , pixelMetaData.length);
        for (int j=0 ; j<pixelMetaData.length ; j++) {
            for (int i=0 ; i<pixelMetaData[j].length ; i++) {
                metaData.setPixelMetaData(i, j, pixelMetaData[j][i]);
            }
        }

        return metaData;
    }

    private ImageMetaDataFactory() {}
}
