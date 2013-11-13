package com.mxme.shell;

/**************************
 * Element class
 * 
 * Contains an element in the shell
 * 
 *  Notes:
 *      
 *     
 */
import javax.microedition.lcdui.*;

public abstract class Element
{
    // Element name index (taken from TextManager)
    public String m_sTitle = ""; // Default behaviour: If this is empty, m_nTitle will be used
    public int m_nTitle;
    // Soft button indices this element uses
    public short m_nLeftSoftButton = Shell.m_nIDOk, m_nRightSoftButton = Shell.m_nIDBack;
    // Hidden property
    public boolean m_bHidden = false;
    // To be treated as a Form's control
    public boolean isEnabled = true;
    // Static Standard Layout
    public static IStandardLayout m_standardLayout;
    
    /****
     *  Initialize this element
     * 
     * @param none
     */
    public abstract void initialize();

    /****
     *  DeInitialize this element
     *
     * @param none
     */
    public abstract void deinitialize();

    /****
     *   Process the element
     * 
     * @param int p_nAction, data to be sent to the processing function
     * @return boolean, was the key handled?
     */
    public abstract boolean keyPressed(int p_nAction, int keyCode);
    public abstract boolean keyReleased(int p_nAction, int keyCode);
    
    /****
     * Paint the element
     * 
     * All drawing assumes we have a big enough Clip to draw what we need.
     * This method also draws a standard layout that can be overriden in the childs.
     * 
     * @param g, Graphics object
     */
    public abstract void paint(Graphics g);

    /**
     * Enable this element
     */
    public void enable()
    {
        isEnabled = true;
    }

    /**
     * Disable this element
     */
    public void disable()
    {
        isEnabled = false;
    }

    /**
     * Sets the Soft Buttons that should appear when this Element is
     * Activated or Selected
     *
     * @param left
     * @param right
     */
    public void setSoftButtons(short left, short right)
    {
        m_nLeftSoftButton = left;
        m_nRightSoftButton = right;
    }
    
}
