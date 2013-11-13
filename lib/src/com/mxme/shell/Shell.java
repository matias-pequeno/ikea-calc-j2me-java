package com.mxme.shell;

/**************************
 * ShellManager class
 * 
 * Static class that initializes and destroys the ShellManager system.
 *
 *  This class is aimed to be as a fast and generic solution into creating
 * full featured "ShellManager". In design terms, a ShellManager regards to everything that
 * encapsulates an interior. 
 * 
 *      A shell can be used to set up a:
 *          - Application introduction
 *          - Main Menu
 *          - Ingame Menus
 *          - Ingame interface
 * 
 *      The ShellManager contains a collection of CanvasDescriptor that are used to
 * describe the basic functionality and properties of a ShellManager.
 * 
 *      This ShellManager system manages memory by itself, you should only "add_shell",
 * "set_shell" and call the respective system functions, keyPressed/released() 
 * and paint().
 *     
 */
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import com.mxme.common.MXMEConstants;
import com.mxme.shell.core.*;

public abstract class Shell 
{
    // Static variables
    private static Shell uniqueInstance;
    // Screen measurements
    public static int 
            m_nScreenWidth, m_nScreenHeight,
            m_nScreenWidthHalf, m_nScreenHeightHalf;
    
    public static int m_nMarginX, m_nMarginY;
    
    // Drawing variables that define margins
    //  if a standard layout is applied, these must be redefined to adjust to it
    public static int m_nStartX, m_nStartY, m_nUsableWidth, m_nUsableHeight;
    
    // Shell properties
    private static final boolean 
            aggressive_unloading = false; // true: everything gets allocated when needed and deallocated when not needed
                                          //       slower, but consumes much less memory, useful for low-end devices.

    // Shell main branches
    protected ElementBranch m_branchRoot, m_branchCurrent;
    
    // Standard soft button action IDs
    private static final short INDEX_OFFSET = 100;
    public static final short
            m_nIDBack = 0+INDEX_OFFSET,
            m_nIDOk = 1+INDEX_OFFSET,
            m_nIDExit = 2+INDEX_OFFSET,
            m_nIDClear = 3+INDEX_OFFSET,
            m_nIDMenu = 4+INDEX_OFFSET,
            m_nIDMore = 5+INDEX_OFFSET,
            m_nHidden = 0x7FFF;
    private static Image m_iSoftButtons[] = new Image[6];
    
    // Canvas
    protected Canvas canvas;
    
    /***
     *  Constructor
     * 
     * @param CanvasDescriptor
     */
    public Shell(Canvas c)
    {
        if( uniqueInstance != null )
        {
//#if DEBUG == "true"
//#             System.out.println("CRITICAL: Trying to instantiate a new Shell, there should be only ONE Shell");
//#endif
            return;
        }

        canvas = c;
        setScreenSize(c.getWidth(), c.getHeight());

        uniqueInstance = this;
    }
    
    /***
     *  setScreenSize
     * 
     * @param int w, int h, new screen size
     */
    public static void setScreenSize(int w, int h)
    {
        // Screen measurements
        m_nScreenWidth = w;
        m_nScreenWidthHalf = w >> 1;
        m_nScreenHeight = h;
        m_nScreenHeightHalf = h >> 1;
        
        // Default margins
        m_nMarginX = m_nScreenWidth / 50;
        m_nMarginY = m_nScreenHeight / 50;
    }

    /**
     *
     */
    public static Shell getInstance()
    {
        return uniqueInstance;
    }
    
    /***
     *  Load / Unload
     * 
     * @param 
     */
    public boolean load()
    {
        if( isLoaded() )
            return false;
        
        //m_branchRoot = new ElementBranch();
        //m_branchCurrent = m_branchRoot;
        return true;
    }
    
    public void unload()
    {
        m_branchRoot = null;
        m_branchCurrent = null;
        Element.m_standardLayout = null;
    }
    
    public boolean isLoaded()
    {
        return m_branchCurrent != null;
    }

    public static Image getSoftButton(int i)
    {
        return m_iSoftButtons[i - INDEX_OFFSET];
    }

    protected static void setSoftButton(int i, Image img)
    {
        m_iSoftButtons[i - INDEX_OFFSET] = img;
    }
    
