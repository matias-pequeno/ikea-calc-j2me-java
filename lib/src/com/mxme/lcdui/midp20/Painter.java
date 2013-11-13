package com.mxme.lcdui.midp20;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import com.mxme.lcdui.IPainter;
import com.mxme.common.MXMEConstants;

public class Painter implements IPainter {

    /**
     * 
     */
    public void drawRegion(Object graphics, Object image, int x_src, int y_src,
            int x_dest, int y_dest, int width, int height, int flip) {
        Graphics g = (Graphics)graphics;
        Image srcImg = (Image)image;
        // TODO: flipping
        g.drawRegion(srcImg, x_src, y_src, 
                width, height, 0, x_dest, y_dest, 0);
    }

    /**
     * @see com.matux.lib.common.createFlippedImage(Object, flipValue)
     */
    public Object createFlippedImage(Object image, int x_src, int y_src, 
            int width, int height, byte flipValue) {
        int flip;
        switch (flipValue) {
        case MXMEConstants.FLIP_HORIZONTAL:
            flip = javax.microedition.lcdui.game.Sprite.TRANS_MIRROR;
            break;
        default:
            flip =0;
        break;
        }
        Image aux = (Image)image;
        return Image.createImage(aux, x_src, y_src, 
                width, height, flip); 
    }
}
