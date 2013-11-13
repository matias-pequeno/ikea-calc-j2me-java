package com.mxme.shell.core;

/**************************
 * Menu class
 * 
 * 
 * 
 *  Notes:
 *      
 *     
 */
import javax.microedition.lcdui.*;

import com.mxme.shell.Element;
import com.mxme.shell.ElementBranch;

public abstract class Menu extends Element
{
    // Private variables
    protected ElementBranch m_eMenuRoot;
    
    // Public variables
    protected int m_nCurrentSelection, m_nPreviousSelection;
    protected int m_nVisibleItems;
    protected int m_nFirstVisibleItem, m_nLastVisibleItem;
    
    /***
     *  Menu Constructor
     * 
     * @param p_nTitle
     * @param p_eMenuBranch
     */
    public Menu(int p_nTitle)
    {
        m_nTitle = p_nTitle;
    }
    
    /****
     *  Sets the menu to be used
     * 
     * @param ElementBranch p_eMenuBranch
     */
    public void setMenu(ElementBranch p_eMenuBranch)
    {
        m_eMenuRoot = p_eMenuBranch;
    }
    
    /****
     *  Initialize this element
     * 
     * @param none
     */
    public void initialize()
    {
        // set some defaults
        //m_nVisibleItems = 6;
        //m_nFirstVisibleItem = 0;
        //m_nLastVisibleItem = m_nVisibleItems - 1;
    }

    /****
     *  Initialize this element
     *
     * @param none
     */
    public void deinitialize() { }

    /**
     * 
     * @return
     */
    public int getCurrentSelectionIndex()
    {
        return m_nCurrentSelection;
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
            // Items do not contain anything, so they're processed directly
            m_eMenuRoot.branch[m_nCurrentSelection].element.keyPressed(p_nAction, keyCode);
            // We return this very menu as a tip that no change of ElementBranch is required
            return m_eMenuRoot;
        }
        else
        {
            // We return the selected element to tip our manager that we need to change the current ElementBranch
            return m_eMenuRoot.branch[m_nCurrentSelection];
        }
    }
    
    /****
     *  Key pressed callback from the ShellManager
     * 
     * @param int p_nData, KeyCode
     */
    public boolean keyPressed(int p_nAction, int keyCode)
    {
        boolean handled = false;
        
        if( p_nAction == Canvas.UP && m_nCurrentSelection > 0 )
        {
            // Select the next non-hidden element
            int sel_step = 0;
            while( (m_nCurrentSelection - ++sel_step) > 0 && m_eMenuRoot.branch[m_nCurrentSelection - sel_step].element.m_bHidden == true ) { }
            if( sel_step > 0 )
                m_nPreviousSelection = m_nCurrentSelection;
            m_nCurrentSelection -= sel_step;

            if( sel_step > 0 && m_nCurrentSelection < m_nFirstVisibleItem )
            {
                m_nFirstVisibleItem--;
                m_nLastVisibleItem--;
            }
            
            handled = true;
        }
        else if( p_nAction == Canvas.DOWN && m_nCurrentSelection < m_eMenuRoot.branchCount - 1 )
        {
            // Select the previous non-hidden element
            int sel_step = 0;
            while( (m_nCurrentSelection + ++sel_step) < m_eMenuRoot.branchCount - 1 && m_eMenuRoot.branch[m_nCurrentSelection + sel_step].element.m_bHidden == true ) { }
            if( sel_step > 0 )
                m_nPreviousSelection = m_nCurrentSelection;
            m_nCurrentSelection += sel_step;

            if( sel_step > 0 && m_nCurrentSelection > m_nLastVisibleItem )
            {
                m_nFirstVisibleItem++;
                m_nLastVisibleItem++;
            }
            
            handled = true;
        }
        
        return handled;
    }
    
    /****
     *  Key released callback from the ShellManager
     * 
     * @param int p_nData, KeyCode
     */
    public boolean keyReleased(int p_nAction, int keyCode) 
    { 
        // If the element currently selected isn't a menu, forward the keyReleased instruction
        //if( !(m_eMenuRoot.branch[m_nCurrentSelection].element instanceof Menu) )
        //    return m_eMenuRoot.branch[m_nCurrentSelection].element.keyReleased(p_nAction, keyCode);
        
        return false;
    }
    
    /****
     *  Paint the element
     * 
     * @param g, Graphics object
     */
    public void paint(Graphics g)
    {
        // Draw the standard layout
        m_standardLayout.draw_standard_layout(g, this);
    }

}
