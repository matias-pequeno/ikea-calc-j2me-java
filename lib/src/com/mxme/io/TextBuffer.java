package com.mxme.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Class used for text internationalization
 * 
 * @author MatuX
 */
public class TextBuffer
{
    private String[] m_lines;
    
    public TextBuffer()
    {

    }
    
    /**
     * Initializes the text buffer
     * @param textInputStream
     * @param unicodeInputStream TODO
     * @param maxLineLength
     * @param lineCount
     * @return
     */
    public boolean initialize(InputStream textInputStream, InputStream unicodeInputStream, int maxLineLength, int lineCount)
    {
        boolean res = true;
        if( textInputStream != null )
        {
        	Reader r = new InputStreamReader(textInputStream);
        	char[] buffer = new char[maxLineLength];
        	m_lines = new String[lineCount];
        	try
            {
        		for (int i = 0; i < lineCount; i++)
        			m_lines[i] = TextBuffer.readLine(r, buffer);
        	} 
            catch (IOException e)
            {
        		res = false;
        	} 
            finally
            {
        		buffer = null;
        		try {
        			r.close();
        		} catch (Exception e) { }

                r = null;
        	}
        } 
        else
        {
        	Reader r = new InputStreamReader(unicodeInputStream);
        	char[] buffer = new char[maxLineLength];
        	byte[] auxBuffer = new byte[maxLineLength / 2];
        	m_lines = new String[lineCount];
        	
            try
            {
        		for (int i = 0; i < lineCount; i++)
                {
        			String str = TextBuffer.readLine(r, buffer);
        			if (str != null)
                    {
        				int pos = -1;
        				boolean completed = false;
        				int j = 0;
        				while( !completed )
                        {
        					String auxStr;
        					if (j > 0)
        						pos = str.indexOf(',', pos + 1);

                            int nextPos = str.indexOf(',', pos + 1);
        					
                            if (nextPos == -1)
                            {
        						completed = true;
        						auxStr = str.substring(pos + 1, str.length());
        					} else {
        						auxStr = str.substring(pos + 1, nextPos);
        					}
//        					System.out.println(i + ", " + j + "->" + auxStr + "|");
        					auxBuffer[j++] = Byte.parseByte(auxStr);
        					auxStr = null;
        				}
        				byte[] finalByteArray = new byte[j];
        				System.arraycopy(auxBuffer, 0, finalByteArray, 0, j);
        				m_lines[i] = new String(finalByteArray, "UTF-8");
        				finalByteArray = null;
        			} else {
        				m_lines[i] = null;
        			}
        		}
        	} catch (IOException e) {
        		res = false;
        	} finally {
        		buffer = null;
        		try {
        			r.close();
        		} catch (Exception e) {
        		}
        		r = null;
        	}
        }
        return res;
    }
    
    /**
     * Reads a line
     * @return
     * @throws IOException
     */
    public static String readLine(Reader r, char[] buffer) throws IOException {
    	String res = null;
    	try {
    		int c;
    		int index;
    		boolean trucking = true;
    		index = 0;
    		while (trucking) {
    			c = r.read();
//    			System.out.println("c->" + (int)c);
    			if (c == '\n' || c == -1) trucking = false;
    			else if (c != '\r') buffer[index++] = (char)c;
    		}
//    		System.out.println(index);
    		if (index == 0) {
    			res = null;
    		} else {
    			res = new String(buffer, 0, index);
    		}
    	} catch (Exception e) {
    	}
    	return res;
    }
    
    /**
     * Gets a text line
     * @param index
     * @return
     */
    public String getText(int index) {
        String res = null;
        if (index > -1 && index < m_lines.length) {
            res = m_lines[index];
        }
        return res;
    }
    
    /**
     * Destroys the text buffer
     */
    public void destroy() {
        m_lines = null;
    }
}
