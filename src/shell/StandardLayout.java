package shell;

/**************************
 * StandardLayout class
 * 
 * Draws the standard layout
 * 
 *  Notes:
 *      
 *     
 */
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

// Shell Library
import com.mxme.shell.IStandardLayout;
import com.mxme.shell.Shell;
import com.mxme.shell.Element;

// Internal imports
import app.MainCanvas;
import util.Gfx;

public class StandardLayout implements IStandardLayout
{
    // Soft button animation
    private static final byte ANIM_READY = -1, ANIM_PLAYING = -3;
    private static final short ANIM_DELAY = 0;
    private static short button_animation = ANIM_READY;
    private static long button_timer = System.currentTimeMillis(); // button animation timer

    public boolean hideTerms = false;
    
    /***
     *  Starts the soft delay button animation
     * 
     * @param action: key pressed
     */
    static public void animate_soft_button()
    {
        // need to perform a small button animation before continuing
        if( button_animation == ANIM_READY )
        {
            button_animation = ANIM_PLAYING;
            button_timer = System.currentTimeMillis();
        }
    }
    
    /***
     *  Process the soft delay button animation
     * 
     */
    static public boolean process_soft_button_animation()
    {
        // process the command button animation after the animation is done
        if( button_animation == ANIM_PLAYING )
        {
            if( System.currentTimeMillis() - button_timer > ANIM_DELAY )
            {
                button_animation = ANIM_READY;
                return true;
            }
        }
        
        return false;
    }

    public void drawTerms(Graphics g, Element e)
    {
        // Draw terms
        if( !hideTerms )
        {
            g.drawImage(Gfx.imgStdTermsText.image, 0, Shell.m_nScreenHeight - Gfx.imgStdCmdOk.image.getHeight(), Graphics.BOTTOM|Graphics.LEFT);
            /*
            int termsY = 0, hwY;
            int modY;
            if( (Shell.m_nScreenHeight <= 128) )
                modY = Gfx.imgStdTermsText.image.getHeight() - (Gfx.imgStdTermsText.image.getHeight() >> 1) + (Shell.m_nMarginY << 2);
            else if( (Shell.m_nScreenHeight <= 160) && !hideTerms )
                modY = Gfx.imgStdTermsText.image.getHeight() - (Gfx.imgStdTermsText.image.getHeight() >> 1) + (Shell.m_nMarginY << 1);
            else if( (Shell.m_nScreenHeight <= 208) && !hideTerms )
                modY = Gfx.imgStdTermsText.image.getHeight() - (Gfx.imgStdTermsText.image.getHeight() >> 1);
            else if( Shell.m_nScreenHeight <= 220 )
                modY = Gfx.imgStdTermsText.image.getHeight();
            else
                modY = Gfx.imgStdTermsText.image.getHeight() << 1;

            termsY = Shell.m_nScreenHeight - modY;
            g.drawImage(Gfx.imgStdTermsText.image, Shell.m_nScreenWidthHalf, termsY, Graphics.BOTTOM|Graphics.HCENTER);
            */
        }

    }
    
    /****
     * Draw standard layout background.<br>
     *  <br>
     * This method draws the background using the standard layout.
     * 
     * @param g, Graphics object
     */
    public void draw_background(Graphics g, Element e)
    {
        // Draw background
        //g.drawImage(Gfx.imgStdBackForm.image, 0, Shell.m_nScreenHeight - 1, Graphics.BOTTOM|Graphics.LEFT);
        g.setColor(0xFFFFFF);
        g.fillRect(0, 0, Shell.m_nScreenWidth, Shell.m_nScreenHeight);

        // Draw terms
        drawTerms(g, e);
    }
    
    /****
     * Draw standard layout title.<br>
     *  <br>
     * This method draws the title using the standard layout.
     * 
     * @param g, Graphics object
     */
    public void draw_title(Graphics g, Element e)
    {
        g.drawImage(Gfx.imgStdTitle.image, 0, 0, Graphics.TOP|Graphics.LEFT);
    }
    
    /****
     * Draw standard layout soft buttons.<br>
     *  <br>
     * This method draws the soft buttons using the standard layout.
     * 
     * @param g, Graphics object
     */
    public void draw_command_buttons(Graphics g, Element e)
    {
        //Image soft = Gfx.getImage(Gfx.MENU_SOFT);

        // Redefine margin variable for the standard layout
        Shell.m_nUsableHeight = Shell.m_nScreenHeight - Gfx.imgStdCmdBack.image.getHeight() - Shell.m_nStartY - Shell.m_nMarginY;

        // Draw soft buttons
        boolean isBeingPressed = false;
        if( button_animation == ANIM_PLAYING )
            isBeingPressed = true;

        // Draw left soft button
        if( e.m_nLeftSoftButton != Shell.m_nHidden )
        {
            // Exception with the Back button in this project
            Image leftImage = Shell.getSoftButton(e.m_nLeftSoftButton);
            if( e.m_nLeftSoftButton == Shell.m_nIDBack )
                leftImage = Gfx.imgStdCmdBackLeft.image;
            g.drawImage(leftImage, 0, MainCanvas.screen_height, Graphics.BOTTOM|Graphics.LEFT);
        }
        // Draw right soft button
        if( e.m_nRightSoftButton != Shell.m_nHidden )
            g.drawImage(Shell.getSoftButton(e.m_nRightSoftButton), MainCanvas.screen_width, MainCanvas.screen_height, Graphics.BOTTOM|Graphics.RIGHT);
    }
    
    /****
     * Draw standard layout.<br>
     *  <br>
     * This method draws a standard layout.
     * 
     * @param g, Graphics object
     */
    public void draw_standard_layout(Graphics g, Element e)
    {
        draw_background(g, e);
        draw_title(g, e);
        draw_command_buttons(g, e);
    }
    
}
