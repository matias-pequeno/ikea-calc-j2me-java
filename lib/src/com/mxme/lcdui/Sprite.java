package com.mxme.lcdui;

import com.mxme.common.*;
import javax.microedition.lcdui.Image;

public class Sprite
{
    /**
     * Image used by the sprite
     */
    private Image[] m_images;
    
    /**
     * Animation controller used by the sprite
     */
    private Animation m_animation;
    
    /**
     * Frame size
     * [0] = width
     * [1] = height
     */
    private short[] m_frameSize;

    /**
     * Short array used internally
     */
    private static short[] m_auxShortArray = new short[2];
    
    /**
     * Creates a new sprite
     * 
     * @param image Image used by this sprite
     * @param numFrames Number of frames ([0] per row, [1] per col) 
     */
    public Sprite(Image[] images, byte[] numFrames)
    {
        m_images = images;
        m_frameSize = new short[2];
        if( images.length == 1 )
        {
            m_frameSize[0] = (short)(images[0].getWidth() / numFrames[0]);
            m_frameSize[1] = (short)(images[0].getHeight()/ numFrames[1]);
        }
        else
        {
            m_frameSize[0] = (short)images[0].getWidth();
            m_frameSize[1] = (short)images[0].getHeight();
        }
    }
    
    /**
     * Destroys the sprite
     */
    public void destroy()
    {
        m_animation = null;
        m_images = null;
        m_frameSize = null;
    }
    
    /**
     * Adds a new animation
     * 
     * @param numFrames Frame count for this animation
     * @param fps FPS for this animation
     * @param loop true if this animation loops backwards
     * @param once true if this animation plays once and stops
     */
    public void addAnimation(byte numFrames, byte fps, boolean loop, boolean once)
    {
        m_animation = new Animation(numFrames, fps, loop, once);
        //m_animation.status = Animation.ANIM_PLAY;
    }
    
    /**
     *  Play animation
     * 
     */
    public void playAnimation()
    {
        if( m_animation != null )
        {
            m_animation.rewind();
            m_animation.status = Animation.ANIM_PLAY;
        }
    }
    
    /**
     *  Get animation
     * 
     */
    public Animation getAnimation()
    {
        return m_animation;
    }
    
    /**
     *  Get an animation copy
     * 
     */
    public Animation getAnimationCopy()
    {
        return new Animation(m_animation);
    }
    
    /**
     * Gets the frame width for an image
     *
     * @return The frame width
     */
    public short getFrameWidth()
    {
        return m_frameSize[MXMEConstants.FRAME_SIZE_WIDTH_INDEX];
    }
    
    /**
     * Gets the frame height for an image
     * 
     * @return The frame height
     */
    public short getFrameHeight()
    {
        return m_frameSize[MXMEConstants.FRAME_SIZE_HEIGHT_INDEX];
    }
    
    /**
     * Gets the frame count for an animation
     * 
     * @return The frame count for the animation
     */
    public byte getFrameCount()
    {
        if( m_animation != null )
            return m_animation.max_frame;
        else
            return (byte)m_images.length;
    }
    
    /**
     * Returns the starting in-image coordinates for a certain frame 
     * @param imageIndex Image to use
     * @param animationIndex Animation to use
     * @param frame Frame index
     * @param res An array where the coords will be stored
     */
    protected void getStartingCoords(short frame, short[] res) 
    {
        if( m_images.length == 1 )
        {
            short framesPerRow = 
                (short) (m_images[0].getWidth() 
                        / m_frameSize[MXMEConstants.FRAME_SIZE_WIDTH_INDEX]);
            int startingY = frame / framesPerRow;
            int startingX = frame % framesPerRow;

            // Y coord
            res[1] = (short) (startingY 
                        * m_frameSize[MXMEConstants.FRAME_SIZE_HEIGHT_INDEX]);
            // X coord
            res[0] = (short) (startingX 
                    * m_frameSize[MXMEConstants.FRAME_SIZE_WIDTH_INDEX]);
        }
        else
        {
            res[0] = 0;
            res[1] = 0;
        }
    }
    
    /**
     * Draws the sprite with its animation
     * 
     * @param g Graphics object to use
     * @param painter Painter in charge of drawing
     * @param x X coord
     * @param y Y coord
     * @param flip Flip value
     */
    public void drawAnimatedSprite(Object g, IPainter painter, 
            short x, short y, byte flip)
    {
        drawFrame(g, painter, x, y, m_animation.frame, flip);
        m_animation.process();
    }
    
    /**
     * Draws the sprite with the given animation
     * 
     * @param g Graphics object to use
     * @param painter Painter in charge of drawing
     * @param animation Animation used to draw
     * @param x X coord
     * @param y Y coord
     * @param flip Flip value
     */
    public void drawAnimatedSprite(Object g, IPainter painter, Animation animation, short x, short y, byte flip)
    {
        drawFrame(g, painter, x, y, animation.frame, flip);
        animation.process();
    }
    
    /**
     * Draws a single frame from the sprite
     * 
     * @param g Graphics object to use
     * @param painter Painter in charge of drawing
     * @param x X coord
     * @param y Y coord
     * @param frame Frame to draw
     * @param flip Flip value
     */
    public void drawFrame(Object g, IPainter painter, 
            short x, short y, byte frame, byte flip)
    {
        // Get starting coords
        getStartingCoords(frame, m_auxShortArray);
        // Draw the frame
        Image img = m_images.length > 1 ? m_images[frame] : m_images[0];
        painter.drawRegion(g, img, 
                m_auxShortArray[0], m_auxShortArray[1],
                x, y, 
                m_frameSize[MXMEConstants.FRAME_SIZE_WIDTH_INDEX],
                m_frameSize[MXMEConstants.FRAME_SIZE_HEIGHT_INDEX], flip);
    }
    
    /**
     * Draws a single frame from the sprite
     * 
     * @param g Graphics object to use
     * @param painter Painter in charge of drawing
     * @param x X coord
     * @param y Y coord
     * @param flip Flip value
     */
    public void drawStaticFrame(Object g, IPainter painter, 
            short x, short y, byte flip)
    {
        drawFrame(g, painter, x, y, m_animation.frame, flip);
    }
    
    /**
     * Draws a single frame from the animation
     * 
     * @param g Graphics object to use
     * @param painter Painter in charge of drawing
     * @param animation Animation used to draw
     * @param x X coord
     * @param y Y coord
     * @param flip Flip value
     */
    public void drawStaticFrame(Object g, IPainter painter, Animation animation, 
            short x, short y, byte flip)
    {
        drawFrame(g, painter, x, y, animation.frame, flip);
    }
    
}
