package shell.ext;

/**************************
 * Viewer element class
 * 
 *  Includes About, Help, Summaries and Tips
 * 
 *  Notes:
 *      
 *     
 */
import com.mxme.common.Common;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Image;

// MatuX library
import com.mxme.common.*;
import com.mxme.shell.Shell;
import com.mxme.shell.Element;

import util.*;
import shell.AppShell;
import shell.StandardLayout;

/**
 * Viewer Class
 *
 * @author MatuX
 */
public class Viewer extends Element
{
    public static final byte 
        NONE = -1, TYPE_SCROLL = 0, TYPE_STATIC = 1;

//#if GFX_RES_LOW == "true" || GFX_RES_ULTRALOW == "true"
//#     private final int startX = (Shell.m_nScreenWidthHalf >> 3) - (Shell.m_nMarginY >> 1);
//#else
   private final int startX = (Shell.m_nScreenWidthHalf >> 2) - (Shell.m_nMarginY >> 1);
//#endif
    private final int fixedW = Shell.m_nScreenWidth - (startX << 1);
    
    // Viewer type
    public byte m_nType;

    // Scrolling
    protected static final byte SCROLL_SPEED_NORMAL = 1, SCROLL_MAX_SPEED = 5;
    protected int scrollOffset = 0, scrollSpeed = SCROLL_SPEED_NORMAL;
    protected long scrollTimer;
    protected boolean scrollDown, scrollUp, scrollSlowDown;
    
    // Viewer text
    protected int m_nViewerText;
    protected String m_sViewerText;
    
    // element constructor
    public Viewer(int p_nTitle)
    {
        m_nTitle = p_nTitle;
        m_nType = TYPE_STATIC;
    }
    
    public Viewer(int p_nTitle, byte p_nType)
    {
        m_nType = p_nType;
        m_nTitle = p_nTitle;
    }
    
    public Viewer(int p_nTitle, int p_nViewerText, byte p_nType)
    {
        m_nType = p_nType;
        m_nTitle = p_nTitle;
        m_nViewerText = p_nViewerText;
    }

    public Viewer(int p_nTitle, String p_sViewerText, byte p_nType)
    {
        m_nTitle = p_nTitle;
        m_nViewerText = NONE;
        m_sViewerText = p_sViewerText;
        m_nType = p_nType;
    }

    public Viewer(String p_sTitle, String p_sViewerText, byte p_nType)
    {
        m_nTitle = NONE;
        m_sTitle = p_sTitle;
        m_nViewerText = NONE;
        m_sViewerText = p_sViewerText;
        m_nType = p_nType;
    }
    
    /**
     *  Initialize this element
     * 
     * @param none
     */
    public void initialize() 
    {
        // load gfx
        Gfx.loadPackage(Gfx.pkgViewer);
        if( m_nViewerText == Text.str_TERMS_TEXT )
            Gfx.loadPackage(Gfx.pkgViewerTerms);

        // Set Soft Buttons
        setSoftButtons(Shell.m_nIDMenu, Shell.m_nIDBack);
        // initialize viewer
        scrollOffset = 0;
        scrollSpeed = SCROLL_SPEED_NORMAL;
        scrollTimer = System.currentTimeMillis();
        scrollDown = false;
        scrollUp = false;
        scrollSlowDown = false;
        // Show Terms?
        ((StandardLayout)m_standardLayout).hideTerms = (m_nViewerText == Text.str_TERMS_TEXT);
    }

    /**
     *  Deinitialize this element
     *
     * @param none
     */
    public void deinitialize()
    {
        Gfx.unloadPackage(Gfx.pkgViewer);
        Gfx.unloadPackage(Gfx.pkgViewerTerms);
        ((StandardLayout)m_standardLayout).hideTerms = false;
    }
    
    /****
     *  Process the element
     * 
     * @param none
     */
    public boolean keyPressed(int p_nAction, int keyCode) 
    { 
        boolean handled = false;
        
        if( p_nAction == Canvas.KEY_NUM8 || p_nAction == Canvas.DOWN )
        {
            scrollDown = true;
            scrollUp = false;
            scrollSlowDown = false;
            scrollSpeed = SCROLL_SPEED_NORMAL;
            handled = true;
        }
        else if( p_nAction == Canvas.KEY_NUM2 || p_nAction == Canvas.UP )
        {
            scrollUp = true;
            scrollDown = false;
            scrollSlowDown = false;
            scrollSpeed = SCROLL_SPEED_NORMAL;
            handled = true;
        }
        
        return handled;
    }
    
