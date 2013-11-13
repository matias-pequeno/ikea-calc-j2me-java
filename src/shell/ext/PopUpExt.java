/*
 * PopUpExt.java
 *
 * Created on Dec 29, 2008, 8:26:58 PM
 *
 * Copyright (C) 2008 Oceanic Images Studios, Inc.  All Rights Reserved.
 * This software is the proprietary information of Oceanic Images Studios.
 *
 */

package shell.ext;

// Java library
import javax.microedition.lcdui.Graphics;
// MatuX library
import com.mxme.shell.core.PopUp;
import com.mxme.shell.Shell;

import util.Font;
import util.Text;

/**
 * Author: MatuX
 * Date: Dec 29, 2008
 * Current revision: 0
 * Last modified: Dec 29, 2008
 * By: MatuX
 * Reviewers:
 *
 */
public class PopUpExt extends PopUp
{
    /**
     * Constructor
     *
     */
    public PopUpExt(int title, int text)
    {
        super(title, text);
    }

    /****
     * Paint the element.<br>
     *  <br>
     * All drawing assumes we have a big enough Clip to draw what we need.
     * This method also draws a standard layout that can be overriden in the childs.
     *
     * @param g, Graphics object
     */
    public void paint(Graphics g)
    {
        String text = Text.get(m_text);
        int boxLenX = Font.len(text) + (Shell.m_nMarginX << 2);
        int boxLenY = (Font.getHeight() << 1) + (Shell.m_nMarginY << 1);
        int boxX = Shell.m_nScreenWidthHalf - (boxLenX >> 1);
        int boxY = Shell.m_nScreenHeightHalf - (boxLenY >> 1);

        g.setColor(0xFFFFFF);
        g.fillRect(boxX, boxY, boxLenX, boxLenY);
        g.setColor(0);
        g.drawRect(boxX + Shell.m_nMarginX, boxY + Shell.m_nMarginY, boxLenX - (Shell.m_nMarginX << 1), boxLenY - (Shell.m_nMarginY << 1));

        Font.draw(g, text, Shell.m_nScreenWidthHalf, Shell.m_nScreenHeightHalf, Font.FONT_ALIGN_CENTER|Font.FONT_ALIGN_VCENTER);
    }

}
