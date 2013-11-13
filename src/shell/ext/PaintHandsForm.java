/*
 * PaintHandsForm.java
 *
 * Created on Mar 9, 2009, 6:26:56 PM
 *
 * Copyright (C) 2009 XXX  All Rights Reserved.
 * This software is the proprietary information of XXX
 *
 */
package shell.ext;

import javax.microedition.lcdui.Graphics;

import com.mxme.shell.Element;
import com.mxme.shell.Shell;

import util.Font;
import util.Gfx;

/**
 * Author: MatuX
 * Date: Mar 9, 2009
 * Current revision: 0
 * Last modified: Mar 9, 2009
 * By: MatuX
 * Reviewers:
 *
 */
public class PaintHandsForm extends Element
{
    // Screen
    private final int startX = (Shell.m_nMarginX << 2) - Shell.m_nMarginX;
    
    /****
     *  Initialize this element
     *
     * @param none
     */
    public void initialize()
    {
        Gfx.loadPackage(Gfx.pkgDemoForm);
        setSoftButtons(Shell.m_nIDBack, Shell.m_nIDMore);
    }

    /****
     *  DeInitialize this element
     *
     * @param none
     */
    public void deinitialize()
    {
        Gfx.unloadPackage(Gfx.pkgDemoForm);
    }

    /****
     *   Process the element
     *
     * @param int p_nAction, data to be sent to the processing function
     * @return boolean, was the key handled?
     */
    public boolean keyPressed(int p_nAction, int keyCode)
    {
        boolean handled = false;

        return handled;
    }

    public boolean keyReleased(int p_nAction, int keyCode)
    {
        boolean handled = false;

        return handled;
    }

    /****
     * Paint the element
     *
     * All drawing assumes we have a big enough Clip to draw what we need.
     * This method also draws a standard layout that can be overriden in the childs.
     *
     * @param g, Graphics object
     */
    public void paint(Graphics g)
    {
        // Draw our standard layout
        m_standardLayout.draw_standard_layout(g, this);

        // Draw subtitle
        int x = 0;
        int y = Gfx.imgStdTitle.image.getHeight();
        g.drawImage(Gfx.imgCalculatorSubtitle.image, x, y, Graphics.TOP|Graphics.LEFT);
        y += Gfx.imgCalculatorSubtitle.image.getHeight() + (Shell.m_nMarginY << 1);

        // Draw sub subtitle
        Font.set(Font.VERDANA_MEDIUM_BOLD_GRAY);
        Font.drawMultiline(g, "Manos de pintura a considerar", 0, y, Shell.m_nScreenWidth - (Shell.m_nMarginX << 1), Font.FONT_ALIGN_CENTER);
        y += (Font.getHeight() << 1) + Shell.m_nMarginY;

        // Line
        g.setColor(0xCDCDCD);
        g.drawLine(0, y, Shell.m_nScreenWidth, y);
        y += Shell.m_nMarginY >> 1;
        // Setup
        Font.set(Font.VERDANA_SMALL_GRAY);
        int w = Font.len("Pared absorvente o ");
        int rightX = Shell.m_nScreenWidth - Font.len("3 manos o más") - startX;
        // PARED MISMO COLOR
            Font.draw(g, "Pared mismo color", startX, y, Font.FONT_ALIGN_LEFT);
            Font.draw(g, "2 manos", rightX, y, Font.FONT_ALIGN_LEFT);
            // Line
            y += Font.getHeight() + (Shell.m_nMarginY >> 1);
            g.drawLine(0, y, Shell.m_nScreenWidth, y);
            y += (Shell.m_nMarginY >> 1);
        // PARED SIN PINTAR
            Font.draw(g, "Pared sin pintar", startX, y, Font.FONT_ALIGN_LEFT);
            Font.draw(g, "3 manos o más", rightX, y, Font.FONT_ALIGN_LEFT);
            // Line
            y += Font.getHeight() + (Shell.m_nMarginY >> 1);
            g.drawLine(0, y, Shell.m_nScreenWidth, y);
            y += (Shell.m_nMarginY >> 1);
        // PARED ABSORVENTE O IRREGULAR
            Font.drawMultiline(g, "Pared absorvente o irregular", startX, y, w, Font.FONT_ALIGN_LEFT);
            Font.draw(g, "3 manos o más", rightX, y, Font.FONT_ALIGN_LEFT);

    }

}
