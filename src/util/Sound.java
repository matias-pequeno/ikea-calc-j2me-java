package util;

/**************************
 * SoundAPI class
 * 
 * Sound system.
 *      Supports:
 *                  o JSR-135
 *      @TODO Should support:
 *                  o No sound
 *                  o Nokia S40 Monotone
 *                  o Nokia S40 Mix
 *                  o Samsung MMF
 *                  o Vodafone SPF
 * 
 */
// MatuX Library
import com.mxme.common.ISoundController; // what about MIDP1, MMF, SPF and monotones?

public class Sound
{
    //#if SFX_JSR_135 == "true"
//#     protected static final String SOUND_CONTROLLER_CLASS_NAME = "com.mxme.common.midp20.SoundController";
    //#endif
    
    // Static Sound constant
    private static ISoundController player;
    
    public static final byte SND_MENU = 0;
    public static final String soundName[] = { "menu" };
    public static final byte SOUND_COUNT = (byte)soundName.length;
    public static final byte START_SOUND = SND_MENU;
    public static final byte SND_SINGLE = SND_MENU;
    
    private final static Sound soundBank[] = new Sound[SOUND_COUNT];
    private static long lastStarted = -1;
    private static byte next_sound = -1,  last_sound_loaded = -1;
    
    // Sound porting constants
    public static final boolean SOUND_SINGLE = false;
    public static final boolean NO_FAST_SOUNDS = false;
    public static final boolean LESS_SOUNDS = false;
    public static final boolean NO_INGAME_SOUNDS = false;
    
    // This sound
    private byte player_id = -1;
    
    /** 
     *  Loads a sound
     * 
     * @param byte id, id of the sound to load
     * @return byte id, id of the sound loaded
     */
    public static final byte load(byte id)
    {
        //#if SFX_JSR_135 == "true"
//#         if( !soundEnabled() ) return -1;
//#
//#         InputStream is = soundBank[id].getClass().getResourceAsStream("/" + soundName[id] + ".mid");
//#         soundBank[id].player_id = player.loadMIDI(is);
//#
//#         last_sound_loaded = id;
        //#endif
        return id;
    }
    
    /** 
     *  Plays a sound
     * 
     * @param byte id, id of the sound to play
     * @return boolean, false if needs a delay before playing the next sound (try again)
     */
    private static final boolean playEx(int id)
    {
        //#if SFX_JSR_135 == "true"
//#         if( NO_FAST_SOUNDS && ((System.currentTimeMillis() - lastStarted) < 300) )
//#             return false;
//#
//#         lastStarted = System.currentTimeMillis();
//#
//#         if( id <= START_SOUND )
//#             stopAll();
//#         player.play(soundBank[id].player_id);
        //#endif
        return true;
    }
    
    /** 
     *  Returns whether or not sound is enabled
     * 
     * @return boolean
     */
    public static final boolean soundEnabled()
    {
        return false; //AppConfig.config_data[AppConfig.VALUE_SOUND] != 0;
    }
    
    /** 
     *  Enables sound
     * 
     */
    public static final void enable()
    {
       //AppConfig.config_data[AppConfig.VALUE_SOUND] = 1;
    }
    
    /** 
     *  Disables sound
     * 
     */
    public static final void disable()
    {
        stopAll();
        //AppConfig.config_data[AppConfig.VALUE_SOUND] = 0;
    }
    
    /** 
     *  Returns whether or not a sound is being played
     * 
     * @param byte id, ID of the sound
     * @return boolean
     */
    public static final boolean isPlaying(byte id)
    {
        boolean result = false;
        //#if SFX_JSR_135 == "true"
//#         result = player.getPlayerState(soundBank[id].player_id) == MatuXConstants.SOUND_PLAYER_STATE_STARTED;
        //#endif
        return result;
    }
    
    /** 
     *  Returns whether or not a sound is in memory
     * 
     * @param byte id, ID of the sound
     * @return boolean
     */
    public static final boolean isLoaded(byte id)
    {
        boolean result = false;
        //#if SFX_JSR_135 == "true"
//#         result = player.getPlayerState(soundBank[id].player_id) != MatuXConstants.SOUND_PLAYER_STATE_ERROR;
        //#endif
        return result;
    }
    
    /** 
     *  Initialize sound system
     * 
     * @return boolean, false if error
     */
    public static final boolean init()
    {
        //#if SFX_JSR_135 == "true"
//#         try
//#         {
//#             player = (ISoundController)Class.forName(SOUND_CONTROLLER_CLASS_NAME).newInstance();
//#             player.initializeSound(SOUND_COUNT);
//#             for( int x = 0; x < SOUND_COUNT; ++x )
//#                 if( soundBank[x] == null ) soundBank[x] = new Sound();
//#
//#             return true;
//#         }
//#         catch( Exception e )
//#         {
            //#if DEBUG == "true"
//#                 e.printStackTrace();
//#                 System.out.println("Sound.initialize(): " + e.getMessage());
            //#endif
//#             return false;
//#         }
        //#else
        return true;
        //#endif
    }
    
    /** 
     *  Queues a sound to be played in the next process
     * 
     * @param byte id, ID of the sound
     * @return void
     */
    public static final void play(byte id)
    {
        //#if SFX_JSR_135 == "true"
//#         if( soundEnabled() )
//#         {
//#             if( SOUND_SINGLE && id != SND_SINGLE ) return;
//#
//#             stopAll();
//#             if( !isLoaded(id) ) load(id);
//#
//#             // set this sound to be played next
//#             next_sound = id;
//#         }
        //#endif
    }
    
    /** 
     *  Process sounds on the queue, sets to play them
     * 
     * @param none
     * @return void
     */
    public static final void process()
    {
        //#if SFX_JSR_135 == "true"
//#         if( !soundEnabled() ) return;
//#
//#         if( next_sound >= 0 )
//#             if( playEx(next_sound) )
//#                 next_sound = -1;
        //#endif
    }
    
    /** 
     *  Stops a sound
     * 
     * @param byte id, ID of the sound
     * @return void
     */
    public static final void stop(byte id)
    {
        //#if SFX_JSR_135 == "true"
//#         if( SOUND_SINGLE && id != SND_SINGLE ) return;
//#         if( soundEnabled() ) player.stop(soundBank[id].player_id);
        //#endif
    }
    
    /** 
     *  Stops all sounds
     * 
     * @param none
     * @return void
     */
    public static final void stopAll()
    {
        //#if SFX_JSR_135 == "true"
//#         if( !soundEnabled() ) return;
//#
//#         if( SOUND_SINGLE )
//#             stop(SND_SINGLE);
//#         else
//#             for( byte i = START_SOUND; i < SOUND_COUNT; i++ )
//#                 stop(i);
        //#endif
    }
    
    /** 
     *  Unload a sound
     * 
     * @param byte id, ID of the sound
     * @return void
     */
    public static final void unload(byte id)
    {
        //#if SFX_JSR_135 == "true"
//#         if( SOUND_SINGLE && id != SND_SINGLE ) return;
//#         if( soundEnabled() ) player.close(soundBank[id].player_id);
        //#endif
    }

    /** 
     *  Unloads all sounds
     * 
     * @param none
     * @return void
     */
    public static final void unloadAll()
    {
        //#if SFX_JSR_135 == "true"
//#         if( SOUND_SINGLE )
//#             unload(SND_SINGLE);
//#         else
//#             for( byte i = 0; i < SOUND_COUNT; i++ )
//#                 unload(i);
        //#endif
    }
    
}
