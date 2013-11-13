package shell.ext;

/**************************
 * MenuExt class
 * 
 * 
 * 
 *  Notes:
 *      
 *     
 */
import javax.microedition.lcdui.Graphics;
import java.util.Vector;

import com.mxme.shell.core.Menu;
import com.mxme.shell.core.Item;
import com.mxme.shell.ElementBranch;
import com.mxme.shell.Shell;

import app.MainCanvas;
import shell.AppShell;
import shell.StandardLayout;
import calculator.Calculator;
import calculator.Wall;
import util.Text;
import util.Font;
import util.Gfx;

public class MenuExt extends Menu
{
    public static final byte MAIN_MENU = 1, PAINT_MENU = 2;

    private byte FADE_SPEED = 5;
    private boolean[] fadeOpt; // true: fadeIn, false: fadeOut
    private int [] fadeColor;
    //private long[] fadeDelay;

    private int indentMenuItems;
    private boolean showScroll;
    private boolean visibilityInitialized = false;
    private boolean initialized = false;
    
    /***
     *  MenuExt Constructor
     * 
     */
    public MenuExt()
    {
        this(0);
    }
    
    public MenuExt(int id)
    {
        super(id);
    }
    
    /****
     *  Initialize this element
     * 
     * @param none
     */
    public void initialize()
    {
        if( m_nTitle == PAINT_MENU )
        {
            // Clear the current Menu
            m_eMenuRoot.removeAllBranches();
            visibilityInitialized = false;
            m_nCurrentSelection = 0;
            m_nPreviousSelection = 0;

            // add the elements dynamically
            Vector walls = Calculator.paintCalculator.walls;
            for( int i = 0; i < walls.size(); ++i )
            {
                Wall w = (Wall)walls.elementAt(i);
                String m2 = w.calculateM2AsString();
                
                // Add the required Forms for this Wall
                m_eMenuRoot.addBranchWithChilds(new PaintMeasureForm(m_eMenuRoot, w, "Pared " + (i+1) + ": " + (m2.equals("")?"Calcular m2":m2), Text.str_CALCULATOR_TYPE_PARED))
                    .addBranchVirgin(new Item(Text.str_BACK_TO_PAINT_MENU))
                    .addBranchVirgin(new Viewer(Text.str_HELP_TEXT, Text.str_BIG_MEASURE_HELP, Viewer.TYPE_SCROLL))
                    .addBranchWithChilds(new PaintMeasureForm(m_eMenuRoot, w, "", Text.str_CALCULATOR_TYPE_VENTANA))
                        .addBranchVirgin(new Item(Text.str_BACK_TO_PAINT_MENU))
                        .addBranchVirgin(new Viewer(Text.str_HELP_TEXT, Text.str_BIG_MEASURE_HELP, Viewer.TYPE_SCROLL))
                        .addBranchWithChilds(new PaintMeasureForm(m_eMenuRoot, w, "", Text.str_CALCULATOR_TYPE_PUERTA))
                            .addBranchVirgin(new Item(Text.str_BACK_TO_PAINT_MENU))
                            .addBranchVirgin(new Viewer(Text.str_HELP_TEXT, Text.str_BIG_MEASURE_HELP, Viewer.TYPE_SCROLL))
                ;
            }

            // The last item is the results button
            m_eMenuRoot.addBranchWithChilds(new PaintYieldForm())
                .addBranchWithChilds(new PaintHandsForm())
                    .addBranchWithChilds(new PaintExtenderForm(0))
                        .addBranchVirgin(new PaintExtenderForm(1))
            ;
        }

        initialize(true);
    }
    
