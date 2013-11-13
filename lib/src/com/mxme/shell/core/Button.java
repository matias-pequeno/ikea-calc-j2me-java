package com.mxme.shell.core;

/**
 * Button element class
 * 
 * 
 * 
 *  Notes:
 *      
 *     
 */
// Java library
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

// MatuX library
import com.mxme.shell.Element;
import com.mxme.shell.Shell;

/**
 *
 *
 * @author MatuX
 */
public class Button extends Element
{
    // Private vars
    private Cursor cursor;
    private int cursorMargin = -2; //Shell.m_nScreenWidth / 240;
    // Internal vars
    public int x, y;
    public Image buttonImage = null, buttonShadow = null, buttonTextImage = null, buttonSelected = null;
    public String buttonText;
    public int fontId;
    public short softDefaultLeft = Shell.m_nIDOk,
                 softDefaultRight = Shell.m_nIDBack;
    
    /**
     * Constructor
     * 
     * @param text
     * 
     */
    public Button(Image buttonImage, Image buttonShadow, String text, int font)
    {
        super();
        buttonText = text;
        fontId = font;
    }
    
    public Button(Image buttonImage, Image buttonShadow, Image textImage, Image buttonSelected)
    {
        super();
        this.buttonImage = buttonImage;
        this.buttonShadow = buttonShadow;
        this.buttonTextImage = textImage;
        this.buttonSelected = buttonSelected;

        // initialize cursor
        cursor = new Cursor();
        cursor.width = this.buttonImage.getWidth() + (cursorMargin << 1);
        cursor.height = this.buttonImage.getHeight() + (cursorMargin << 1);
    }

    /****
     * Initialize this element
     *
     * @param none
     *
     */
    public void initialize()
    {
        isEnabled = false;
        x = 0;
        y = 0;
    }

    /****
     * Deinitialize this element
     *
     * @param none
     *
     */
    public void deinitialize()
    {
        
    }

    /****
     * Enable this element
     *
     * @param none
     *
     */
    public void enable()
    {
        super.enable();
        setSoftButtons(softDefaultLeft, softDefaultRight);
        cursor.enable();
    }

    /****
     * Disable this element
     *
     * @param none
     *
     */
    /*public void disable()
    {
        super.disable();
    }*/

    /**
     * Set default soft buttons for this control
     *
     * @param left
     * @param right
     */
    public void setDefaultSoftButtons(short left, short right)
    {
       softDefaultLeft = left;
       softDefaultRight = right;
    }

    /****
     * Process the element
     * 
     * @param none
     * 
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
     * @param g, Graphics object
     * 
     */
    public void paint(Graphics g)
    {
        // Draw button
        g.drawImage((isEnabled?buttonSelected:buttonImage), x, y, Graphics.TOP|Graphics.HCENTER);
        if( buttonShadow != null )
            g.drawImage(buttonShadow, x, y + buttonImage.getHeight(), Graphics.TOP|Graphics.HCENTER);
        g.drawImage(buttonTextImage, x, y + (buttonImage.getHeight() >> 1), Graphics.VCENTER|Graphics.HCENTER);

        // Draw cursor
        if( isEnabled && buttonSelected == null )
        {
            cursor.posX = x - cursorMargin - (buttonImage.getWidth() >> 1);
            cursor.posY = y - cursorMargin;
            cursor.paint(g);
        }
    }
    
}
