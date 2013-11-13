package util;

/**************************
 * Font class
 * 
 * Manages all Fonts, including loading and drawing
 * 
 *  @todo I should do a Font class that stores all the information
 *        about a single font, that Font class should have functions like:
 *              load() and draw().
 *        Much easier way to manage fonts as this is a loosely adaptation from
 *        a non-OOP very structured code.
 *
 */
import com.mxme.common.Common;
import java.io.*;
import javax.microedition.lcdui.*;

import app.MainCanvas;

public class Font 
{
    private static final String PNG_FONT[] = { "fon0", "fon1", "fon2", "fon3", "fon4" };
    
    //#if FONT_SINGLE == "true"
    //# public static int FONT_COUNT = 1;
    //#else
    public static int FONT_COUNT = PNG_FONT.length;
    //#endif
    public static final int FONT_USE_CLIP = 4;
    public static final int FONT_OVERRIDE_FLICKER = 64;
    public static final int SCROLL_STANDARD = 128;
    public static final int SCROLL_CREDITS_LIKE = 256;
    public static final int SCROLL_DEFAULT = SCROLL_CREDITS_LIKE;
    // Font opt constants
    public static final int 
            FONT_ALIGN_TOP = 0,
            FONT_ALIGN_LEFT = 0,
            FONT_ALIGN_CENTER = 1,
            FONT_ALIGN_RIGHT = 2,
            FONT_ALIGN_BOTTOM = 8,
            FONT_ALIGN_CURVED = 16,
            FONT_ALIGN_VCENTER = 32;
    // Font IDs
    public static final int 
            VERDANA_MEDIUM_BOLD_GRAY = 0,
            VERDANA_MEDIUM_BOLD_RED = 1,
            VERDANA_MEDIUM_NUMBERS = 2,
            VERDANA_SMALL_BOLD_GRAY = 3,
            VERDANA_SMALL_GRAY = 4,
            FONT_DEFAULT = 0;
    
    // Default Font
    public static int font_id = FONT_DEFAULT;
    
    // Scroll variables
    private static String scroll_last_string;
    public static int scrollLines;
    private static String scroll_s[] = new String[128];
    private static StringBuffer line_buffer = new StringBuffer();
    private static StringBuffer scroll_word = new StringBuffer();
    
    // This function loads a PNGile formated file with fonts clipping
    private static int font_count[];
    private static short font_x[][];
    private static short font_y[][];
    private static byte font_w[][];
    private static byte font_h[][];
    private static Image img_font[];
    
    private static final void initScroll(String s, int limit_x)
    {
        line_buffer.delete(0, line_buffer.length());
        scroll_word.delete(0, scroll_word.length());
        scroll_last_string = s;
        scrollLines = 0;
        char c;
        int char_count = s.length() - 1;
        int width_count = getWidth(font_id, 'A');
        int char_width;

        for( int i = 0; i <= char_count; i++ )
        {
            c = s.charAt(i);
            char_width = getWidth(c);

            // Append char to word
            if( c != '\n' )
            {
                scroll_word.append(c);
                width_count += char_width;
            }

            // Append word to this line
            if( c == ' ' || c == '\n' || i == char_count ) // || c == '.' || c == ',' || c == ':' <- not needed, there is always a space after ., , and :
            {
                line_buffer.append(scroll_word);
                scroll_word.delete(0, scroll_word.length());
            }

            // Advance to next line.
            if( c == '\n' || width_count >= limit_x || i == char_count )
            {
                //if( line_buffer.length() > 0 )
                //{
                    if ( line_buffer.length() > 0 && line_buffer.charAt(line_buffer.length()-1) == ' ' )
                    {
                        line_buffer.deleteCharAt(line_buffer.length()-1);
                        width_count -= getWidth(font_id, ' ');
                    }
                    
                    width_count = getWidth(font_id, c) + scroll_word.length() * char_width;
                    scroll_s[scrollLines] = line_buffer.toString();
                    scrollLines++;
                    line_buffer.delete(0, line_buffer.length());
                //}
            }
        }
    }
    
    public static final void load()
    {
        font_count = new int[FONT_COUNT];
        font_x = new short[FONT_COUNT][];
        font_y = new short[FONT_COUNT][];
        font_w = new byte[FONT_COUNT][];
        font_h = new byte[FONT_COUNT][];
        img_font = new Image[FONT_COUNT];

        for( int font_num = 0; font_num < FONT_COUNT; font_num++ )
        {
            int use_font = font_num;
            //#if FONT_SINGLE == "true"
            //# use_font = 0;
            //#endif
            img_font[font_num] = Common.loadImage(PNG_FONT[use_font]);
            
            int i;
            int c;
            // int pos;
            StringBuffer buffer = new StringBuffer();
            InputStream is = buffer.getClass().getResourceAsStream("/" + PNG_FONT[use_font]);
            DataInputStream dis = new DataInputStream(is);
            
            try
            {
                font_count[font_num] = dis.readUnsignedByte();
                font_x[font_num] = new short[255];
                font_y[font_num] = new short[255];
                font_w[font_num] = new byte[255];
                font_h[font_num] = new byte[255];

                for( i = 0; i < font_count[font_num]; i++ )
                {
                    c = dis.readUnsignedByte();
                    font_x[font_num][c] = (short) dis.readUnsignedByte();
                    font_y[font_num][c] = (short) dis.readUnsignedByte();
                    font_w[font_num][c] = (byte) dis.readUnsignedByte();
                    font_h[font_num][c] = (byte) dis.readUnsignedByte();
                }
            }
            catch( Exception e )
            {
                //#if DEBUG == "true"
                    e.printStackTrace();
                    System.out.println(e.getMessage());
                //#endif
            }
        }

        // Set default font
        font_id = FONT_DEFAULT;
    }

