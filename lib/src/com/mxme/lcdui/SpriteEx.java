package com.mxme.lcdui;

import javax.microedition.lcdui.Image;

import com.mxme.lcdui.Sprite;
import com.mxme.common.MXMEConstants;
import com.mxme.common.Common;

/****
 *  SpriteEx class
 * 
 * Wrapper for the MatuX library Sprite class. Contains some extra functions.
 * 
 */
public class SpriteEx 
{
    // Global vars
    public static final byte DEFAULT_FPS = 10;

    Sprite m_sprite = null;
    
    // properties
    boolean m_flipHorizontal,
            m_hasAnimation, 
            m_animLoops,
            m_animPlaysOnce;
    byte[] m_spriteFrames;
    String m_fileName;
    String[] m_fileNames;
    
    /**
     *  Constructor for single file sprites
     * 
     * @param 
     * @return 
     */
    public SpriteEx(String fileName, boolean flipHorizontal, boolean hasAnimation, byte[] spriteFrames, boolean animLoops, boolean animPlaysOnce)
    {
        this.m_fileName = fileName;
        init(flipHorizontal, hasAnimation, spriteFrames, animLoops, animPlaysOnce);
    }

    /**
     *  Constructor for multi-files sprites
     * 
     * @param 
     * @return 
     */
    public SpriteEx(String[] fileNames, boolean flipHorizontal, boolean hasAnimation, byte[] spriteFrames, boolean animLoops, boolean animPlaysOnce)
    {
        // Java = Basura
        m_fileNames = fileNames;
        //m_fileNames = new String[fileNames.length];
        //System.arraycopy(fileNames, 0, m_fileNames, 0, fileNames.length);
        
        init(flipHorizontal, hasAnimation, spriteFrames, animLoops, animPlaysOnce);
    }
    
    /**
     *  SpriteEx initializer
     * 
     * @param 
     */
    private void init(boolean flipHorizontal, boolean hasAnimation, byte[] spriteFrames, boolean animLoops, boolean animPlaysOnce)
    {
        m_flipHorizontal = flipHorizontal;
        m_hasAnimation = hasAnimation;
        m_spriteFrames = spriteFrames == null ? new byte[] {1,1} : spriteFrames;
        m_animLoops = animLoops;
        m_animPlaysOnce = animPlaysOnce;
    }
    
    /**
     *  Loads the Sprite
     * 
     * @param none
     * @return nothing
     */
    public void load()
    {
        byte frameCount = (byte)(m_spriteFrames[0] * m_spriteFrames[1]);
        int imageCount;
        boolean multiFile;

        if( m_fileNames == null )
        {
            multiFile = Common.fileExists("/" + m_fileName + "0" + ".png");
            imageCount = multiFile?frameCount:1;
        }
        else
        {
            multiFile = false;
            imageCount = m_fileNames.length;
        }

        Image sprite_image[] = new Image[imageCount];

        for( int i = 0; i < imageCount; ++i)
        {
            String file;
            if( m_fileNames == null )
            {
                file = m_fileName;
                if( multiFile ) file += i;
            }
            else
                file = m_fileNames[i];

            sprite_image[i] = Common.loadImage(file);
            if( m_flipHorizontal )
                sprite_image[i] = (Image)Common.painter.createFlippedImage((Object)sprite_image[i], 0, 0, sprite_image[i].getWidth(), sprite_image[i].getHeight(), MXMEConstants.FLIP_HORIZONTAL);
        }

        m_sprite = new Sprite(sprite_image, m_spriteFrames);
        if( m_hasAnimation )
            m_sprite.addAnimation(frameCount, DEFAULT_FPS, m_animLoops, m_animPlaysOnce);
    }
    
    /**
     *  Return our current m_sprite
     * 
     * @param none
     * @return Sprite, SpriteEx Sprite.
     */
    public Sprite getSprite()
    {
        return m_sprite;
    }
    
    /**
     *  Unloads m_sprite
     * 
     * @param none
     * @return nothing
     */
    public void unload()
    {
        m_sprite = null;
    }
    
}