    public void initialize(boolean loadGfx)
    { 
        super.initialize();
        if( loadGfx )
        {
            Gfx.loadPackage(Gfx.pkgMenu);
            Gfx.loadPackage(Gfx.pkgStdMenu);
        }
        if( m_nTitle == PAINT_MENU )
            setSoftButtons(Shell.m_nIDOk, Shell.m_nIDBack);
        else
            setSoftButtons(Shell.m_nIDOk, Shell.m_nIDExit);
        initializeFadeEngine();

        if( !visibilityInitialized )
        {
            Font.set(Font.VERDANA_MEDIUM_BOLD_GRAY);

            indentMenuItems = Font.getHeight() >> 1;
            if( Shell.m_nScreenHeight <= 128 )
                indentMenuItems -= 2;
            else if( Shell.m_nScreenHeight <= 208 )
                indentMenuItems -= 3;
            
            // calculate m_nVisibleItems
            m_nVisibleItems = (AppShell.usableMenuHeight()) / (Font.getHeight() + indentMenuItems);
            // small visual adjustment (hack)
            if( Shell.m_nScreenHeight <= 208 && Shell.m_nScreenHeight > 160 )
                m_nVisibleItems--;
            //else if( Shell.m_nScreenHeight > 220 || (Shell.m_nScreenHeight > 128 && Shell.m_nScreenHeight <= 160) )
            //    m_nVisibleItems++;
            // Compute first and last visible items
            m_nFirstVisibleItem = 0;
            m_nLastVisibleItem = m_nVisibleItems > m_eMenuRoot.branchCount ? m_eMenuRoot.branchCount - 1 : m_nVisibleItems - 1;
            showScroll = m_nVisibleItems > m_nLastVisibleItem;

            visibilityInitialized = true;
        }

        initialized = true;
    }

    public void initializeFadeEngine()
    {
        fadeOpt = new boolean[m_eMenuRoot.branchCount];
        for( int i = 0; i < fadeOpt.length; ++i )
            fadeOpt[i] = false;
        fadeOpt[m_nCurrentSelection] = true; // always fade in the first option
        fadeColor = new int[m_eMenuRoot.branchCount];
        for( int i = 0; i < fadeColor.length; ++i )
            fadeColor[i] = 0xffffff;
        //fadeDelay = new long[m_eMenuRoot.branchCount];
    }

    /****
     *  Deinitialize this element
     *
     * @param none
     */
    public void deinitialize()
    {
        initialized = false;
        super.deinitialize();
        Gfx.unloadPackage(Gfx.pkgMenu);
        Gfx.unloadPackage(Gfx.pkgStdMenu);
    }
    
    /****
     *  Key pressed callback from the Shell
     * 
     * @param int p_nData, KeyCode
     */
    public boolean keyPressed(int p_nAction, int keyCode)
    {
        boolean handled = false;
        
        handled = super.keyPressed(p_nAction, keyCode);

        if( handled && m_nCurrentSelection != m_nPreviousSelection )
        {
            fadeOpt[m_nPreviousSelection] = false;
            fadeOpt[m_nCurrentSelection] = true;
        }
        else if( !handled && p_nAction == Shell.m_nIDOk && m_eMenuRoot.branch[m_nCurrentSelection].element.m_nTitle == Text.str_EXIT )
            MainCanvas.quit();


        return handled;
    }
    
    public boolean keyReleased(int p_nAction, int keyCode) 
    { 
        return super.keyReleased(p_nAction, keyCode); 
    }
    
    /****
     *   Process the element
     * 
     * @param int p_nData, KeyCode
     */
    public ElementBranch process(int p_nAction, int keyCode)
    {
        return super.process(p_nAction, keyCode);
    }