    /****
     * Returns the width of a string
     * 
     * @param String s, String to measure
     */
    public static final int len(String s)
    {
        int total = 0;
        for( int i = s.length(); i-- > 0;)
            total += getWidth(font_id, s.charAt(i));

        return total;
    }
    
    public static final int getHeight()
    {
        int h = font_h[font_id]['A'];
        int h2 = font_h[font_id]['0'];
        // some fonts are only-numbers, this
        // is kind of tricky, though.
        // we should go through all the chars
        // in the font and grab the highest
        // height?
        return (h2 > h)?h2:h;
    }
    
    public static final int getHeight(char c)
    {
        return font_h[font_id][c];
    }
    
    public static final int getHeight(int id, char c)
    {
        return font_h[id][c];
    }
    
    public static final int getWidth(char c)
    {
        return font_w[font_id][c];
    }
    
    public static final int getWidth(int id, char c)
    {
        return font_w[id][c];
    }
    
    public static final void set(int font)
    {
        font_id = font;
    }

    /**
     *  Draws a string using the current font
     * 
     * @param g: Graphics reference
     * @param s: String to be drawn
     * @param x: X Coordinate
     * @param y: Y Coordinate
     * @param opt: specifies the opt for the string (left, center, right, bottom, with clipping).<br>
     * 
     * See CONSTANTS:<br>
     *  <b><i>FONT_ALIGN_LEFT<br>
     *  FONT_ALIGN_RIGHT<br>
     *  FONT_ALIGN_BOTTOM<br>
     *  FONT_ALIGN_CENTER<br>
     *  FONT_ALIGN_TOP<br>
     *  FONT_USE_CLIP</i></b>
     * 
     * @return
     */
    public static final int draw(Graphics g, String s, int x, int y, int align)
    {
        if( s.length() == 0 )
            return -1;

        int backup_clip_x = g.getClipX();
        int backup_clip_y = g.getClipY();
        int backup_clip_width = g.getClipWidth();
        int backup_clip_height = g.getClipHeight();
        
        Image img = img_font[font_id];

        int i = 0;
        boolean ahead = true;
        boolean noClipping = true;
        boolean doCurve = false;
        int charCount = s.length();
        char c;

        // Set up drawing
        if( align > 0 )
        {
            if( (align & FONT_USE_CLIP) != 0 )
                noClipping = false;

            if( (align & FONT_ALIGN_CENTER) != 0 )
                x -= (len(s) >> 1) - 4;
            else if( (align & FONT_ALIGN_RIGHT) != 0 )
            {
                ahead = false;
                i = charCount - 1;
                x -= getWidth(s.charAt(i));
            }
            if( (align & FONT_ALIGN_BOTTOM) != 0 )
                y -= font_h[font_id][s.charAt(0)];
            if( (align & FONT_ALIGN_CURVED) != 0 )
                doCurve = true;
        }

        // Draw all the characters
        while( true )
        {
            c = s.charAt(i);

            if( noClipping )
            {
                g.setClip(x, y, getWidth(c), getHeight(c));
                g.drawImage(img, x - font_x[font_id][c], y - font_y[font_id][c], 0);
            }
            else
            {
                int clip_x = g.getClipX();
                int clip_y = g.getClipY();
                int clip_width = g.getClipWidth();
                int clip_height = g.getClipHeight();
                g.clipRect(x, y, getWidth(c), getHeight(c));
                g.drawImage(img, x - font_x[font_id][c], y - font_y[font_id][c], 0);       
                g.setClip(clip_x, clip_y, clip_width, clip_height);
            }

            i += ahead?1:-1;
            if( !(i >= 0 && i < charCount) ) break;
            align = getWidth(ahead?c:s.charAt(i));
            x += ahead?align:-align;
            // horizontal indentation
            //x++;
            
            if( doCurve )
                y += getHeight(c)/20;
        }
        
        g.setClip(backup_clip_x, backup_clip_y, backup_clip_width, backup_clip_height);

        return x;
    }

    public static int getFullScrollHeight()
    {
        int indent = getHeight() >> 3;
        return scrollLines * (getHeight() + indent);
    }

