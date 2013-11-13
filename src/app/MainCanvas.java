package app;

/**************************
 * MainCanvas class
 * 
 * Canvas entry point.
 * 
 *
 *
 */

//#if NOKIA_UIAPI == "true"
//# import com.nokia.mid.ui.FullCanvas;
//# import com.nokia.mid.ui.DeviceControl;
//#endif

// J2ME Imports
import com.mxme.common.Common;
import com.mxme.shell.Shell;
import com.mxme.lcdui.IPainter;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Command;

// MatuX Library
import com.mxme.common.*;
import com.mxme.shell.*;

// Internal imports
import shell.*;
import util.*;
import net.*;


//#if MIDP2 == "true"
import javax.microedition.lcdui.game.*;
//#endif

//#if NOKIA_UIAPI == "true"
//# public class MainCanvas extends com.nokia.mid.ui.FullCanvas implements Runnable
//#else
public class MainCanvas extends Canvas implements Runnable, CommandListener
//#endif
{
    //#if MIDP2 == "true"
    private static final String DEVICE_CONTROLLER_CLASS_NAME = "com.mxme.common.midp20.DeviceController";
    //#endif

    // System variables
    public static Display display;
    public IDeviceController device_control;
    public static MainMIDlet m_midlet;
    Thread thread = null;
    public static String midlet_version;

    // Miscellaneous variables
    private long delay;
    private long last_logic_call;
    public static int delta_time = 1;
    private Command[] commands;
    public static boolean draw_please_wait;
    public boolean paused;
    public static boolean finished;
    private boolean paint_it;
    
    // Company logo intro
    public static final int DELAY_DEFAULT = 5000;
    
    // Title intro
    private long false_load_bar_time = 0;
    private int time_since_last_call = 0;
    private byte loading_step = 0;
    
    // Shell
    public static AppShell m_appShell;

    // Networking
    public static Connection connection;
    
    // States
    public int current_state;
    public final static int 
            //STATE_INGAME = 1,
            STATE_MENU = 2,  
            STATE_COMPANYLOGO = 4,  
            STATE_LEVEL_LOCKED = 8, 
            STATE_TITLE_LOADING = 16;

    // Cheat constants
    private static char CHEAT_CODE[] = { KEY_NUM6, KEY_NUM2, KEY_NUM8, KEY_NUM8, KEY_NUM9 }; // M A T U X
    public static boolean cheats_enabled = false;
    private int key_cheat;

    // Miscellaneous Constants
    public static int lang_default = -1;

    // Screen constants. USE view_width/height for ingame
    public static int screen_width,  screen_height,  screen_width_half,  screen_height_half;
    // Viewport values for the game
    public static int view_height,  view_height_half,  view_width,  view_width_half;
    
    // show menus
    public static final byte SHOWMENU_MAIN = 1, SHOWMENU_GAMETYPE = 2, SHOWMENU_TIPS = 3;

    /***
     *  MainCanvas constructor
     * 
     * @param p_midletParent, Midlet to which this Canvas belongs to.
     */
    public MainCanvas(MainMIDlet p_midletParent)
    {
        // Set full screen mode
        //#if MIDP2 == "true"
        setFullScreenMode(true);
        //#else
        //#if MIDP1_FULLSCREEN == "true"
        //# setFullScreenMode(true);
        //#endif
        //#endif
        
        //Game.mainCanvas = this;
        
        // Store our m_midlet parent
        m_midlet = p_midletParent;
        
        // Reset some static variables
        finished = false;
        cheats_enabled = false;
        lang_default = -1;
        draw_please_wait = false;
        delta_time = 1;
        
        // store m_midlet version and default language to use if multiple langs are available
        try
        {
            if( m_midlet.getAppProperty("MIDlet-Version") != null )
                midlet_version = m_midlet.getAppProperty("MIDlet-Version");

            if( m_midlet.getAppProperty("default-lang") != null )
                lang_default = Integer.parseInt(m_midlet.getAppProperty("default-lang"));

            if( lang_default < 0 ) lang_default = 0;
        }
        catch( Exception e )
        {
            //#if DEBUG == "true"
                e.printStackTrace();
                System.out.println(e.getMessage());
            //#endif
        }
        
        // Set and store current screen size
        sizeChanged(getWidth(), getHeight());
        
        //
        // Initialize Utilities
        //
        // Init randomizer
        Common.initRand(System.currentTimeMillis());
        // Device Control
        try { device_control = (IDeviceController)Class.forName(DEVICE_CONTROLLER_CLASS_NAME).newInstance(); } catch( Exception e ) { }
        // Fonts
        Font.load();
        // Sound
        Sound.init();
        // Paint Control
        try { Common.painter = (IPainter)Class.forName(Common.PAINTER_CLASS_NAME).newInstance(); } catch( Exception e ) { }
        // App Configuration
        AppConfig.load_config();
        // Text
        Text.init(0); //AppConfig.config_data[AppConfig.VALUE_LANGUAGE]);
        // Main Menu
        m_appShell = new AppShell(this);
        // Networking
        connection = new Connection();
        
        //
        // Start the Canvas
        //
        // go directly to the title + loading logo
        //setState(STATE_COMPANYLOGO);
        setState(STATE_TITLE_LOADING);
    }
    
    /*******
     * 
     * @param
     */
    public static final void quit()
    {
        //#if FORCE_PLEASE_WAIT == "true"
//#        force_paint_loading();
        //#endif

        Sound.unloadAll();
        AppConfig.save_config(false);
        finished = true;
        Display.getDisplay(m_midlet).setCurrent(null);
        m_midlet.destroyApp(true);

    }

    /*****
     *  Callback function, occurs when size of screen has changed
     * 
     * @param w, screen width
     * @param h, screen height
     */
    protected final void sizeChanged(int w, int h)
    {
//#if ScreenHeight == "208"
//#         h = 208;
//#endif

        view_width = screen_height = h;
        view_height = screen_width = w;
        view_width_half = screen_width_half = screen_width >> 1;
        view_height_half = screen_height_half = screen_height >> 1;
        
        Shell.setScreenSize(w, h);
    }

    public static void forceRepaint()
    {
        m_midlet.canvas.repaint();
        m_midlet.canvas.serviceRepaints();
    }

    protected final void showNotify()
    {
        //#if PAUSE_BLACK_SCREEN == "true"
//#             if( !paused )
//#             {
//#                 if( thread != null )
                    //#if NO_SERIAL_CALL == "true"
//#                         repaint();
                    //#else
//#                         display.callSerially(this);
                    //#endif
//#             }
        //#else
            paused = false;
        //#endif
            
        if( thread == null )
            (thread = new Thread(this)).start();
        last_logic_call = System.currentTimeMillis();
        
        if( current_state == STATE_MENU )
        {
            //#if SFX_RELOAD_ON_RESUME == "true"
//#                 Sound.last_sound_loaded = -1;
//#                 Sound.load(Sound.SND_MENU);
            //#endif
            
            Sound.play(Sound.SND_MENU);
        }
        
        paint_it = false;
        
        //#if DEV_SHARP == "true" || PAUSE_BLACK_SCREEN == "true"
//#             System.gc();
        //#endif
    }

    protected final void hideNotify()
    {
        if( !paused )
        {
            // pause the application and stop all sounds
            paused = true;

            Sound.stopAll();
            hideNotify();
            //#if SFX_RELOAD_ON_RESUME == "true"
//#             last_sound_loaded = -1;
            //#endif
        
            repaint();
            //#if DEV_SIEMENS == "false"
                serviceRepaints();
            //#endif
        }
        
        // reset key buffer
        //Game.key_buffer = 0;
    }

    public final void run()
    {
        //#if NO_SERIAL_CALL == "true"
            while( !finished )
            {
                delta_time = (int) (System.currentTimeMillis() - last_logic_call);
                last_logic_call = System.currentTimeMillis();
                
                if( !paint_it )
                {
//                    if( !paused )
//                    {
//                        if( current_state == STATE_INGAME && Game.getInstance() != null )
//                        {
//                            // if too slow
//                            if( delta_time > 250 ) delta_time = 250;
//                            Game.getInstance().process(delta_time);
//                        }
//                    }

                    Sound.process();

                    // 1 paint == 1 process
                    paint_it = true;

                    repaint();
                    //#if DEV_SIEMENS == "true"
//#                     boolean siemens = true;
                    //#else
                    boolean siemens = false;
                    //#endif
                    if( !siemens || !paused )
                        serviceRepaints();
                }
                Thread.yield();

                //#if DEV_SIEMENS == "true" || DEV_SAMSUNG == "true" || DEV_SHARP == "true"
//#                     paint_it = false;
                //#endif
                
                // if too fast (lock to 20 fps -> 1000/ 50 = 20)
                try
                {
                    Thread.sleep(delta_time < 45 ? 50 - delta_time : 5);
                }
                catch( InterruptedException e )
                {
                    //#if DEBUG == "true"
                        e.printStackTrace();
                        System.out.println(e.getMessage());
                    //#endif
                }
            }
        //#else
//#             if( !finished && !paused )
//#             {
//#                 delta_time = (int) (System.currentTimeMillis() - last_logic_call);
//#                 last_logic_call = System.currentTimeMillis();
//# 
//#                 // if too slow
//#                 if( delta_time > 256 )
//#                     delta_time = 256;
//# 
//#                 // if too fast (lock to 20 fps -> 1000/ 50 = 20)
//#                 try
//#                 {
//#                     if( delta_time < 45 )
//#                         Thread.sleep(50 - delta_time);
//#                     else
//#                         Thread.sleep(5);
//#                 }
//#                 catch( InterruptedException e )
//#                 {
                    //#if DEBUG == "true"
//#                         //TODO what about this line? Err(new Exception("run serially -> " + e.getMessage()));
                    //#endif
//#                 }
//# 
//#                 if( current_state == STATE_INGAME )
//#                     Game.getInstance().process(delta_time);
//# 
//#                 Sound.process();
//#                 repaint();
//#                 // if (Utils.NOKIA) serviceRepaints(); // it works best like this
//#                 Thread.yield();
//#                 paint_it = true;
//#             }
//#             else if( paused )
//#             {
//#                 Thread.yield();
//#                 repaint();
//#                 paint_it = true;
//#             }
        //#endif
    }

    public final void paint(Graphics g)
    {
        //#if NO_SERIAL_CALL == "false"
//#             display.callSerially(this);
        //#endif

        if( paused )
        {
            if( true ) //Utils.PAUSE_BLACK_SCREEN )
            {
                int prevFont = Font.font_id;
                Font.set(Font.VERDANA_MEDIUM_BOLD_GRAY);
                Common.clearScreen(g, this, 0);
                Font.drawScroll(g, Text.get(Text.str_PRESS_KEY_TO_CONTINUE),
                        view_width_half, view_height_half,
                        view_width, view_height_half, view_height_half,
                        Font.FONT_ALIGN_CENTER | Font.FONT_USE_CLIP | Font.FONT_ALIGN_VCENTER);
                Font.set(prevFont);
            }
            return;
        }
        
        try
        {
            if( paint_it )
            {
//                if( current_state == STATE_INGAME )
//                {
//                    try {
//                        Game.getInstance().paint(g);
//                    } catch( Exception e1 ) {
//                        //#if DEBUG == "true"
//                            e1.printStackTrace();
//                            System.out.println(e1.getMessage());
//                        //#endif
//                    }
//                }
//                else
                if( current_state == STATE_MENU )
                {
                    // Forward the paint call to the ShellManager
                    m_appShell.paint(g);
                }
                 else if( current_state == STATE_COMPANYLOGO )
                {
                    Image logo = Gfx.loadAndGetImage(Gfx.imgLogo);
                    g.drawImage(logo, screen_width_half - (logo.getWidth() >> 1), screen_height_half - (logo.getHeight() >> 1), 0);

                    if( System.currentTimeMillis() - delay > DELAY_DEFAULT ) //company logo ends
                    {
                        Gfx.unloadImage(Gfx.imgLogo);
                        setState(STATE_TITLE_LOADING);
                    }
                }
                else if( current_state == STATE_TITLE_LOADING )
                {
                    //#if LOW_END == "true"
//#                 setState(STATE_MENU);
                    //#else
                    // Draw logo
                    //g.setColor(0xFFFFFFFF);
                    //g.fillRect(0, 0, screen_width, screen_height);
                    Image logo = Gfx.loadAndGetImage(Gfx.imgLogo);
                    g.drawImage(logo, 0, 0, 0);
                            //screen_width_half - (logo.getWidth() >> 1),
                            //screen_height_half - (logo.getHeight() >> 1), 0);

                    // length of str_LOADING
                    int len = Gfx.imgLogo.image.getWidth() >> 1;

                    // draw simple loading bar
                    int midx = MainCanvas.screen_width_half - (len >> 1);
                    int y = Shell.m_nMarginY << 2;
                    int h = screen_height / 50;
                    // compute time
                    time_since_last_call += delta_time;
                    long time_elapsed = System.currentTimeMillis() - false_load_bar_time;
                    // compute bar
                    int bar_len = ((int)time_elapsed * len) / DELAY_DEFAULT;
                    if( bar_len > len ) bar_len = len;
                    
                    // draw bars
                    g.setColor(0xaf0000);
                    g.fillRect(midx, y, bar_len, h);
                    g.setColor(0x4f0000);
                    g.drawRect(midx, y, len, h);

                    /*// draw double loading bar
                    int midx = MainCanvas.screen_width - (len >> 1) - margin_x;
                    time_since_last_call += delta_time;
                    long time_elapsed = System.currentTimeMillis() - false_load_bar_time;
                    int bar_len = ((int)time_elapsed * (len >> 1)) / DELAY_DEFAULT;
                    int y = MainCanvas.screen_height - (MainCanvas.screen_height_half/6);
                    int h = screen_height/50;
                    g.setClip(0, 0, screen_width, screen_height);
                    // draw bars
                    g.setColor(0xFF0000);
                    g.fillRect(midx-bar_len, y, bar_len, h);
                    g.fillRect(midx, y, bar_len, h);
                    g.setColor(0x7F0000);
                    g.drawRect(midx, y, bar_len, h);
                    g.drawRect(midx-bar_len, y, bar_len, h);
                    // fix small line
                    g.setColor(0xFF0000);
                    g.drawLine(midx, y+1, midx, y + h - 1);
                    */

                    // do some stepped loading
                    if( loading_step == 1 )
                    {
                        //Gfx.loadPackage(Gfx.pkgLogin);
                        Gfx.loadPackage(Gfx.pkgStd);
                        loading_step++;
                    }
                    if( loading_step == 3 )
                    {
                        //ProfileManager.loadProfiles();
                        loading_step++;
                    }
                    if( loading_step == 5 )
                    {
                        m_appShell.load();
                        loading_step++;
                    }
                    if( loading_step == 7 )
                    {
                        Gfx.unloadImage(Gfx.imgLogo);
                        setState(STATE_MENU);
                    }
                    if( time_since_last_call > DELAY_DEFAULT / 4 )
                    {
                        time_since_last_call = 0;
                        loading_step++;
                    }
                    //#endif
                }
                
                // 1 paint == 1 process
                paint_it = false;
            }

            //#if FORCE_PLEASE_WAIT == "true"
//#             if( draw_please_wait )
//#            {
//#                draw_please_wait = false;
//#                Common.clear_screen(g, 0x000000);
//#                Font.set(Font.FONT_SMALL_WHITE);
//#                Font.draw(g, Text.get(Text.str_LOADING), view_width_half, view_height_half, Font.FONT_ALIGN_CENTER);
//#            }
            //#endif

            //#if DEV_SIEMENS == "true"
//#                 g.setClip(0, 0, screen_width, screen_height);
//#                 g.setColor(0);
//#                 g.drawRect(0, 0, screen_width - 1, screen_height - 1);
            //#endif
                
            //#if DEBUG == "true"
                //*
                Font.set(Font.VERDANA_MEDIUM_BOLD_GRAY);
                g.setClip(0, 0, screen_width, screen_height);
                g.setColor(0xFFFFFF);

                // FPS
                if( delta_time == 0 ) delta_time = 1;
                String s = "fps: " + (1000 / delta_time);
                g.fillRect(3, 50, Font.len(s), Font.getHeight());
                Font.draw(g, s, 3, 50, 0);

                // MEM
                System.gc();
                s = "Mem: " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
                g.fillRect(3, 3, Font.len(s), Font.getHeight());
                Font.draw(g, s, 3, 3, 0);
                //*/
            //#endif
        }
        catch( Exception e )
        {
            //#if DEBUG == "true"
                e.printStackTrace();
                System.out.println("Paint. current_state = " + current_state + " <<" + e.getMessage());
            //#endif
        }
    }

//#if FORCE_PLEASE_WAIT == "true"
//#    public final void force_paint_loading()
//#    {
//#             if( !draw_please_wait )
//#             {
//#                 draw_please_wait = true;
//#                 repaint();
                //#if DEV_SHARP == "false" && DEV_SIEMENS == "false"
//#                     serviceRepaints();
                //#endif
//#             }
//#    }
//#endif
    
    public final void setState(int state)
    {
        //#if DEBUG == "true"
            System.out.println("MainCanvas.setState( " + state + " )");
        //#endif

        //#if FORCE_PLEASE_WAIT == "true"
//#        if( current_state != STATE_COMPANYLOGO && current_state != STATE_TRANSITION )
//#            force_paint_loading();
        //#endif
        
        switch( state )
        {
//            case STATE_INGAME:
//                key_cheat = 0;
//                m_appShell.unload();
//                Game.destroy();
//                System.gc();
//
//                //#if DEBUG == "true"
//                    long mem_available = Runtime.getRuntime().freeMemory();
//
//                    Game.create();
//
//                    System.gc();
//                    System.out.println("Memory used by Game instance = " + (mem_available - Runtime.getRuntime().freeMemory()) + " bytes");
//                    System.out.println("....Memory ussage: " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
//                //#else
////#                     Game.create();
//                //#endif
//                break;
            case STATE_MENU:
                // Unload Game (doesn't matter if it iscreateloaded)
//                Game.destroy();
                System.gc();
                // Load Menu
                m_appShell.load();
                break;
            case STATE_COMPANYLOGO:
                delay = System.currentTimeMillis();
                draw_please_wait = false;
                break;
            case STATE_TITLE_LOADING:
                loading_step = 0;
                Sound.play(Sound.SND_MENU);
                false_load_bar_time = System.currentTimeMillis();
                draw_please_wait = false;
                break;
        }

        last_logic_call = (int) System.currentTimeMillis();
        current_state = state;
        paint_it = false;
    }
    
    protected synchronized final void keyPressed(int keyCode)
    {
        //#if PAUSE_BLACK_SCREEN == "true"
//#         if( paused )
//#         {
//#             paused = false;
//#             showNotify();
//#             return;
//#         }
        //#else
        if( paused ) 
            return;
        //#endif

        int action;
        try {
            action = getGameAction(keyCode);
        } catch( Exception e ) {
            return;
        }

        //#if DEV_MOTOROLA == "true"
//#             if(      Math.abs(keyCode) == 21 ) action = MXMEConstants.SOFTKEY_LEFT_CUSTOM;
//#             else if( Math.abs(keyCode) == 22 ) action = MXMEConstants.SOFTKEY_RIGHT_CUSTOM;
        //#elif DEV_SHARP == "true" || DEV_ALCATEL == "true" || DEV_PANASONIC == "true"
//#             if(      Math.abs(keyCode) == 21 ) action = MXMEConstants.SOFTKEY_LEFT_CUSTOM;
//#             else if( Math.abs(keyCode) == 22 ) action = MXMEConstants.SOFTKEY_RIGHT_CUSTOM;
        //#elif DEV_SIEMENS == "true"
//#             if(      keyCode == -1 ) action = MXMEConstants.SOFTKEY_LEFT_CUSTOM;
//#             else if( keyCode == -4 ) action = MXMEConstants.SOFTKEY_RIGHT_CUSTOM;
        //#elif DEV_LG == "true"
//#             if(      keyCode == -202 ) action = MXMEConstants.SOFTKEY_LEFT_CUSTOM;
//#             else if( keyCode == -203 ) action = MXMEConstants.SOFTKEY_RIGHT_CUSTOM;
        //#elif DEV_SAGEM == "true"
//#             if(      keyCode == -7 ) action = MXMEConstants.SOFTKEY_LEFT_CUSTOM;
//#             else if( keyCode == -6 ) action = MXMEConstants.SOFTKEY_RIGHT_CUSTOM;
        //#elif DEV_NOKIA_3650 == "true"
//#             switch( keyCode )
//#             {
//#                 // Commands
//#                 case -6: action = MXMEConstants.SOFTKEY_LEFT_CUSTOM; break;
//#                 case -11:
//#                 case -7: action = MXMEConstants.SOFTKEY_RIGHT_CUSTOM; break;
//#                 // Game actions        
//#                 case Canvas.KEY_NUM1: action = Canvas.UP; break;
//#                 case Canvas.KEY_NUM2: action = Canvas.LEFT; break;
//#                 case Canvas.KEY_NUM3: action = Canvas.DOWN; break;
//#                 case Canvas.KEY_NUM9: action = Canvas.KEY_POUND; break;
//#                 case Canvas.KEY_STAR: action = Canvas.RIGHT; break;
//#                 case Canvas.KEY_POUND:action = Canvas.FIRE; break;
//#                 // Ignored buttons:
//#                 case Canvas.KEY_NUM4:
//#                 case Canvas.KEY_NUM5:
//#                 case Canvas.KEY_NUM6:
//#                 case Canvas.KEY_NUM7:
//#                 case Canvas.KEY_NUM8:  
//#                     action = 0; break;
//#             }
        //#else
            switch( keyCode )
            {
                case -6:
                    action = MXMEConstants.SOFTKEY_LEFT_CUSTOM; break;
                case -11:
                case -7:
                    action = MXMEConstants.SOFTKEY_RIGHT_CUSTOM; break;
                case KEY_NUM2: action = Canvas.UP;   break;
                case KEY_NUM4: action = Canvas.LEFT;  break;
                case KEY_NUM6: action = Canvas.RIGHT;  break;
                case KEY_NUM8: action = Canvas.DOWN;  break;
                case KEY_NUM5: action = Canvas.FIRE;  break;
            }
        //#endif

//        if( current_state == STATE_INGAME )
//        {
//            // Process cheat code
//            key_cheat += (keyCode == CHEAT_CODE[key_cheat]?1:-key_cheat);
//            if( key_cheat == CHEAT_CODE.length )
//            {
//                cheats_enabled = !cheats_enabled;
//                key_cheat = 0;
//                Game.getInstance().displayDialog((cheats_enabled ? Text.str_EASTER_EGG : Text.str_EASTER_EGG));
//            }
//
//            // forward the key to our game
//            Game.keyPressed(action);
//        }
//        else
        if( current_state == STATE_MENU )
            m_appShell.keyPressed(action, keyCode);
    }

    protected synchronized final void keyReleased(int keyCode)
    {
        if( paused ) return;
        
        int action;
        try {
            action = getGameAction(keyCode);
        } catch( Exception e ) {
            return;
        }
        
        switch( keyCode )
        {
            /*case -6:
                action = MXMEConstants.SOFTKEY_LEFT_CUSTOM; break;
            case -11:
            case -7:
                action = MXMEConstants.SOFTKEY_RIGHT_CUSTOM; break;*/
            case KEY_NUM2: action = Canvas.UP; break;
            case KEY_NUM4: action = Canvas.LEFT; break;
            case KEY_NUM6: action = Canvas.RIGHT; break;
            case KEY_NUM8: action = Canvas.DOWN; break;
            case KEY_NUM5: action = Canvas.FIRE; break;
        }
            
        // Forward the keyReleased callback
//        if( current_state == STATE_INGAME )
//            Game.keyReleased(action);
//        else
        if( current_state == STATE_MENU )
            m_appShell.keyReleased(action, keyCode);

    }

    //#if COMMAND_BAR == "true"
//#     //--------------------------------------------------------------------------//
//#     //      Functions for System command bars... implemented, but not tested
//#     //--------------------------------------------------------------------------//
//#     public final void set_left_cmd(String label)
//#     {
//#             if( this.commands[0] == null )
//#             {
//#                 this.commands[0] = new Command(" ", Command.SCREEN, 0);
//#                 this.addCommand(commands[0]);
//#             }
//#     }
//# 
//#     public final void set_right_cmd(String label)
//#     {
//#         
//#             if( this.commands[1] == null )
//#             {
//#                 /*if( COMMAND_BAR )
//#                 this.commands[1] = new Command( label, Command.SCREEN, 1);
//#                 else*/
//#                 this.commands[1] = new Command("  ", Command.BACK, 1);
//#                 this.addCommand(commands[1]);
//#             }
//#     }
    //#endif
    
    public final void commandAction(Command c, Displayable d)
    {
        //#if COMMAND_BAR == "true"
//#             if( this.commands[0] != null )
//#                 if( c.getLabel() == this.commands[0].getLabel() )
//#                     keyPressed(-6);
//#                 else
//#                     keyPressed(-7);
//#             else if( this.commands[1] != null )
//#                 keyPressed(-7);
        //#endif
    }
    
}