    public boolean keyReleased(int p_nAction, int keyCode) 
    {
        boolean handled = false;

        if( p_nAction == Canvas.KEY_NUM8 || p_nAction == Canvas.DOWN )
        {
            //scrollDown = false;
            scrollSlowDown = true;
            handled = true;
        }
        else if( p_nAction == Canvas.KEY_NUM2 || p_nAction == Canvas.UP )
        {
            //scrollUp = false;
            scrollSlowDown = true;
            handled = true;
        }
        
        return handled;
    }

    public void paintText(Graphics g, int x, int y, int w, int h)
    {
//#if DEBUG == "true"
        // Draw debug rect
        g.setColor(0xFF0000);
        g.drawRect(x, y, w, h);
//#endif

        // Draw scroll text
        //Font.set(Shell.m_nScreenHeight>160?Font.VERDANA_MEDIUM_BOLD_GRAY:Font.VERDANA_MEDIUM_BOLD_RED);
        Font.set(Font.VERDANA_SMALL_BOLD_GRAY);
        // Adjust Scroll Offset
        //int lastScrollOffset = scrollOffset;
        if( scrollDown ) scrollOffset += scrollSpeed;
        if( scrollUp ) scrollOffset -= scrollSpeed;
        // Test for Scroll Offset Bounds
        if( scrollOffset < 0 ) scrollOffset = 0;
        // Adjust Scroll Speed
        if( (scrollUp || scrollDown) && scrollSpeed < SCROLL_MAX_SPEED && !scrollSlowDown )
            scrollSpeed++;
        else if( scrollSlowDown && scrollSpeed > SCROLL_SPEED_NORMAL )
        {
            scrollSpeed--;
            if( scrollSpeed == SCROLL_SPEED_NORMAL )
            {
                scrollSlowDown = false;
                scrollDown = false;
                scrollUp = false;
            }
        }

        // draw viewer text
        String t = m_nViewerText == NONE ? m_sViewerText : Text.get(m_nViewerText);
        boolean scrollEnd = Font.drawScroll(g, t, x, y, w, h, scrollOffset, Font.FONT_ALIGN_LEFT|Font.SCROLL_STANDARD);
        if( scrollEnd ) scrollOffset = Font.getFullScrollHeight() - h;

        // draw scroll cursor
        if( Font.getFullScrollHeight() > h )
        {
            // Calculate the scroll
            int scrollX = x + w;
            if( Shell.m_nScreenWidth > 128 )
                scrollX += Shell.m_nMarginX;
            int scrollY = y;
            int scrollCursorHeight = 4;
            int scrollH = h - scrollCursorHeight;
            int scrollCursorY = scrollY + ((scrollOffset * scrollH) / (Font.getFullScrollHeight() - h));

            // draw scroll
            g.setColor(0xb8b7b7);
            g.drawLine(scrollX, scrollY, scrollX, scrollY + scrollH + scrollCursorHeight);
            g.drawLine(scrollX + 1, scrollY, scrollX + 1, scrollY + scrollH + scrollCursorHeight);

            // draw scroll cursor
            g.setColor(0x053b65);
            g.drawLine(scrollX, scrollCursorY, scrollX, scrollCursorY + scrollCursorHeight);
            g.drawLine(scrollX + 1, scrollCursorY, scrollX + 1, scrollCursorY + scrollCursorHeight);
        }
        
    }
    
    /****
     *  Paint the element
     * 
     * @param g, Graphics object
     */
    public void paint(Graphics g)
    {
        m_standardLayout.draw_standard_layout(g, this);

        int y = Gfx.imgStdTitle.image.getHeight();

        // Draw subtitle
        if( Gfx.imgTermsSubtitle.image != null )
        {
            Image imgSubtitle = Gfx.imgTermsSubtitle.image;
            g.drawImage(imgSubtitle, 0, y, Graphics.TOP|Graphics.LEFT);
            y += imgSubtitle.getHeight();
            if( Shell.m_nScreenHeight > 160 )
                y += Shell.m_nMarginY << 2;
        }

        // paint Viewer text
        paintText(g, startX, y, fixedW, AppShell.usableMenuHeight());

    }

}
