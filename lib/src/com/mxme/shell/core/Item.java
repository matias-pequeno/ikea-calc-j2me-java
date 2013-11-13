package com.mxme.shell.core;

/**************************
 * Item class
 * 
 * 
 * 
 *  Notes:
 *      
 *     
 */
import javax.microedition.lcdui.*;

import com.mxme.shell.Element;

public class Item extends Element
{
    public static final boolean 
            ON  = true, OFF = false;
    private int m_iTextOn, m_iTextOff;
    public boolean m_bState;
    public boolean m_bSwitchMode;
    
    public Item(int p_nTitle)
    {
        m_nTitle = p_nTitle;
        m_bSwitchMode = false;
    }
    
    public Item(int p_iTextOn, int p_iTextOff, boolean p_bInitialState)
    {
        m_iTextOn = p_iTextOn;
        m_iTextOff = p_iTextOff;
        m_bState = p_bInitialState;
        m_nTitle = m_bState==ON?m_iTextOn:m_iTextOff;
        m_bSwitchMode = true;
    }
    
    /****
     *  Initialize this element
     * 
     * @param none
     */
    public void initialize() { }

    /****
     *  Deinitialize this element
     *
     * @param none
     */
    public void deinitialize() { }
    
    /****
     *   Process the element
     * 
     * @param int p_nAction
     */
    public boolean keyPressed(int p_nAction, int keyCode)
    {
        boolean handled = false;
        
        if( m_bSwitchMode )
        {
            m_bState = !m_bState;
            m_nTitle = m_bState==ON?m_iTextOn:m_iTextOff;
            
            handled = true;
        }
        
        return handled;
    }
    
    public boolean keyReleased(int p_nAction, int keyCode) { return false; }
    
    /****
     *  Paint the element
     * 
     * @param g, Graphics object
     */
    public void paint(Graphics g)
    {
        // Nothing
    }

}