    /**
     *  Sets and initializes the current element
     * 
     * @param ElementBranch p_ebElement, new current element
     */
    public ElementBranch setCurrentBranch(ElementBranch p_ebElementBranch)
    {
        if( p_ebElementBranch == null ) return m_branchCurrent;
        
        boolean bGoingBack = false;
        if( m_branchCurrent != null )
        {
            bGoingBack = m_branchCurrent.parent == p_ebElementBranch;

            // Deinitialize our current element
            m_branchCurrent.element.deinitialize();
        }

        // Set the new element...
        m_branchCurrent = p_ebElementBranch;
        
        // If we go back to EnterName, we automatically set it to "update" mode
        //  since we already entered a name, 99.9% of times going back here will
        //  mean that the user committed a mistake and introduced a name that
        //  was wrong.
        if( bGoingBack && m_branchCurrent.element instanceof MultiTap )
            ((MultiTap)m_branchCurrent.element).resetVariables(true);
        else
            // ...initialize the element.
            m_branchCurrent.element.initialize();

        return m_branchCurrent;
    }

    /**
     * Gracefully creates an Element Tree Root, sets it as our current Branch,
     * stores it as our Root and returns it.
     *
     * @param p_rootElement
     * @return
     */
    public ElementBranch createRoot(Element p_rootElement)
    {
        return m_branchRoot = ElementBranch.createRoot(p_rootElement);
    }

    public void createTree(ElementBranch p_dummyFullTree)
    {
        setCurrentBranch(m_branchRoot);
    }
    
    /**
     * Called from MainCanvas when a key was pressed.
     * 
     * @param action: key pressed
     */
    public void keyPressed(int action, int keyCode)
    {
        // Forward the keyPressed callback
        boolean key_handled = m_branchCurrent.element.keyPressed(action, keyCode);
        
        if( !key_handled )
        {
            // Process softbutton
            if( (action == MXMEConstants.SOFTKEY_RIGHT_CUSTOM || action == Canvas.FIRE) && (m_branchCurrent.element.m_nRightSoftButton != m_nHidden) )
                action = m_branchCurrent.element.m_nRightSoftButton;
            else if( action == MXMEConstants.SOFTKEY_LEFT_CUSTOM && (m_branchCurrent.element.m_nLeftSoftButton != m_nHidden) )
                action = m_branchCurrent.element.m_nLeftSoftButton;

            // Perform action
            if( action == m_nIDMore )
            {
                // If there is an element to move forward, we go
                if( m_branchCurrent.branch[0] != null )
                    setCurrentBranch(m_branchCurrent.branch[0]);
            }
            else if( action == m_nIDOk )
            {
                // If this is a Menu...
                if( m_branchCurrent.element instanceof Menu )
                    // ...We let it to process their elements by itself,
                    // whatever it returns will be our next element.
                    setCurrentBranch(((Menu)m_branchCurrent.element).process(action, keyCode));
                // ...Else if this Branch contains no more Sub-branches...
                else if( m_branchCurrent.branchCount == 0 )
                    // ...We just go back.
                    keyPressed(m_nIDBack, keyCode);
                // ...Else...
                else
                {
                    // If there is an element to move forward, we go
                    if( m_branchCurrent.branch[0] != null )
                        setCurrentBranch(m_branchCurrent.branch[0]);
                }
            }
            else if( action == m_nIDBack )
                // Just go back.
                setCurrentBranch(m_branchCurrent.parent);
            else if( action == m_nIDMenu )
                // We go to the root
                setCurrentBranch(m_branchRoot);
        }
            
    }
    
    /**
     * Called from MainCanvas when a key was released.
     * 
     * @param action: key released
     */
    public void keyReleased(int action, int keyCode)
    {
        // Forward the key released instruction to our current element
        m_branchCurrent.element.keyReleased(action, keyCode);
    }
    
    /***
     *  Paints the shell
     * 
     * @param Graphics g: 
     */
    public void paint(Graphics g)
    {
        // Define viewport ranges
        m_nStartX = m_nScreenWidthHalf >> 2;
        m_nStartY = m_nMarginY;
        m_nUsableWidth = m_nScreenWidthHalf + (m_nScreenWidthHalf>>1); // shell width, half + a quarter of the screen
        m_nUsableHeight = m_nScreenHeightHalf - m_nStartY - m_nMarginY;
        
        // paint our current element
        m_branchCurrent.element.paint(g);
    }
    
}
