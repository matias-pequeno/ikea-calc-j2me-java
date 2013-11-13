package shell;

/**************************
 * AppShell class
 * 
 * Encapsulates the whole game and application shell layer.
 * 
 *  Notes:
 *      check load() for some useful tips about memory management
 *      
 *     
 */
import com.mxme.shell.ElementBranch;
import com.mxme.shell.Element;
import com.mxme.shell.Shell;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Canvas;

// Shell Library
import com.mxme.common.MXMEConstants;
import com.mxme.shell.*;
import com.mxme.shell.core.Item;

import shell.ext.*;
import util.*;
import calculator.Wall;
import app.MainCanvas;

public class AppShell extends Shell
{
    // Miscellaneous
    public static int queue_action = 0;
    // Background sound
    public static byte m_nSoundBackground;
    
    public static MainCanvas mainCanvas;

    // Terminos y Condiciones
    private static ElementBranch viewerTermsBranch = new ElementBranch();
    private static boolean showingTerms = false;
    public static Viewer viewerTerms;
    private static ElementBranch previousElementBranch;
    public static ElementBranch nextElementBranch;
    
    /****
     *  Constructor
     * 
     * @param none
     */
    public AppShell(Canvas c)
    {
        super(c);
        mainCanvas = (MainCanvas)c;
    }

    public void showTerms()
    {
        previousElementBranch = m_branchCurrent;
        setCurrentBranch(ElementBranch.createOrphan(new Viewer(Text.str_TERMS, Text.str_TERMS_TEXT, Viewer.TYPE_SCROLL)));
        showingTerms = true;
    }

    public void hideTerms(boolean backToMenu)
    {
        showingTerms = false;
        setCurrentBranch(backToMenu?m_branchRoot:previousElementBranch);
    }

    public static int usableMenuHeight()
    {
        //int y = Gfx.imgStdTitle.image.getHeight() + Gfx.imgStdCmdOk.image.getHeight() + Gfx.imgStdTermsText.image.getHeight() + 48;
        //return Shell.m_nScreenHeight - y;
//#if GFX_RES_HIGH == "true"
        return 130;
//#elif GFX_RES_MED == "true"
//#        return (Gfx.imgStdBackForm.image.getWidth() >> 1) - Font.getHeight(Font.TAHOMA_SMALL_BOLD_BLUE);
//#elif GFX_RES_LOW == "true"
//#        return (Gfx.imgStdBackForm.image.getWidth() >> 1) - Shell.m_nMarginY - 2;
//#elif GFX_RES_ULTRALOW == "true"
//#        return (Gfx.imgStdBackForm.image.getWidth() >> 1) - (Shell.m_nMarginY << 3);
//#endif
    }
    
    public final boolean load()
    {
        if( !super.load() )
            return false;
        
        // Create our standard layout
        Element.m_standardLayout = new StandardLayout();

        // Set Soft Buttons
        // We assume these were already loaded!
        setSoftButton(m_nIDOk, Gfx.imgStdCmdOk.image);
        setSoftButton(m_nIDBack, Gfx.imgStdCmdBack.image);
        setSoftButton(m_nIDExit, Gfx.imgStdCmdExit.image);
        setSoftButton(m_nIDClear, Gfx.imgStdCmdClear.image);
        setSoftButton(m_nIDMenu, Gfx.imgStdCmdMenu.image);
        setSoftButton(m_nIDMore, Gfx.imgStdCmdMore.image);
        
        // Create Menu Tree
        createTree(
            createRoot(new MenuExt(MenuExt.MAIN_MENU))
                .addBranchWithChilds(new PaintCountForm())
                    .addBranchVirgin(new MenuExt(MenuExt.PAINT_MENU))
                    .getParent()
                .addBranchVirgin(new Viewer(Text.str_CALCULATOR_TYPE_CERAMICOS, "En construccion...", Viewer.TYPE_SCROLL))
                .addBranchVirgin(new Viewer(Text.str_CALCULATOR_TYPE_LAMINADOS, "En construccion...", Viewer.TYPE_SCROLL))
                .addBranchVirgin(new Item(Text.str_EXIT))
        );

        // Setup and play Main MenuExt music
        m_nSoundBackground = Sound.SND_MENU;
        if( !Sound.isPlaying(m_nSoundBackground) )
            Sound.play(m_nSoundBackground);
        
        // Start cursor animation
        //Gfx.playSpriteAnimation(Gfx.MENU_ARR_LEFT);
        //Gfx.playSpriteAnimation(Gfx.MENU_ARR_RIGHT);
        
        return true;
    }

