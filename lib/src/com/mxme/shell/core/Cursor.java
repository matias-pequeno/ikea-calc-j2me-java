/*
 * Cursor.java
 *
 * Created on Dec 29, 2008, 2:47:23 PM
 *
 * Copyright (C) 2008 XXX. All Rights Reserved.
 * This software is the proprietary information of Oceanic Images Studios.
 *
 */

package com.mxme.shell.core;

import javax.microedition.lcdui.Graphics;

/**
 * Author: MatuX
 * Date: Dec 29, 2008
 * Current revision: 0
 * Last modified: Dec 29, 2008
 * By: MatuX
 * Reviewers:
 *
 */
public class Cursor
{
    // cursor anim
    private int modResult = 0;
    private boolean modResultBackwards = false;
    private static final short CURSOR_SPEED = 250;
    private long cursorTimer;

    public int posX, posY, width, height;
    public int lineSize = 4;
    public boolean animCursor = false;

    /****
     * Enable this element
     *
     * @param none
     *
     */
    public void enable()
    {
        cursorTimer = System.currentTimeMillis();
    }

    public void paint(Graphics g)
    {
        // Anim cursor
        if( animCursor )
        {
            if( System.currentTimeMillis() - cursorTimer > CURSOR_SPEED )
            {
                cursorTimer = System.currentTimeMillis();
                if( modResultBackwards ) modResult--;
                else modResult++;
                System.out.println(modResult);
                if( modResult == lineSize - 2 || modResult == 0 )
                    modResultBackwards = !modResultBackwards;
            }
        }

        boolean drawLine;
        int x, y;
        g.setColor(0);

        //
        // draw horizontal line
        // upper line
        drawLine = false;
        y = posY;
        for( x = posX; x <= (posX + width); ++x )
        {
            if( x % lineSize == modResult ) drawLine = !drawLine;
            if( drawLine ) g.drawLine(x, y, x, y);
        }
        // lower line
        drawLine = false;
        y = posY + height;
        for( x = posX; x <= (posX + width); ++x )
        {
            if( x % lineSize == modResult ) drawLine = !drawLine;
            if( drawLine ) g.drawLine(x, y, x, y);
        }

        //
        // draw vertical line
        // leftmost line
        drawLine = false;
        x = posX;
        for( y = posY; y <= (posY + height); ++y )
        {
            if( y % lineSize == modResult ) drawLine = !drawLine;
            if( drawLine ) g.drawLine(x, y, x, y);
        }
        // rightmost line
        drawLine = false;
        x = posX + width;
        for( y = posY; y <= (posY + height); ++y )
        {
            if( y % lineSize == modResult ) drawLine = !drawLine;
            if( drawLine ) g.drawLine(x, y, x, y);
        }
    }

}
