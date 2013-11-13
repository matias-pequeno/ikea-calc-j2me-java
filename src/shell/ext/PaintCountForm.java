/*
 * CalculatorForm.java
 *
 * Created on Feb 21, 2009, 7:11:13 PM
 *
 * Copyright (C) 2009 XXX  All Rights Reserved.
 * This software is the proprietary information of XXX
 *
 */
package shell.ext;

// Java library
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Canvas;
// MatuX library
import com.mxme.shell.Element;
import com.mxme.shell.Shell;
import com.mxme.shell.core.Button;
// Internal
import app.MainCanvas;
import shell.AppShell;
import shell.StandardLayout;
import calculator.Calculator;
import util.Text;
import util.Font;
import util.Gfx;

/**
 * Author: MatuX
 * Date: Feb 21, 2009
 * Current revision: 0
 * Last modified: Feb 21, 2009
 * By: MatuX
 * Reviewers:
 *
 */
public class PaintCountForm extends Element
{
    // Form components
    private TextBox boxWallAmount;
    private Button buttonContinue;
    private Viewer viewerMiniHelp;

    // Form behavior
    private boolean msgAboveMaxWalls, msgWriteSomething;

    // Cursor
    private Element currentSelection = null;
    private boolean alreadyChanged = false;

    // Screen
    private final int startX = (Shell.m_nScreenWidthHalf >> 2) - (Shell.m_nMarginY >> 1);

    /**
     * Constructor
     *
     */
    public PaintCountForm()
    {
        m_nTitle = Text.str_CALCULATOR_TYPE_PINTURA;
    }

    /****
     * Initialize this element
     *
     * @param none
     *
     */
    public void initialize()
    {
        Gfx.loadPackage(Gfx.pkgDemoForm);
        setSoftButtons(Shell.m_nIDOk, Shell.m_nIDMenu);

        // Init text boxes
        boxWallAmount = new TextBox("", Font.VERDANA_MEDIUM_NUMBERS, Gfx.imgCalculatorTextBox.image);
        boxWallAmount.initialize();
        boxWallAmount.onlyNumbers(true);
        boxWallAmount.maxChars = 2;
        boxWallAmount.setDefaultSoftButtons(Shell.m_nIDMenu, Shell.m_nIDClear);
        currentSelection = boxWallAmount;
        // Init button
        buttonContinue = new Button(Gfx.imgCalculatorButton.image, null, Gfx.imgCalculatorButtonText.image, Gfx.imgCalculatorButtonSel.image);
        buttonContinue.initialize();
        buttonContinue.x = startX + Gfx.imgCalculatorTextBox.image.getWidth() + (Gfx.imgCalculatorButton.image.getWidth() >> 1) + Shell.m_nMarginX;
        //buttonContinue.setDefaultSoftButtons(Shell.m_nIDOk, Shell.m_nIDBack);
        if( Shell.m_nScreenHeight <= 128 )
            buttonContinue.x -= Shell.m_nMarginX * 3;

        alreadyChanged = false;

        // set messages
        msgAboveMaxWalls = false;

        // enable default element
        enableElement(boxWallAmount);

        // Create viewer
        viewerMiniHelp = new Viewer(Text.str_TERMS, Text.str_CANTPAREDESHELP_MINI, Viewer.TYPE_SCROLL);
        viewerMiniHelp.initialize();
    }

    /****
     * Deinitialize this element
     *
     * @param none
     *
     */
    public void deinitialize()
    {
        Gfx.unloadPackage(Gfx.pkgDemoForm);
        boxWallAmount = null;
        buttonContinue = null;
        currentSelection = null;

        alreadyChanged = false;
    }