    /**
     *  Draws a multiline string according to a given scroll.
     * 
     * @param g: Graphics reference
     * @param s: String to be drawn
     * @param x: X Coordinate to start drawing
     * @param y: Y Coordinate to start drawing
     * @param width: Specifies the limit x where it should stop and start a new
     *        line (in pixels)
     * @param height: Specifies the limit y where it should draw (in pixels)
     * @param scroll: Specifies from which part of the text will start drawing
     *        (in pixels).
     * @param opt: Options (Align, Scroll, Rendering opts)
     * 
     * @return boolean, true if end of scroll
     */
    public static final boolean drawScroll(Graphics g, String s, int x, int y, int width, int height, int scroll, int opt)
    {
        int backup_clip_x = g.getClipX();
        int backup_clip_y = g.getClipY();
        int backup_clip_width = g.getClipWidth();
        int backup_clip_height = g.getClipHeight();
        
        if( scroll_last_string != s )
            initScroll(s, width);

        int scrollType = SCROLL_DEFAULT;
        if( (opt & SCROLL_CREDITS_LIKE) != 0 )
            scrollType = SCROLL_CREDITS_LIKE;
        else if( (opt & SCROLL_STANDARD) != 0 )
            scrollType = SCROLL_STANDARD;

//#if GFX_RES_HIGH == "true"
        if( (opt & FONT_ALIGN_VCENTER) != 0 )
            y = y - (getFullScrollHeight() >> 1); //(y + (height >> 1));//
//#elif GFX_RES_MED == "true"
//#         if( ((opt & FONT_ALIGN_VCENTER) != 0) && scrollLines > 2 )
//#             y = (y + (height >> 1)) - (getFullScrollHeight() >> 1);
//#elif GFX_RES_LOW == "true" || GFX_RES_ULTRALOW == "true"
//#         //if( ((opt & FONT_ALIGN_VCENTER) != 0) && scrollLines > 2 )
//#         //        y += Font.getHeight(); //(y + (height >> 1)) - (getFullScrollHeight() >> 1);
//#endif
        
        if( (opt & FONT_USE_CLIP) == 0 )
            g.setClip(0, y, MainCanvas.screen_width, height);
        else
            g.setClip(0, 0, MainCanvas.screen_width, MainCanvas.screen_height);
        
        //*
        //#if DEBUG == "true"
            g.setColor(0xFF00FF);
            g.drawRect(x, y, width - 1, height - 1);
        //#endif
        //*/

        if( (opt & FONT_USE_CLIP) == 0 )
        {
            if( (opt & FONT_ALIGN_CENTER) != 0 )
                x += width >> 1;
            opt |= 4;
        }

        int draw_y = y - scroll;

        if( scrollType == SCROLL_CREDITS_LIKE )
        {
            draw_y += height;
            scroll -= height;
        }
        else if( scrollType == SCROLL_STANDARD )
            scroll += height;
        
        height += y + getHeight();
        y -= getHeight();
        int indent = getHeight() >> 3;
        int end_y = getFullScrollHeight();

        boolean scrollEnd = (scrollType == SCROLL_STANDARD && scroll > end_y);
        //if( scrollEnd )
        //    draw_y = (y + getHeight()) - (end_y - (height - ((y + getHeight()) + getHeight())));

//#if GFX_RES_LOW == "true" || GFX_RES_ULTRALOW == "true"
//#         // SUPER hack for La Subastada
//#         if( ((opt & FONT_ALIGN_VCENTER) != 0) && scrollLines > 2 )
//#             draw_y += (getHeight() >> 1);
//#endif
        
        //#if NO_FLICKER == "true"
//#             if( (opt & FONT_OVERRIDE_FLICKER) == 0 )
//#                 draw_y = (draw_y / (font_height / 2)) * (font_height / 2);
        //#endif

        for( int i = 0; draw_y < height; ++i )
        {
            if( draw_y >= y && i < scrollLines )
                draw(g, scroll_s[i], x, draw_y, opt);
            draw_y += getHeight() + indent;
        }
        
        // reset clip
        g.setClip(backup_clip_x, backup_clip_y, backup_clip_width, backup_clip_height);
        
        return scroll > end_y || scrollEnd;
    }
    
    /**
     *  Draws a multiline string
     * 
     * @param g: Graphics reference
     * @param s: String to be drawn
     * @param x: X Coordinate to start drawing
     * @param y: Y Coordinate to start drawing
     * @param width: Specifies the limit x where it should stop and start a new
     *        line (in pixels)
     * @param opt: Type of opt (0 left opt, 1 center opt)
     * 
     * @return void
     */
    public static final void drawMultiline( Graphics g, String s, int x, int y, int width, int align )
    {
        drawMultiline(g, s, x, y, width, 0, align);
    }

    public static final void drawMultiline( Graphics g, String s, int x, int y, int width, int height, int align )
    {
        if( scroll_last_string != s )
            initScroll(s, width);
        if( height == 0 )
            height = scrollLines * getHeight() + getHeight();
        drawScroll( g, s, x, y, width, height, (scrollLines + 1) * getHeight(), align | FONT_OVERRIDE_FLICKER );
    }

}
