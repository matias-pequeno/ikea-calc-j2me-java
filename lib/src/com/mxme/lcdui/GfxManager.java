/****
 * GfxManager Class
 *
 * This class contains, loads and unloads all graphics used during
 * the whole application.
 * It also uses "packages", which is a collection of IDs of images and sprites
 * that will be loaded and unloaded as a group.
 *
 */
package com.mxme.lcdui;

// Java ME imports
import javax.microedition.lcdui.Image;

// Lib imports
import com.mxme.common.Common;

public abstract class GfxManager
{
    /**
     * Image list
     *
     */
    public static ImageContainer
            // Example image
            imgExample  = new ImageContainer("example_image");

    /**
     * Full sprite list
     *
     */
    public static SpriteExContainer // 0: m_fileName 1: m_flipHorizontal 2: m_hasAnimation 3: frameCount 4: m_animLoops 5: m_animPlaysOnce
                                    // name     flip   anim   frame          loop   once
            sprExample = new SpriteExContainer("example_sprite",   true,  true,  new byte[] {6,1}, true,  false);
    
    /**
     * Graphic packages
     * 
     */
    //  [0][] = Images, [1][] = Sprites
    public static final byte IMAGES = 0, SPRITES = 1;
    public static ImageContainer[][] pkgExample = { {imgExample}, {sprExample} };
    
    /**
     *  Returns the requested Image, loads it if necessary
     * 
     * @param ID, Image ID
     * @return Image, requested Image
     */
    public static Image loadAndGetImage(ImageContainer img)
    {
        if( img.image == null )
            img.image = Common.loadImage(img.filename);
        return img.image;
    }
    
    public static void unloadImage(ImageContainer img)
    {
        img.image = null;
    }

    /**
     *  Returns the requested Image, loads it if necessary
     * 
     * @param ID, Sprite ID
     * @return Sprite, requested Sprite
     */
    public static Sprite getSprite(SpriteExContainer spr)
    {
        return spr.sprite.getSprite();
    }
    
    /**
     *  Plays the animation that belongs to a Sprite
     * 
     * @param ID, Sprite ID
     * @return void
     */
    public static void playSpriteAnimation(SpriteExContainer spr)
    {
        spr.sprite.getSprite().playAnimation();
    }
    
    public static void loadPackage(ImageContainer[][] pkg)
    {
        //#if DEBUG == "true"
//#         if( pkg[IMAGES].length > 0 || pkg[SPRITES].length > 0 )
//#             System.out.println("Loading package whose first ID is " + (pkg[IMAGES].length>0?pkg[IMAGES][0]:pkg[SPRITES][0]));
        //#endif
        
        // Load Package Images
        for( int i = 0; i < pkg[IMAGES].length; ++i )
            pkg[IMAGES][i].image = Common.loadImage(pkg[IMAGES][i].filename);
        // Load Package Sprites
        for( int i = 0; i < pkg[SPRITES].length; ++i )
            ((SpriteExContainer)pkg[SPRITES][i]).sprite.load();
    }
    
    public static void unloadPackage(ImageContainer[][] pkg)
    {
        //#if DEBUG == "true"
//#         if( pkg[IMAGES].length > 0 || pkg[SPRITES].length > 0 )
//#             System.out.println("Unloading package whose first ID is " + (pkg[IMAGES].length>0?pkg[IMAGES][0]:pkg[SPRITES][0]));
        //#endif
        
        // Unload Package Images
        for( int i = 0; i < pkg[IMAGES].length; ++i )
            pkg[IMAGES][i].image = null;
        // Unload Package Sprites
        for( int i = 0; i < pkg[SPRITES].length; ++i )
            ((SpriteExContainer)pkg[SPRITES][i]).sprite.unload();
    }
    
    /*public static byte getFrameCount(SpriteExContainer spr)
    {
        return (byte)(spr.sprite.m_spriteFrames[0] * spr.sprite.m_spriteFrames[1]);
    }*/

}
