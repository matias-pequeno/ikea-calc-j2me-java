package com.mxme.lcdui;

import javax.microedition.lcdui.Image;

public class BitmapFont {
    /**
     * Image used by the font
     */
    private Image m_image;
    
    /**
     * Chars that are represented by this font
     */
    private String m_representedChars;
    
    /**
     * Width of each character
     */
    private short m_charWidth;
    
    /**
     * Height of each character
     */
    private short m_charHeight;
    
    private short m_gapDif;
    private short m_charsPerRow;
    
    
    /**
     * Constructor
     * @param image
     * @param representedChars
     */
    public BitmapFont(Image image, short rowCount, String representedChars) {
        m_image = image;
        m_representedChars = representedChars;
        m_charsPerRow = (short) (representedChars.length() / rowCount);
        m_charWidth = (short) (m_image.getWidth() / m_charsPerRow);
        m_charHeight = (short)( m_image.getHeight() / rowCount);
    }
    
    /**
     * Constructor
     * @param image
     * @param representedChars
     */
    public BitmapFont(Image image, short rowCount, 
    		String representedChars, short gapDif) {
        m_image = image;
        m_representedChars = representedChars;
        m_charsPerRow = (short) (representedChars.length() / rowCount);
        m_charWidth = (short) (m_image.getWidth() / m_charsPerRow);
        m_charHeight = (short)( m_image.getHeight() / rowCount);
        m_gapDif = gapDif;
    }
    
    /**
     * Constructor
     * @param image
     * @param representedChars
     */
    public BitmapFont(Image image, String representedChars) {
        m_image = image;
        m_representedChars = representedChars;
        m_charWidth = (short) (m_image.getWidth() / representedChars.length());
        m_charHeight = (short)( m_image.getHeight());
        m_charsPerRow = (short) representedChars.length();
    }
    
    /**
     * Constructor
     * @param image
     * @param representedChars
     */
    public BitmapFont(Image image, String representedChars, short gapDif) {
        m_image = image;
        m_representedChars = representedChars;
        m_charWidth = (short) (m_image.getWidth() / representedChars.length());
        m_charHeight = (short)( m_image.getHeight());
        m_gapDif = gapDif;
        m_charsPerRow = (short) representedChars.length();
    }
    
    /**
     * Destroys the bitmap font
     */
    public void destroy() {
        m_image = null;
        m_representedChars = null;
    }
    
    /**
     * Draws a string (from a StringBuffer) using the font
     * @param g Graphics object to use
     * @param painter Painter to use
     * @param x Left coord
     * @param y Top coord
     * @param strBuffer StringBuffer to use
     * @param offset Initial position in the StringBuffer
     * @param length Length of the String that will be drawn
     */
    public void drawString(Object g, IPainter painter, int x, int y, 
            StringBuffer strBuffer, int offset, int length) {
        drawString(g, painter, x, y, 
                strBuffer.toString().substring(offset, length));
    }
    
    /**
     * Draws a string using the font
     * @param g Graphics object to use
     * @param painter Painter to use
     * @param x Left coord
     * @param y Top coord
     * @param str String to be drawn
     */
    public void drawString(Object g, IPainter painter, 
            int x, int y, String str) {
        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            int pos = m_representedChars.indexOf(str.charAt(i));
            int posX = pos % m_charsPerRow;
            int posY = pos / m_charsPerRow;
            if (pos != -1) {
                int srcX = m_charWidth * posX;
                int srcY = m_charHeight * posY;
                painter.drawRegion(g, m_image, srcX, 
                        srcY, x, y, m_charWidth, m_charHeight, 0);
            }
            x += m_charWidth - m_gapDif;
        }
    }
    
    /**
     * Gets the drawing width of a string
     * @param str String
     * @param The width
     */
    public int getStringWidth(String str) {
        return (m_charWidth - m_gapDif) * str.length();
    }
    
    /**
     * Gets the drawing height of a character
     * @param The height
     */
    public int getCharHeight() {
        return m_charHeight;
    }
    
    /**
     * Gets the drawing width of a character
     * @param The width
     */
    public int getCharWidth() {
        return m_charWidth;
    }
}
