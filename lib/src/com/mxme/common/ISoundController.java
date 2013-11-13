package com.mxme.common;

import java.io.InputStream;

public interface ISoundController {
    /**
     * Initializes the sound system
     * @param playerCount Max number of players to use
     */
    void initializeSound(byte playerCount);
    
    /**
     * Loads a new MIDI to be played next
     * @param istream Input stream from which to read
     * @return The used player index
     */
    byte loadMIDI(InputStream istream);
    
    /**
     * Deallocates a player
     * @param playerIndex Index of the player
     * @return "true" if the MIDI was successfully deallocated
     */
    boolean deallocate(byte playerIndex);
    
    /**
     * Prefetches a player
     * @param playerIndex Index of the player
     * @return "true" if the MIDI was successfully prefetched
     */
    boolean prefetch(byte playerIndex);
    
    /**
     * Closes a player
     * @param playerIndex Index of the player
     * @return "true" if the MIDI was successfully closed
     */
    boolean close(byte playerIndex);
    
    /**
     * Plays the current loaded sound (if any) in the given player
     * @param playerIndex Index of the player
     */
    boolean play(byte playerIndex);
    
    /**
     * Returns the player state
     * @param playerIndex Index of the player
     * @return Player state
     */
    byte getPlayerState(byte playerIndex);
    
    /**
     * Stops the sound that is currently be played (if any)
     * @param playerIndex Index of the player
     */
    boolean stop(byte playerIndex);
    
    /**
     * Destroys the controller
     */
    void destroy();
    
    byte getFirstPrefetchedPlayer();
    
    boolean isSoundSupported();
}