    /**
     * Paints the elements of this menu
     *
     */
    protected void paintMenuElements(Graphics g, int y)
    {
        /*for( int i = 0; i < m_eMenuRoot.branchCount; ++i )
        {
            // If the current element is hidden, skip it
            if( m_eMenuRoot.branch[i].element.m_bHidden )
                continue;
        }*/

        // Draw menu elements
        Font.set(Font.VERDANA_MEDIUM_BOLD_GRAY);

        // Process fades
        int margin = Shell.m_nMarginY >> 1;
        int boxX = Shell.m_nScreenWidth >> 3;
        int boxW = Shell.m_nScreenWidth - (boxX << 1);
        int boxH = Font.getHeight() + (margin << 1);
        /*
        for( int i = m_nFirstVisibleItem; i <= m_nLastVisibleItem; ++i )
        {
            int red = (fadeColor[i] >> 16) & 0xff;
            int green = (fadeColor[i] >> 8) & 0xff;
            int blue = (fadeColor[i] & 0xff);

            if( fadeOpt[i] ) // Fade IN
            {
                if( red   > 217 ) red -= (FADE_SPEED);
                if( green > 170 ) green -= (FADE_SPEED);
                if( blue  > 142 ) blue -= FADE_SPEED;
            }
            else // Fade OUT
            {
                if( red   < 255 ) red += (FADE_SPEED+2);
                if( green < 255 ) green += (FADE_SPEED);
                if( blue  < 255 ) blue += (FADE_SPEED-3);
                // bounds
                if( red > 255 ) red = 255;
                if( green > 255 ) green = 255;
                if( blue > 255 ) blue = 255;
            }

            fadeColor[i] = (red<<16) + (green<<8) + blue;

            // draw selection cursor
            g.setColor(fadeColor[i]);

            int boxY = y + ((Font.getHeight() + indentMenuItems) * (i - m_nFirstVisibleItem)) - margin;
            g.fillRect(boxX, boxY, boxW, boxH);
        }
        */
        int itemY = y;
        for( int i = m_nFirstVisibleItem; i <= m_nLastVisibleItem; ++i )
        {
            // If the current element is hidden, skip it
            if( m_eMenuRoot.branch[i].element.m_bHidden )
                continue;

            // Draw the element display text
            String title = m_eMenuRoot.branch[i].element.m_sTitle.equals("")?Text.get(m_eMenuRoot.branch[i].element.m_nTitle):m_eMenuRoot.branch[i].element.m_sTitle;
            if( i == m_nCurrentSelection )
            {
                Font.set(Font.VERDANA_MEDIUM_BOLD_RED);
                title = "> " + title;
            }
            else
                Font.set(Font.VERDANA_MEDIUM_BOLD_GRAY);

            int x = Shell.m_nScreenWidthHalf / 3;
            Font.draw(g, title, x, itemY, Font.FONT_ALIGN_LEFT);
            if( i != m_nLastVisibleItem )
            {
                itemY += Font.getHeight() + indentMenuItems;
                g.drawImage(Gfx.imgStdMenuLine.image, x - (Shell.m_nMarginX * 3), itemY - (indentMenuItems >> 1) , Graphics.TOP|Graphics.LEFT);
            }
        }

        // Show scroll
        if( showScroll )
        {
            // Calculate the scroll
            int scrollX = boxX + boxW + Shell.m_nMarginX;
            int scrollY = y;
            int scrollH = AppShell.usableMenuHeight() - (Shell.m_nMarginY * 3);
            int scrollCursorHeight = scrollH / m_eMenuRoot.branchCount;
            scrollH = scrollCursorHeight * m_eMenuRoot.branchCount;
            int scrollCursorY = y + (m_nCurrentSelection * scrollCursorHeight);

            // draw scroll
            g.setColor(0xb8b7b7);
            int sw = (Shell.m_nScreenWidth * 4) / Shell.m_nScreenWidth;
            for( int i = 0; i < sw; ++i )
                g.drawLine(scrollX + i, scrollY, scrollX + i, scrollY + scrollH);

            // draw scroll cursor
            g.drawImage(Gfx.imgStdMenuScroll.image, scrollX + (sw>>1), scrollCursorY, Graphics.TOP|Graphics.HCENTER);
            //g.setColor(0x053b65);
            //for( int i = 0; i < 4; ++i )
            //    g.drawLine(scrollX + i, scrollCursorY, scrollX + i, scrollCursorY + scrollCursorHeight);
        }
    }
    
    /****
     *  Paint the element
     * 
     * @param g, Graphics object
     */
    public void paint(Graphics g)
    {
        // fool proof
        if( !initialized )
            return;

        // get standard layout
        m_standardLayout.draw_standard_layout(g, this);

        int y = Gfx.imgStdTitle.image.getHeight();

        // Draw subtitle
        g.drawImage(Gfx.imgMenuBackSubtitle.image, 0, y, Graphics.TOP|Graphics.LEFT);
        y += Gfx.imgMenuBackSubtitle.image.getHeight();
        if( Shell.m_nScreenHeight > 160 )
            y += Shell.m_nMarginY;

        // Paint Menu Elements
        if( /*m_nTitle == MAIN_MENU &&*/ Shell.m_nScreenHeight > 160 )
            y += Shell.m_nMarginY << 1;
        else if( /*m_nTitle == MAIN_MENU &&*/ Shell.m_nScreenHeight <= 128 )
            y += Shell.m_nMarginY;
        
        paintMenuElements(g, y);

    }
    
}
