/*
 * PaintMeasureForm.java
 *
 * Created on Mar 5, 2009, 4:58:41 PM
 *
 * Copyright (C) 2009 XXX.  All Rights Reserved.
 * This software is the proprietary information of XXX
 *
 */

package shell.ext;

// Java library
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Canvas;
// MatuX library
import com.mxme.shell.Element;
import com.mxme.shell.ElementBranch;
import com.mxme.shell.Shell;
import com.mxme.shell.core.Button;
import com.mxme.shell.core.Menu;
import com.mxme.shell.core.Item;
// Internal
import app.MainCanvas;
import shell.AppShell;
import shell.StandardLayout;
import calculator.Calculator;
import calculator.Wall;
import util.Text;
import util.Font;
import util.Gfx;

/**
 * Author: Administrador
 * Date: Mar 5, 2009
 * Current revision: 0
 * Last modified: Mar 5, 2009
 * By: Administrador
 * Reviewers:
 *
 */
public class PaintMeasureForm extends Menu
{
    // Form components
    private TextBox boxWidth, boxHeight;
    private Button buttonDone, buttonHelp, buttonAddMore;
    private Viewer viewerMiniHelp;
    private int m_nType;
    private ElementBranch branchOrigin;

    private Object measuredObject;

    // Cursor
    private Element currentSelection = null;
    private boolean alreadyChangedOne = false, alreadyChangedTwo = false;

    // Screen
    private final int startX = (Shell.m_nMarginX << 2) + Shell.m_nMarginX;

    /**
     * Constructor
     *
     */
    public PaintMeasureForm(ElementBranch origin, Object measuredObject, String title, int type)
    {
        super(type);
        m_sTitle = title;
        m_nType = type;
        this.measuredObject = measuredObject;
        branchOrigin = origin;
    }

    /****
     * Initialize this element
     *
     * @param none
     *
     */
    public void initialize()
    {
        super.initialize();
        
        Gfx.loadPackage(Gfx.pkgDemoForm);
        setSoftButtons(Shell.m_nIDOk, Shell.m_nIDMenu);

        // Init text boxes
        // Width
        boxWidth = new TextBox("", Font.VERDANA_MEDIUM_NUMBERS, Gfx.imgCalculatorSmallTextBox.image);
        boxWidth.initialize();
        boxWidth.onlyNumbers(true);
        boxWidth.maxChars = 5;
        currentSelection = boxWidth;
        // Height
        boxHeight = new TextBox("", Font.VERDANA_MEDIUM_NUMBERS, Gfx.imgCalculatorSmallTextBox.image);
        boxHeight.initialize();
        boxHeight.onlyNumbers(true);
        boxHeight.maxChars = 5;

        // Set the current data into the boxes
        alreadyChangedOne = false;
        alreadyChangedTwo = false;

        if( measuredObject instanceof Wall )
        {
            Wall w = (Wall)measuredObject;
            int wi = 0, he = 0;
            if( m_nType == Text.str_CALCULATOR_TYPE_PARED )
            {
                wi = w.getWidth();
                he = w.getHeight();
            }
            else if( m_nType == Text.str_CALCULATOR_TYPE_VENTANA )
            {
                wi = w.getWindowWidth(0);
                he = w.getWindowHeight(0);
            }
            else if( m_nType == Text.str_CALCULATOR_TYPE_PUERTA )
            {
                wi = w.getDoorWidth(0);
                he = w.getDoorHeight(0);
            }

            if( wi != 0 && he != 0 )
            {
                boxWidth.set(String.valueOf(wi));
                boxHeight.set(String.valueOf(he));

                alreadyChangedOne = true;
                alreadyChangedTwo = true;
            }
        }

        // Init button
        // Button Done
        buttonDone = new Button(Gfx.imgCalculatorButton.image, null, Gfx.imgCalculatorButtonDoneText.image, Gfx.imgCalculatorButtonSel.image);
        buttonDone.initialize();
        buttonDone.x = startX + Gfx.imgCalculatorTextBox.image.getWidth() + (Gfx.imgCalculatorButton.image.getWidth() >> 1) + Shell.m_nMarginX;
        if( Shell.m_nScreenHeight <= 128 )
            buttonDone.x -= Shell.m_nMarginX * 3;
        // Button Help
        buttonHelp = new Button(Gfx.imgCalculatorButton.image, null, Gfx.imgCalculatorButtonHelpText.image, Gfx.imgCalculatorButtonSel.image);
        buttonHelp.initialize();
        buttonHelp.x = startX + Gfx.imgCalculatorTextBox.image.getWidth() + (Gfx.imgCalculatorButton.image.getWidth() >> 1) + Shell.m_nMarginX;
        if( Shell.m_nScreenHeight <= 128 )
            buttonHelp.x -= Shell.m_nMarginX * 3;
        // Button continue
        buttonAddMore = new Button(Gfx.imgCalculatorButton.image, null, Gfx.imgCalculatorButtonText.image, Gfx.imgCalculatorButtonSel.image);
        buttonAddMore.initialize();
        buttonAddMore.x = startX + (Gfx.imgCalculatorButton.image.getWidth() >> 1) + Shell.m_nMarginX;
        if( Shell.m_nScreenHeight <= 128 )
            buttonAddMore.x -= Shell.m_nMarginX * 3;

        // enable default element
        enableElement(boxWidth);

        // Create viewer
        int helpIndex = 0;
        if( m_nType == Text.str_CALCULATOR_TYPE_PARED )
            helpIndex = Text.str_MEASURE_WALL_HELP_MINI;
        else if( m_nType == Text.str_CALCULATOR_TYPE_VENTANA )
            helpIndex = Text.str_MEASURE_WINDOW_HELP_MINI;
        else if( m_nType == Text.str_CALCULATOR_TYPE_PUERTA )
            helpIndex = Text.str_MEASURE_DOOR_HELP_MINI;

        viewerMiniHelp = new Viewer(Text.str_TERMS, helpIndex, Viewer.TYPE_SCROLL);
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
        //Gfx.loadPackage(Gfx.pkgDemoForm);

        boxWidth = null;
        boxHeight = null;
        buttonDone = null;
        buttonHelp = null;
        buttonAddMore = null;
        currentSelection = null;

        alreadyChangedOne = false;
        alreadyChangedTwo = false;

        super.deinitialize();
    }