    /**
     * Paint the element
     *
     * @param g, Graphics object
     *
     */
    public void paint(Graphics g)
    {
        // Set soft buttons
        //setSoftButtons(Shell.m_nIDMenu, currentSelection.m_nRightSoftButton);
        setSoftButtons(currentSelection.m_nLeftSoftButton, currentSelection.m_nRightSoftButton);

        // get standard layout
//#if GFX_RES_ULTRALOW == "true"
//#         g.drawImage(Gfx.imgProfileBack.image, 0, Shell.m_nScreenHeight - 1, Graphics.BOTTOM|Graphics.LEFT);
//#         ((StandardLayout)m_standardLayout).drawTerms(g, this);
//#         m_standardLayout.draw_title(g, this);
//#         m_standardLayout.draw_command_buttons(g, this);
//#else
        m_standardLayout.draw_standard_layout(g, this);
//#endif

        // Draw subtitle
        int y = Gfx.imgStdTitle.image.getHeight();
        g.drawImage(Gfx.imgCalculatorSubtitle.image, 0, y, Graphics.TOP|Graphics.LEFT);
        y += Gfx.imgCalculatorSubtitle.image.getHeight() + (Shell.m_nMarginY << 2);

        // Textbox and Button
        boxWallAmount.x = startX;
        if( Shell.m_nScreenHeight <= 128 )
            boxWallAmount.x -= Shell.m_nMarginX  * 3;
        boxWallAmount.y = y;
        boxWallAmount.paint(g);
        buttonContinue.y = y;
        buttonContinue.paint(g);
        int buttonH = Gfx.imgCalculatorButton.image.getHeight();
        y += buttonH << 1;// + Shell.m_nMarginY;

        // Some general settings
        Font.set(Font.VERDANA_MEDIUM_BOLD_GRAY);
        int x = startX;
        int w = Shell.m_nScreenWidth - (x << 1);
        int lowerBoxH = AppShell.usableMenuHeight() - buttonH - (Shell.m_nMarginY << 1);
//#if GFX_RES_ULTRALOW == "true"
//#         lowerBoxH += Font.getHeight();
//#endif

        // Message boxes
        int intMarginX = Shell.m_nMarginX << 1;
        int boxH = lowerBoxH;
        Font.set(Font.VERDANA_MEDIUM_BOLD_RED);
        int align = Font.FONT_ALIGN_CENTER|Font.FONT_ALIGN_VCENTER;
        int textW = w - intMarginX;
        int textH = boxH;
        int textX = startX;
        int textY = y;
        int boxX = textX;
        // Super ultra hack
        if( Shell.m_nScreenHeight <= 160 )
        {
            textX -= intMarginX * 2;
            textW += intMarginX * 5;
            w = textW;
            boxX = textX;
            textX -= intMarginX * 2;
            textW += intMarginX * 2;
            textH += intMarginX;
        }

        if( msgAboveMaxWalls || msgWriteSomething )
        {
            String s = msgWriteSomething? Text.get(Text.str_WRITE_SOMETHING) : Text.get(Text.str_ABOVE_MAX_WALLS);
            //g.setColor(0xefc108);
            //g.fillRect(boxX, textY, w, boxH);
            Font.drawMultiline(g, s, textX, textY - Font.getHeight(), textW, textH, align);
        }
        else
        {
            // Load code text
            viewerMiniHelp.paintText(g, x, y, w, lowerBoxH);
            y += (Font.getHeight() << 1) + Shell.m_nMarginY;
        }

        // change control
        if( boxWallAmount.isEnabled && boxWallAmount.getCurPos() >= boxWallAmount.maxChars && !alreadyChanged )
        {
            keyPressed(-1, Canvas.FIRE);
            alreadyChanged = true;
        }

    }

    /**
     * Process the element
     *
     * @param none
     *
     */
    public boolean keyPressed(int p_nAction, int keyCode)
    {
        boolean handled = false;

        handled = currentSelection.keyPressed(p_nAction, keyCode);

        if( !handled )
        {
            if( (keyCode == Canvas.FIRE || p_nAction == Shell.m_nIDOk) )
            {
                if( currentSelection == boxWallAmount )
                {
                    enableElement(buttonContinue);
                    handled = true;
                }
                else if( currentSelection == buttonContinue )
                {
                    if( boxWallAmount.getString().equals("") )
                    {
                        msgWriteSomething = true;
                        msgAboveMaxWalls = false;
                        boxWallAmount.clear();
                        enableElement(boxWallAmount);
                        handled = true;
                    }
                    else
                    {
                        int wallCount = Integer.parseInt(boxWallAmount.getString());
                        if( wallCount > 10 || wallCount <= 0 )
                        {
                            msgAboveMaxWalls = true;
                            msgWriteSomething = false;
                            boxWallAmount.clear();
                            enableElement(boxWallAmount);
                            handled = true;
                        }
                        else
                            // Set up our paintCalculator
                            Calculator.paintCalculator.resetWalls(wallCount);
                            // We don't set the handle so the Shell loads the next screen
                    }
                }
            }
            else if( /*p_nAction == Canvas.DOWN ||*/ p_nAction == Canvas.RIGHT )
            {
                if( currentSelection == boxWallAmount )
                    enableElement(buttonContinue);
                else if( currentSelection == buttonContinue )
                    enableElement(boxWallAmount);

                handled = true;
            }
            else if( /*p_nAction == Canvas.UP ||*/ p_nAction == Canvas.LEFT )
            {
                if( currentSelection == boxWallAmount )
                    enableElement(buttonContinue);
                else if( currentSelection == buttonContinue )
                    enableElement(boxWallAmount);

                handled = true;
            }
        }

        if( !handled )
            handled = viewerMiniHelp.keyPressed(p_nAction, keyCode);

        return handled;
    }

    public boolean keyReleased(int p_nAction, int keyCode)
    {
        boolean handled = false;

        handled = viewerMiniHelp.keyReleased(p_nAction, keyCode);

        return handled;
    }

    /**
     * enableElement
     *
     * @param e
     *
     */
    private void enableElement(Element e)
    {
        if( e.equals(boxWallAmount) )
        {
            // enable this element
            boxWallAmount.enable();
            // disable this element
            disableElement(buttonContinue);
        }
        else if( e.equals(buttonContinue) )
        {
            // enable this element
            buttonContinue.enable();
            // disable this element
            disableElement(boxWallAmount);
        }

        currentSelection = e;
    }

    /**
     * disableElement
     *
     * @param e
     *
     */
    private void disableElement(Element e)
    {
        if( e.equals(boxWallAmount) )
        {
            boxWallAmount.disable();
            boxWallAmount.showDefaultText = false;
        }
        else if( e.equals(buttonContinue) )
        {
            buttonContinue.disable();
        }
    }

}
