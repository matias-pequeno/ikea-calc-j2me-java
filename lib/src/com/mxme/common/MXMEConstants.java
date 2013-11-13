package com.mxme.common;

/**
 * Constants for common classes
 * @author MatuX
 */
public class MXMEConstants
{
    /**
     * Animation constants
     */
    public static final byte ANIMATION_FIRST_FRAME_INDEX = 0;
    public static final byte ANIMATION_FRAME_COUNT_INDEX = 1;
    public static final byte ANIMATION_FPS_INDEX = 2;
    public static final byte ANIMATION_FIELD_COUNT = 3;
    
    public static final byte FRAME_SIZE_WIDTH_INDEX = 0;
    public static final byte FRAME_SIZE_HEIGHT_INDEX = 1;
    public static final byte FRAME_SIZE_FIELD_COUNT = 2;
    
    // Custom Soft keys (Matías F. Pequeno)
    public static final int SOFTKEY_RIGHT_CUSTOM = -50;
    public static final int SOFTKEY_LEFT_CUSTOM = -51;
    public static final int SOFTKEY_C = -8;
    
    // Soft keys
    protected static final int LEFT_SOFTKEY_KEY_1 = -21;
    protected static final int LEFT_SOFTKEY_ACTION_1 = 0;
    protected static final int RIGHT_SOFTKEY_KEY_1 = -22;
    protected static final int RIGHT_SOFTKEY_ACTION_1 = 0;
    
    protected static final int LEFT_SOFTKEY_KEY_2 = -6;
    protected static final int LEFT_SOFTKEY_ACTION_2_1 = Integer.MIN_VALUE;
    protected static final int LEFT_SOFTKEY_ACTION_2_2 = 0;
    protected static final int RIGHT_SOFTKEY_KEY_2 = -7;
    protected static final int RIGHT_SOFTKEY_ACTION_2_1 = Integer.MIN_VALUE;
    protected static final int RIGHT_SOFTKEY_ACTION_2_2 = 0;
    
    protected static final int LEFT_SOFTKEY_KEY_3 = -1;
    protected static final int LEFT_SOFTKEY_ACTION_3_1 = 8;
    protected static final int LEFT_SOFTKEY_ACTION_3_2 = 0;
    protected static final int RIGHT_SOFTKEY_KEY_3 = -4;
    protected static final int RIGHT_SOFTKEY_ACTION_3_1 = 8;
    protected static final int RIGHT_SOFTKEY_ACTION_3_2 = 0;
    
    protected static final int LEFT_SOFTKEY_KEY_4 = 57345;
    protected static final int LEFT_SOFTKEY_ACTION_4 = 0;
    protected static final int RIGHT_SOFTKEY_KEY_4 = 57346;
    protected static final int RIGHT_SOFTKEY_ACTION_4 = 0;
    
    protected static final int LEFT_SOFTKEY_KEY_5 = -6;
    protected static final int LEFT_SOFTKEY_ACTION_5 = 8;
    protected static final int RIGHT_SOFTKEY_KEY_5 = -7;
    protected static final int RIGHT_SOFTKEY_ACTION_5 = 8;
    
    protected static final int LEFT_SOFTKEY_KEY_6 = 21;
    protected static final int LEFT_SOFTKEY_ACTION_6 = 0;
    protected static final int RIGHT_SOFTKEY_KEY_6 = 22;
    protected static final int RIGHT_SOFTKEY_ACTION_6 = 0;
    
    protected static final int LEFT_SOFTKEY_KEY_7 = -202;
    protected static final int LEFT_SOFTKEY_ACTION_7 = 9;
    protected static final int RIGHT_SOFTKEY_KEY_7 = -203;
    protected static final int RIGHT_SOFTKEY_ACTION_7 = 10;

    // Flip values
    public static final byte FLIP_NONE = 0;
    public static final byte FLIP_HORIZONTAL = 1;
    
    // State stack constraints and fields
    public static final short STATE_STACK_SIZE = 15;
    
    // Event queue constraints and fields
    public static final short EVENT_QUEUE_SIZE = 5;
    public static final byte EVENT_QUEUE_FIELD_COUNT = 2;
    public static final byte EVENT_QUEUE_KEY_FIELD = 0;
    public static final byte EVENT_QUEUE_TYPE_FIELD = 1;
    
    //event types
    public static final byte EVENT_KEY_PRESSED = 1;
    public static final byte EVENT_KEY_RELEASED = 2;
    
    // Keys
    protected static final int KEY_0 = 0;
    public static final int KEY_1 = 1;
    public static final int KEY_2 = 2;
    public static final int KEY_3 = 3;
    public static final int KEY_4 = 4;
    public static final int KEY_5 = 5;
    public static final int KEY_6 = 6;
    public static final int KEY_7 = 7;
    public static final int KEY_8 = 8;
    public static final int KEY_9 = 9;
    public static final int KEY_POUND = 10;
    public static final int KEY_STAR = 11;
    public static final int KEY_UP = 12;
    public static final int KEY_LEFT = 13;
    public static final int KEY_RIGHT = 14;
    public static final int KEY_DOWN = 15;
    public static final int KEY_FIRE = 16;
    public static final int KEY_COUNT = 17;
    
    // Sound player states
    public static final byte SOUND_PLAYER_STATE_ERROR = -1;
    public static final byte SOUND_PLAYER_STATE_PREFETCHED = 0;
    public static final byte SOUND_PLAYER_STATE_REALIZED = 1;
    public static final byte SOUND_PLAYER_STATE_STARTED = 2;
    
    public static final long GC_SLEEP = 50;
    
    public static final short ZERO_SHORT = 0;
    public static final byte ZERO_BYTE = 0;
    public static final short ONE_SHORT = 1;
    public static final byte ONE_BYTE = 1;
}
