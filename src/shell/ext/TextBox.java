package shell.ext;

/**************************
 * TextBox class
 * 
 * 
 * 
 *  Notes:
 *      
 *     
 */
import com.mxme.common.MXMEConstants;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.Canvas;

// MatuX lib
import com.mxme.shell.core.MultiTap;
import com.mxme.shell.Shell;

// Game
import shell.AppShell;
import util.*;

public class TextBox extends MultiTap
{
    // Caret titilation
    private static final short CARET_SPEED = 250;
    private long caretTimer;
    private boolean showCaret;

    // Internal vars
    private String defaultText;
    private Image textBox;
    private int fontId, textBoxMarginX, textBoxMarginY;
    private int width, firstIndexShow, lastIndexShow;

    // Public vars
    public boolean showDefaultText;
    public int x, y;
    public char maskChar = ' ';
    public short softDefaultLeft = Shell.m_nIDMenu,
                 softDefaultRight = Shell.m_nIDClear;

    /**
     * TextBox constructor
     *
     * @param defaultText
     * @param fontId
     * @param textBox
     */
    public TextBox(String defaultText, int fontId, Image textBox)
    {
        super(0);
        
        this.defaultText = defaultText;
        this.fontId = fontId;
        this.textBox = textBox;

        if( textBox != null )
            width = textBox.getWidth() - (Shell.m_nMarginX << 2);
    }
    
    /****
     *  Initialize this element
     * 
     * @param none
     */
    public void initialize()
    {
        super.initialize();
        showDefaultText = true;
        x = 0;
        y = 0;
        isEnabled = false;
    }

    /**
     * Give focus to the component
     * 
     */
    public void enable()
    {
        super.enable();
        setSoftButtons(softDefaultLeft, softDefaultRight);
        caretTimer = System.currentTimeMillis();
    }

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

    protected boolean moveRight()
    {
        boolean r = super.moveRight();

        //if( r )
        //{
            // compute first and last index show
            // get the length of the whole word
            int prevFontId = Font.font_id;
            Font.set(fontId);
            String s = getString();

            int l = Font.len(s);
            // performance
            if( l < width )
            {
                firstIndexShow = 0;
                lastIndexShow = s.length();
            }
            else
            {
                if( lastIndexShow < s.length() ) {
                    firstIndexShow++;
                    lastIndexShow++;
                }
            }

            Font.set(prevFontId);
        //}

        return r;
    }

    protected boolean moveLeft()
    {
        boolean r = super.moveLeft();

        //if( r )
        //{
            // compute first and last index show
            // get the length of the whole word
            int prevFontId = Font.font_id;
            Font.set(fontId);
            String s = getString();

            int l = Font.len(s);
            // performance
            if( l < width )
            {
                firstIndexShow = 0;
                lastIndexShow = s.length();
            }
            else
            {
                if( firstIndexShow > 0 ) {
                    firstIndexShow--;
                    lastIndexShow--;
                }
            }

            Font.set(prevFontId);
        //}

        return r;
    }

    public void set(String s)
    {
        super.set(s);

        // get the length of the whole word
        int prevFontId = Font.font_id;
        Font.set(fontId);

        int total = 0, i = 0;
        for( i = 0; m_cText[i] != ' ' && total < width; i++ )
            total += Font.getWidth(Font.font_id, m_cText[i]);

        firstIndexShow = 0;
        lastIndexShow = i;

        Font.set(prevFontId);
    }

    /****
     *  Process the element
     * 
     * @param int p_nData, for <i>Sound On/Off</i> used to know what sound to play if On
     */
    public boolean keyPressed(int p_nAction, int keyCode) 
    { 
        boolean handled = false;
        handled = super.keyPressed(p_nAction, keyCode);

        if( !handled && keyCode != Canvas.FIRE && (p_nAction == AppShell.m_nIDClear || keyCode == MXMEConstants.SOFTKEY_C) )
        {
            clearCurChar();
            handled = true;
        }

        if( handled )
            showDefaultText = false;

        return handled;
    }
    
    private boolean validName()
    {
        return true; 
    }
    
    private boolean nullName()
    {
        for( int i = 0; i < m_cText.length; i++ )
            if( m_cText[i] != ' ' )
                return false;
        
        return true;
    }

    /**
     * keyReleased function
     * 
     * @param p_nAction
     * @param keyCode
     * @return
     *
     */
    public boolean keyReleased(int p_nAction, int keyCode) 
    { 
        return super.keyReleased(p_nAction, keyCode);
    }
    
    /****
     *  Paint the element
     * 
     * @param g, Graphics object
     *
     */
    public void paint(Graphics g)
    {
        int prevFontId = Font.font_id;
        Font.set(fontId);

        super.paint(g);

        // Set positions
        int fontY = y + (textBox.getHeight() >> 1) - (Font.getHeight() >> 1);
        int fontX = x + Shell.m_nMarginX;

        // Draw Text Box
        g.drawImage(textBox, x, y, Graphics.TOP|Graphics.LEFT);

        // Draw Text Box text
        int /*begI = 0, endI = 0,*/ caretX = 0;
        if( showDefaultText )
        {
            Font.draw(g, defaultText, fontX, fontY, Font.FONT_ALIGN_LEFT);
            caretX = fontX;
        }
        else if( m_cText[0] != ' ' )
        {
            String text = getString();
            if( maskChar != ' ' )
            {
                String predictText = getString();
                boolean hideChar = timerTillNextChar == 0;
                text = new String();
                char[] cText = new char[predictText.length()];
                for( int i = 0; i < predictText.length(); ++i )
                    cText[i] = '*';
                if( !hideChar )
                    cText[curPos] = m_cText[curPos];
                text = new String(cText);
            }

            /*// calculate text position
            int total = 0;
            int textW = textBox.getWidth() - (Shell.m_nMarginX << 2);
            //if( Shell.m_nScreenWidth < 176 )
            //    textW -= (Shell.m_nMarginX << 1);
            for( endI = 0; endI < text.length() && total < textW; endI++ )
                total += Font.getWidth(Font.font_id, text.charAt(endI));

            int finalCurPos = curPos;
            int oCurPos = curPos;
            if( oCurPos > endI )
            {
                //if( Shell.m_nScreenWidth < 176 && timerTillNextChar != 0 )
                //    oCurPos++;
                begI = oCurPos - endI;
                endI += begI;
                finalCurPos -= begI;
            }*/

            // compute caret position
            int finalCurPos = curPos - firstIndexShow;
            caretX = maskChar != ' ' ?
                        fontX + Font.getWidth(Font.font_id, maskChar) * finalCurPos :
                        fontX + Font.len(String.valueOf(m_cText, firstIndexShow, finalCurPos));

            Font.draw(g, text.substring(firstIndexShow, timerTillNextChar != 0 ? lastIndexShow + 1 : lastIndexShow), fontX, fontY, Font.FONT_ALIGN_LEFT);
        }

        // Draw caret (aka cursor)
        if( System.currentTimeMillis() - caretTimer > CARET_SPEED )
        {
            caretTimer = System.currentTimeMillis();
            showCaret = !showCaret;
        }

        if( isEnabled && showCaret)
        {
            // draw caret
            g.setColor(0);
            if( caretX == 0 ) caretX = fontX;
            int caretH = Font.getHeight();
            if( Shell.m_nScreenHeight <= 160 )
            {
                fontY += 1;
                caretH -= 3;
            }
            g.drawLine(caretX, fontY, caretX, fontY + caretH);
        }

        Font.set(prevFontId);
    }
    
}
