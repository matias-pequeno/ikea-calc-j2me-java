/*
 * PaintYieldForm.java
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
import util.Text;

/**
 * Author: MatuX
 * Date: Mar 9, 2009
 * Current revision: 0
 * Last modified: Mar 9, 2009
 * By: MatuX
 * Reviewers:
 *
 */
public class PaintYieldForm extends Element
{
    // Screen
    private final int startX = (Shell.m_nMarginX << 2) + Shell.m_nMarginX;

    public PaintYieldForm()
    {
        m_nTitle = Text.str_CALCULATE;
    }
    
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
        g.drawImage(Gfx.imgCalculatorSubtitle.image, 0, y, Graphics.TOP|Graphics.LEFT);
        y += Gfx.imgCalculatorSubtitle.image.getHeight() + (Shell.m_nMarginY << 1);

        // Draw text Tipo de Pintura and Rendimiento
        Font.set(Font.VERDANA_MEDIUM_BOLD_GRAY);
        String []s = {"Tipo de Pintura", "Litros Requeridos"};
        int titleLen2 = Font.len(s[1]);
        int width = titleLen2 - (titleLen2 >> 2);
        int rightX = Shell.m_nScreenWidth - width;
        Font.drawMultiline(g, s[0], startX, y, width, Font.FONT_ALIGN_LEFT);
        Font.drawMultiline(g, s[1], rightX, y, width, Font.FONT_ALIGN_LEFT);
        y += (Font.getHeight() << 1) + Shell.m_nMarginY;
        
        // Draw the information
        Font.set(Font.VERDANA_SMALL_GRAY);
        g.setColor(0xCDCDCD);
        g.drawLine(0, y, Shell.m_nScreenWidth, y);
        y += Shell.m_nMarginY >> 1;
        String []pT = {"Esmalte al agua","Látex","Esmalte sintético","Óleo"};
        int []rend = {12000,10000,13000,12000};
        for( int i = 0; i < 4; i++ )
        {
            Font.draw(g, pT[i], startX, y, Font.FONT_ALIGN_LEFT);
            // Calculate
            int yield = Calculator.paintCalculator.calculateCM2() / rend[i];
            int yieldComma = (Calculator.paintCalculator.calculateCM2() % rend[i]) / 1000;
            Font.draw(g, yield + "," + yieldComma + "lts.", rightX, y, Font.FONT_ALIGN_LEFT);
            // Line
            y += Font.getHeight() + (Shell.m_nMarginY >> 1);
            g.drawLine(0, y, Shell.m_nScreenWidth, y);
            // Adjust Y
            y += (Shell.m_nMarginY >> 1);
        }

    }

}
