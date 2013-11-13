package com.mxme.lcdui;

/**************************
 * Animation class
 * 
 * Mini animation class
 * 
 *  Notes:
 *      @author Matías F. Pequeno
 *     
 */

public class Animation 
{
    public static final byte
            ANIM_STOP = 0,
            ANIM_PAUSE = 1,
            ANIM_PLAY = 2;
    public byte status = ANIM_STOP;
    
    private long timer = 0;
    private long delay = 0;
    public boolean loop = false, once = false;
    public boolean play_backwards = false;
    
    public byte frame = 0;
    public byte max_frame = 0;
    
    public Animation(byte max_frame, byte fps, boolean loop, boolean once)
    {
        frame = 0;
        this.max_frame = max_frame;
        delay = 1000 / fps;
        this.loop = loop;
        this.once = once;
        
        timer = System.currentTimeMillis();
    }
    
    //Copies the animation
    public Animation(Animation other)
    {
        this.frame = 0;
        this.max_frame = other.max_frame;
        this.delay = other.delay;
        this.loop = other.loop;
        this.once = other.once;
        
        timer = System.currentTimeMillis();
    }
    
    public void play()
    {
        status = ANIM_PLAY;
    }
    
    public void stop()
    {
        status = ANIM_STOP;
        rewind();
    }
    
    public void pause()
    {
        status = ANIM_STOP;
    }
    
    public void rewind()
    {
        frame = 0;
        play_backwards = false;
    }
    
    public void process()
    {
        if( status == ANIM_PLAY )
            // selection cursor animation, loopeable
            if( System.currentTimeMillis() - timer > delay )
            {
                if( loop )
                {
                    if( play_backwards ) frame--;
                    else frame++;
                    if( frame == max_frame - 1 ) play_backwards = true;
                    if( frame == 0 )
                    {
                        play_backwards = false;
                        if( once ) stop();
                    }
                }
                else
                {
                    frame++;
                    if( frame == max_frame - 1)
                    {
                        if( once ) pause(); else rewind();
                    }
                }

                timer = System.currentTimeMillis();
            }
    }
    
}
