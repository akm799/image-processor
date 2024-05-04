package com.test.image.util;

public class ColourHelper {
    private static final int MAX_RGB_COMPONENT = 255;
    private static final int MAX_ALPHA = 255;
    private static final byte MAX_ALPHA_BYTE = (byte)MAX_ALPHA;

    private static final int RED_INDEX = 0;
    private static final int GREEN_INDEX = 1;
    private static final int BLUE_INDEX = 2;

    public static boolean isValidRgbColour(int rgb) {
        final int red = getRed(rgb);
        final int green = getGreen(rgb);
        final int blue = getBlue(rgb);

        return isValidRgbComponent(red) && isValidRgbComponent(green) && isValidRgbComponent(blue);
    }

    private static boolean isValidRgbComponent(int rgbComponent) {
        return 0 <= rgbComponent && rgbComponent <= MAX_RGB_COMPONENT;
    }

    public static int getAlpha(int rgb) {
        return (rgb >> 24) & 0xFF;
    }

    public static int getRed(int rgb) {
        return (rgb >> 16) & 0xFF;
    }

    public static int getGreen(int rgb) {
        return (rgb >> 8) & 0xFF;
    }

    public static int getBlue(int rgb) {
        return rgb & 0xFF;
    }

    public static void getRgbValues(int rgb, int[] rgbValues) {
        rgbValues[RED_INDEX]   = (rgb >> 16) & 0xFF;
        rgbValues[GREEN_INDEX] = (rgb >>  8) & 0xFF;
        rgbValues[BLUE_INDEX]  =  rgb        & 0xFF;
    }

    public static void getNormalizedRgbValues(int rgb, int[] rgbValues) {
        final int ri = (rgb >> 16) & 0xFF;
        final int gi = (rgb >>  8) & 0xFF;
        final int bi =  rgb        & 0xFF;
        final float sum = ri + gi + bi;

        rgbValues[RED_INDEX]   = Math.round(MAX_RGB_COMPONENT * ri/sum);
        rgbValues[GREEN_INDEX] = Math.round(MAX_RGB_COMPONENT * gi/sum);
        rgbValues[BLUE_INDEX]  = Math.round(MAX_RGB_COMPONENT * bi/sum);
    }

    public static int getRgb(int red, int green, int blue) {
        return getRgb(red, green, blue, MAX_ALPHA);
    }

    public static int getRgb(int red, int green, int blue, int alpha) {
        return  ((alpha   & 0xFF) << 24) |
                ((red     & 0xFF) << 16) |
                ((green   & 0xFF) <<  8) |
                ((blue    & 0xFF)      );
    }

    public static int toGrayScale(int rgb) {
        return (getRed(rgb) + getGreen(rgb) + getBlue(rgb))/3;
    }

    private ColourHelper() {}
}
