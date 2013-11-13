package com.mxme.lcdui;

public interface IPainter {
    void drawRegion(Object graphics, Object image, int x_src, int y_src, 
            int x_dest, int y_dest, int width, int height, int flip);
    Object createFlippedImage(Object image, int x_src, int y_src, 
            int width, int height, byte flipValue);
}
