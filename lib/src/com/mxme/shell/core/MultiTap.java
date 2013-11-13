package com.mxme.shell.core;

/**************************
 * MultiTap class
 * 
 * 
 * 
 *  Notes:
 *      
 *     
 */
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Canvas;

import com.mxme.shell.Element;

public abstract class MultiTap extends Element
{
    // Private
    public byte maxChars = 127;
    protected byte curPos;
    protected int lastKeyNum, curChar, delayTillNextChar = 1000;
    protected long timerTillNextChar;
    private int len;
            
    // Multitap
    private static final String[] LETTERS = new String[] {
            "0",
            "@._1",
            "abc2",
            "def3",
            "ghi4",
            "jkl5",
            "mnoñ6",
            "pqrs7",
            "tuv8",
            "wxyz9" };

    private static final String[] NUMBERS = new String[] {
            "0",
            "1",
            "2",
            "3",
            "4",
            "5",
            "6",
            "7",
            "8",
            "9" };

    
    // Protected
    protected char m_cText[] = new char[maxChars];
    protected boolean m_bUpdating;
    protected boolean m_insertMode;
    protected boolean m_dontInsert;
    protected boolean m_bOnlyNumbers;
    
    // Public
    public MultiTap(int p_nTitle)
    {
        m_nTitle = p_nTitle;
        m_bOnlyNumbers = false;
        initialize();
    }
    
    /****
     *  Initialize this element
     * 
     * @param none
     */
    public void initialize()
    { 
        // reset name array
        for( int i = 0; i < maxChars; ++i )
            m_cText[i] = ' ';
        
        resetVariables(false);
    }

    /****
     *  Deinitialize this element
     *
     * @param none
     */
    public void deinitialize()
    {

    }

    public void set(String s)
    {
        for( int i = 0; i < s.length(); i++ )
            m_cText[i] = s.charAt(i);
        curPos = (byte)s.length();
        calcLen();
    }

    public void clear()
    {
        initialize();
        set("");
    }
    
    public void resetVariables(boolean update)
    {
        m_bUpdating = update;
        // reset internal variables
        lastKeyNum = -1;
        curChar = 0;
        curPos = 0;
        len = 0;
        timerTillNextChar = 0;
        m_insertMode = true; // default
        m_dontInsert = false;
    }
    
    private void resetMultiTap()
    {
        curChar = 0;
        lastKeyNum = -1;
        timerTillNextChar = 0;
        m_dontInsert = false;
    }

    protected boolean moveRight()
    {
        int currentCurPos = curPos;

        if( m_cText[curPos] != ' ' )
            if( ++curPos >= m_cText.length )
                curPos = (byte)(m_cText.length-1);

        return currentCurPos != curPos;
    }

    protected boolean moveLeft()
    {
        int currentCurPos = curPos;

        if( --curPos < 0 )
            curPos = 0;

        return currentCurPos != curPos;
    }

    protected void calcLen()
    {
        int i;
        for( i = 0; m_cText[i] != ' '; ++i )
            continue;
        len = i;
    }

    /****
     *  Process the element
     * 
     * @param none
     */
    public boolean keyPressed(int action, int keyCode)
    {
        boolean handled = false;
        
        if( keyCode >= Canvas.KEY_NUM0 && keyCode <= Canvas.KEY_NUM9 )
        {
            timerTillNextChar = System.currentTimeMillis();
            int pressedKeyNum = keyCode - Canvas.KEY_NUM0;
            if( pressedKeyNum != lastKeyNum || m_bOnlyNumbers )
            {
                if( len >= maxChars )
                    return true;

                if( lastKeyNum != -1 ) {
                    moveRight();
                    m_dontInsert = false;
                }
                curChar = 0;
                lastKeyNum = pressedKeyNum;
            }

            // Move all chars to the right if in insert mode
            if( m_insertMode && m_cText[0] != ' ' && curPos < (maxChars - 1) && m_cText[maxChars - 1] == ' ' && m_cText[curPos + 1] != ' ' && !m_dontInsert)
            {
                // Look for the last entry
                int lastEntry;
                for( lastEntry = curPos; m_cText[lastEntry] != ' '; ++lastEntry )
                    continue;

                // Go backwards moving one char at a time
                for( int curEntry = lastEntry; curEntry > curPos; --curEntry )
                    m_cText[curEntry] = m_cText[curEntry - 1];
            }

            String []array = m_bOnlyNumbers ? NUMBERS : LETTERS;
            m_cText[curPos] = array[pressedKeyNum].charAt(curChar++ % (maxChars-1));
            if( curChar >= array[pressedKeyNum].length() ) curChar = 0;
            m_dontInsert = true;

            calcLen();
            
            handled = true;
       }
        else if( action == Canvas.RIGHT )
        {
            handled = moveRight();
            resetMultiTap();
            //handled = true;
        }
        else if( action == Canvas.LEFT )
        {
            handled = moveLeft();
            resetMultiTap();
            //handled = true;
        }
        
        return handled;
    }
    
    public boolean keyReleased(int p_nAction, int keyCode)
    {
        return false;
    }

    public String getString()
    {
        String finalText = new String();
        for( int i = 0; m_cText[i] != ' '; ++i )
            finalText += m_cText[i];
        return finalText;
    }

    public int getCurPos()
    {
        return curPos;
    }

    public void onlyNumbers(boolean val)
    {
        m_bOnlyNumbers = val;
    }
        
    /**
     * Clears the current character and
     * accomodates the rest
     */
    public void clearCurChar()
    {
        if( curPos != 0 )
        {
            if( timerTillNextChar != 0 || (curPos == maxChars - 1 && m_cText[curPos] != ' ') )
                m_cText[curPos] = ' ';
            else
            {
                for( int i = curPos - 1; m_cText[i] != ' '; ++i )
                {
                    if( i == maxChars - 1 )
                    {
                        m_cText[i] = ' ';
                        break;
                    }

                    m_cText[i] = m_cText[i + 1];
                }

                moveLeft();
            }

            calcLen();
            resetMultiTap();
        }
    }

    /****
     *  Paint the element
     * 
     * @param g, Graphics object
     */
    public void paint(Graphics g)
    {
        if( timerTillNextChar != 0 && (System.currentTimeMillis() - timerTillNextChar > delayTillNextChar || m_bOnlyNumbers) )
        {
            moveRight();
            resetMultiTap();
        }
        
    }

}
