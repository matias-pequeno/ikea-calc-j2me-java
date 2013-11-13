/*
 * PaintExtenderForm.java
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

import calculator.PaintCalculator;
import calculator.Calculator;
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
public class PaintExtenderForm extends Element
{
    // Screen
    private final int startX = (Shell.m_nMarginX << 2) + Shell.m_nMarginX;
    private int page;

    /**
     * Constructor
     *
     * @param page
     */
    public PaintExtenderForm(int page)
    {
        super();
        this.page = page;
    }

    /****
     *  Initialize this element
     *
     * @param none
     */
    public void initialize()
    {
        Gfx.loadPackage(Gfx.pkgDemoForm);
        setSoftButtons(Shell.m_nIDBack, (page == 0?Shell.m_nIDMore:Shell.m_nHidden));
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
        g.drawImage(Gfx.imgCalculatorSubtitle.image, 0, y, Graphics.TOP|Graphics.LEFT);
        y += Gfx.imgCalculatorSubtitle.image.getHeight() + (Shell.m_nMarginY << 1);

        // Draw sub subtitle
        Font.set(Font.VERDANA_MEDIUM_BOLD_GRAY);
        Font.drawMultiline(g, "Diluyente a utilizar", 0, y, Shell.m_nScreenWidth - (Shell.m_nMarginX << 1), Font.FONT_ALIGN_CENTER);
        y += Font.getHeight() + Shell.m_nMarginY;

        if( page == 0 )
        {
            // Draw sub sub subtitle
            Font.set(Font.VERDANA_SMALL_BOLD_GRAY);
            Font.drawMultiline(g, "Tipo de diluyente según pintura", 0, y, Shell.m_nScreenWidth - (Shell.m_nMarginX << 1), Font.FONT_ALIGN_CENTER);
            y += Font.getHeight() << 1;

            // Table
            // Draw text Tipo de Pintura and Rendimiento
            String []s = {"Pintura", "Tipo de diluyente"};
            int width = Font.len(s[1]);
            int rightX = Shell.m_nScreenWidth - width - Shell.m_nMarginX;
            Font.drawMultiline(g, s[0], startX, y, width, Font.FONT_ALIGN_LEFT);
            Font.drawMultiline(g, s[1], rightX, y, width, Font.FONT_ALIGN_LEFT);
            // Line
            y += (Font.getHeight() << 1) + (Shell.m_nMarginY >> 1);
            g.setColor(0xCDCDCD);
            g.drawLine(0, y, Shell.m_nScreenWidth, y);
            y += (Shell.m_nMarginY >> 1);
            // Text
            Font.set(Font.VERDANA_SMALL_GRAY);
            Font.draw(g, "Esmalte al agua", startX, y, Font.FONT_ALIGN_LEFT);
            Font.draw(g, "Agua", rightX, y, Font.FONT_ALIGN_LEFT);
            // Line
            y += Font.getHeight() + (Shell.m_nMarginY >> 1);
            g.drawLine(0, y, Shell.m_nScreenWidth, y);
            y += (Shell.m_nMarginY >> 1);
            // Text
            Font.draw(g, "Látex", startX, y, Font.FONT_ALIGN_LEFT);
            Font.draw(g, "Agua", rightX, y, Font.FONT_ALIGN_LEFT);
            // Line
            y += Font.getHeight() + (Shell.m_nMarginY >> 1);
            g.drawLine(0, y, Shell.m_nScreenWidth, y);
            y += (Shell.m_nMarginY >> 1);
            // Text
            Font.draw(g, "Esmalte sintético", startX, y, Font.FONT_ALIGN_LEFT);
            Font.draw(g, "Aguarrás", rightX, y, Font.FONT_ALIGN_LEFT);
            // Line
            y += Font.getHeight() + (Shell.m_nMarginY >> 1);
            g.drawLine(0, y, Shell.m_nScreenWidth, y);
            y += (Shell.m_nMarginY >> 1);
            // Text
            Font.draw(g, "Óleo", startX, y, Font.FONT_ALIGN_LEFT);
            Font.draw(g, "Aguarrás", rightX, y, Font.FONT_ALIGN_LEFT);
            // Line
            y += Font.getHeight() + (Shell.m_nMarginY >> 1);
            g.drawLine(0, y, Shell.m_nScreenWidth, y);
            y += (Shell.m_nMarginY >> 1);

        }
        else if( page == 1 )
        {
            // Draw sub sub subtitle
            Font.set(Font.VERDANA_SMALL_BOLD_GRAY);
            Font.drawMultiline(g, "Cant. diluyente según herramienta", 0, y, Shell.m_nScreenWidth - (Shell.m_nMarginX << 1), Font.FONT_ALIGN_CENTER);
            y += Font.getHeight() << 1;

            int w = Shell.m_nScreenWidth - (startX << 1);
            Font.draw(g, "Brocha o rodillo", startX, y, Font.FONT_ALIGN_LEFT);
            y += Font.getHeight();
            Font.set(Font.VERDANA_SMALL_GRAY);
            Font.drawMultiline(g, "5% de diluyente respecto cantidad total de pintura", startX, y, w, Font.FONT_ALIGN_LEFT);
            y += Font.getHeight() * (Font.scrollLines + 1);

            Font.set(Font.VERDANA_SMALL_BOLD_GRAY);
            Font.draw(g, "Pistola", startX, y, Font.FONT_ALIGN_LEFT);
            y += Font.getHeight();
            Font.set(Font.VERDANA_SMALL_GRAY);
            Font.drawMultiline(g, "10% de diluyente respecto cantidad total de pintura", startX, y, w, Font.FONT_ALIGN_LEFT);
        }
    }

}