    public final void unload()
    {
        super.unload();
        //Gfx.unloadPackage(Gfx.pkgMenu);
        
        // kill the sounds used by the main menu
        Sound.unloadAll();
    }
    
    /***
     *  Paints the shell
     * 
     * @param Graphics g: 
     */
    public void paint(Graphics g)
    {
        super.paint(g);
        
        /*if( StandardLayout.process_soft_button_animation() )
        {
            if( queue_action == m_nIDExit )
                MainCanvas.quit();
            else
                keyPressed(queue_action, 0);
            
            queue_action = 0;
        }*/
    }
    
    /**
     * Called from MainCanvas when a key was pressed.
     * 
     * @param action: key pressed
     */
    public void keyPressed(int action, int keyCode)
    {
        if( (action == MXMEConstants.SOFTKEY_RIGHT_CUSTOM && m_branchCurrent.element.m_nRightSoftButton == m_nIDExit) ||
            (action == MXMEConstants.SOFTKEY_LEFT_CUSTOM  && m_branchCurrent.element.m_nLeftSoftButton  == m_nIDExit) )
            MainCanvas.quit();
        // This is dirty, but if our current Element is a ViewerBase..
        /*else if( m_branchCurrent.element instanceof Viewer && ((Viewer)m_branchCurrent.element).m_nType == Viewer.TYPE_SCROLL &&
            (action == Canvas.FIRE || action == MXMEConstants.SOFTKEY_RIGHT_CUSTOM) )
        {
            // ..we let it process the Fire and Right button event in its own way (used as fast forward)
            m_branchCurrent.element.keyPressed(action, keyCode);
        }
        // We catch the soft keys to perform a small animation as requested by the artist
        else if( action == MXMEConstants.SOFTKEY_RIGHT_CUSTOM && (m_branchCurrent.element.m_nRightSoftButton != Shell.m_nHidden) )
        {
            StandardLayout.animate_soft_button();
            queue_action = m_branchCurrent.element.m_nRightSoftButton;
        }
        else if( (action == MXMEConstants.SOFTKEY_LEFT_CUSTOM || action == Canvas.FIRE) && (m_branchCurrent.element.m_nLeftSoftButton != Shell.m_nHidden) )
        {
            StandardLayout.animate_soft_button();
            queue_action = m_branchCurrent.element.m_nLeftSoftButton;
        }*/
        else if( keyCode == Canvas.KEY_STAR && !showingTerms )
            showTerms();
        else if( action == MXMEConstants.SOFTKEY_RIGHT_CUSTOM && (m_branchCurrent.element.m_nRightSoftButton != Shell.m_nHidden) )
        {
            if( (m_branchCurrent.element.m_nRightSoftButton == m_nIDBack || m_branchCurrent.element.m_nRightSoftButton == m_nIDMenu) && showingTerms )
                hideTerms(m_branchCurrent.element.m_nRightSoftButton == m_nIDMenu);
            else
                super.keyPressed(m_branchCurrent.element.m_nRightSoftButton, keyCode);
        }
        else if( (action == MXMEConstants.SOFTKEY_LEFT_CUSTOM || action == Canvas.FIRE) && keyCode != Canvas.KEY_NUM5 && (m_branchCurrent.element.m_nLeftSoftButton != Shell.m_nHidden) )
        {
            if( ((m_branchCurrent.element.m_nLeftSoftButton == m_nIDBack || m_branchCurrent.element.m_nLeftSoftButton == m_nIDMenu) || action == Canvas.FIRE) && showingTerms )
                hideTerms(m_branchCurrent.element.m_nLeftSoftButton == m_nIDMenu);
            else
                super.keyPressed(m_branchCurrent.element.m_nLeftSoftButton, action == Canvas.FIRE ? Canvas.FIRE : keyCode);
        }
        else
            super.keyPressed(action, keyCode);
    }
    
}