    /****
     *   Process the element
     *
     * @param int p_nData, KeyCode
     */
    public ElementBranch process(int p_nAction, int keyCode)
    {
        if( m_eMenuRoot.branch[m_nCurrentSelection].element instanceof Item )
        {
            Item i = (Item)m_eMenuRoot.branch[m_nCurrentSelection].element;
            if( i.m_nTitle == Text.str_BACK_TO_PAINT_MENU )
                return branchOrigin;
        }

       return super.process(p_nAction, keyCode);
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
        setSoftButtons(currentSelection.m_nLeftSoftButton, Shell.m_nIDBack);

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
        y += Gfx.imgCalculatorSubtitle.image.getHeight() + (Shell.m_nMarginY << 1);

        // Draw text width/height
        int boxW = Gfx.imgCalculatorTextBox.image.getWidth();
        int boxH = Gfx.imgCalculatorTextBox.image.getHeight();
        boxWidth.x = startX - Shell.m_nMarginX;
        boxHeight.x = startX + boxW;
        g.drawImage(Gfx.imgCalculatorAncho.image, boxWidth.x, y, Graphics.TOP|Graphics.LEFT);
        g.drawImage(Gfx.imgCalculatorAlto.image, boxHeight.x, y, Graphics.TOP|Graphics.LEFT);
        y += Gfx.imgCalculatorAncho.image.getHeight() + Shell.m_nMarginY;
        // Draw width/height Boxes
        boxWidth.y = y;
        boxHeight.y = y;
        boxWidth.paint(g);
        boxHeight.paint(g);
        y += boxH + Shell.m_nMarginY;
        // Draw buttons (Done, Help, Add Window/Door)
        int halfBut = Gfx.imgCalculatorButton.image.getWidth() >> 1;
        // Done
        buttonDone.x = startX + halfBut;
        buttonDone.y = y;
        buttonDone.paint(g);
        // Help
        buttonHelp.x = buttonDone.x + Gfx.imgCalculatorButton.image.getWidth();
        buttonHelp.y = y;
        buttonHelp.paint(g);
        // AddMore
        if( m_nType != Text.str_CALCULATOR_TYPE_PUERTA )
        {
            buttonAddMore.x = buttonHelp.x + Gfx.imgCalculatorButton.image.getWidth();
            buttonAddMore.y = y;
            buttonAddMore.paint(g);
        }
        y += Gfx.imgCalculatorButton.image.getHeight() + (Shell.m_nMarginY << 1);
        // Load mini help text
        Font.set(Font.VERDANA_MEDIUM_BOLD_GRAY);
        int x = startX;
        int w = Shell.m_nScreenWidth - (x << 1);
        int lowerBoxH = (Font.getHeight() * 4) + (Font.getHeight() >> 1);
//#if GFX_RES_ULTRALOW == "true"
//#         lowerBoxH += Font.getHeight();
//#endif
        viewerMiniHelp.paintText(g, x, y, w, lowerBoxH);
        y += (Font.getHeight() << 1) + Shell.m_nMarginY;

        // change control
        if( boxWidth.isEnabled && boxWidth.getCurPos() >= boxWidth.maxChars && !alreadyChangedOne )
        {
            keyPressed(-1, Canvas.FIRE);
            alreadyChangedOne = true;
        }
        else if( boxHeight.isEnabled && boxHeight.getCurPos() >= boxHeight.maxChars && !alreadyChangedTwo )
        {
            keyPressed(-1, Canvas.FIRE);
            alreadyChangedTwo = true;
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

        if( !(keyCode == Canvas.KEY_NUM8 || keyCode == Canvas.KEY_NUM2) )
            handled = viewerMiniHelp.keyPressed(p_nAction, keyCode);

        //if( super.keyPressed(p_nAction, keyCode) )
        //    return true;

        handled = currentSelection.keyPressed(p_nAction, keyCode);

        if( !handled )
        {
            if( (keyCode == Canvas.FIRE || p_nAction == Shell.m_nIDOk) )
            {
                if( currentSelection == boxWidth )
                {
                    enableElement(boxHeight);
                    handled = true;
                }
                else if( currentSelection == boxHeight )
                {
                    enableElement(buttonAddMore);
                    handled = true;
                }
                else if( currentSelection == buttonDone )
                {
                    m_nCurrentSelection = 0;
                }
                else if( currentSelection == buttonHelp )
                {
                    m_nCurrentSelection = 1;
                }
                else if( currentSelection == buttonAddMore )
                {
                    m_nCurrentSelection = 2;
                }

            }
            else if( /*p_nAction == Canvas.DOWN ||*/ p_nAction == Canvas.RIGHT )
            {
                if( currentSelection == boxWidth )
                    enableElement(boxHeight);
                else if( currentSelection == boxHeight )
                    enableElement(buttonDone);
                else if( currentSelection == buttonDone )
                    enableElement(buttonHelp);
                else if( currentSelection == buttonHelp )
                    enableElement(buttonAddMore);
                else if( currentSelection == buttonAddMore )
                    enableElement(boxWidth);

                handled = true;
            }
            else if( /*p_nAction == Canvas.UP ||*/ p_nAction == Canvas.LEFT )
            {
                if( currentSelection == boxWidth )
                    enableElement(buttonAddMore);
                else if( currentSelection == buttonAddMore )
                    enableElement(buttonHelp);
                else if( currentSelection == buttonHelp )
                    enableElement(buttonDone);
                else if( currentSelection == buttonDone )
                    enableElement(boxHeight);
                else if( currentSelection == boxHeight )
                    enableElement(boxWidth);

                handled = true;
            }
        }

        return handled;
    }

    public boolean keyReleased(int p_nAction, int keyCode)
    {
        boolean handled = false;
        if( !(keyCode == Canvas.KEY_NUM8 || keyCode == Canvas.KEY_NUM2) )
            handled = viewerMiniHelp.keyReleased(p_nAction, keyCode);
        if( !handled )
            handled = super.keyReleased(p_nAction, keyCode);
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
        if( e == buttonAddMore && m_nType == Text.str_CALCULATOR_TYPE_PUERTA )
            e = buttonDone;
        
        // disable all
        disableElement(boxWidth);
        disableElement(boxHeight);
        disableElement(buttonDone);
        disableElement(buttonHelp);
        disableElement(buttonAddMore);

        // enable this element
        e.enable();

        // make it our current selection
        currentSelection = e;

        // we store the measure written in the textboxes
        if( measuredObject instanceof Wall )
        {
            int wi = 0, he = 0;
            try {
            wi = Integer.parseInt(boxWidth.getString());
            he = Integer.parseInt(boxHeight.getString());
            } catch( Exception ex ) { }

            Wall w = (Wall)measuredObject;
            if( m_nType == Text.str_CALCULATOR_TYPE_PARED )
                w.setWallMeasure(wi, he);
            else if( m_nType == Text.str_CALCULATOR_TYPE_VENTANA )
                w.addWindow(wi, he);
            else if( m_nType == Text.str_CALCULATOR_TYPE_PUERTA )
                w.addDoor(wi, he);
        }

    }

    /**
     * disableElement
     *
     * @param e
     *
     */
    private void disableElement(Element e)
    {
        e.disable();

        if( e.equals(boxWidth) )
            boxWidth.showDefaultText = false;
        else if( e.equals(boxHeight) )
            boxHeight.showDefaultText = false;
        
    }

}
