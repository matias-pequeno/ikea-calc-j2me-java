package com.mxme.common.midp20;

import java.io.InputStream;

import javax.microedition.media.Manager;
import javax.microedition.media.Player;
//import com.sprintpcs.media
import com.mxme.common.ISoundController;
import com.mxme.common.MXMEConstants;

public class SoundController implements ISoundController {

    private Player m_players[];
    
    /**
     * @see ISoundController.initializeSound(byte)
     */
    public void initializeSound(byte playerCount) {
        destroy();
        m_players = new Player[playerCount];
    }

    /**
     * @see ISoundController.loadMIDI(InputStream)
     */
    public byte loadMIDI(InputStream istream) {
        byte res = -1;
        if (m_players != null) {
            // Look for a suitable position
            res = 0;
            boolean found = false;
            while (res < m_players.length
                    && !found) {
                if (m_players[res] == null) {
                    found = true;
                } else {
                    res++;
                }
            }
            if (!found) {
                res = -1;
            } else {                
                // Load the sound
                try {
                    m_players[res] = 
                        Manager.createPlayer(istream, "audio/midi");
                    m_players[res].realize();
                } catch (Exception e) {
                    m_players[res] = null;
                    res = -1;
                } finally {
//                    try {
//                        istream.close(); 
//                    } catch (Exception e) {
//                    } finally {
                        istream = null;
//                    }
                }           
            }
        }
        return res;
    }

    /**
     * @see ISoundController.prefetch(byte)
     */
    public boolean prefetch(byte playerIndex) {   
        boolean res = false;
        if (m_players != null &&
                playerIndex > -1 && playerIndex < m_players.length
                && m_players[playerIndex] != null
                && m_players[playerIndex].getState() 
                    == Player.REALIZED) {
            try {
                m_players[playerIndex].prefetch();
                res = true;
            } catch (Exception e) {
                res = false;
            }
        }
        return res;
    }
    
    /**
     * @see ISoundController.close(byte)
     */
    public boolean close(byte playerIndex) {   
        boolean res = false;
        if (m_players != null &&
                playerIndex > -1 && playerIndex < m_players.length
                && m_players[playerIndex] != null) {
            try {
                m_players[playerIndex].close();
                res = true;
            } catch (Exception e) {
                res = false;
            } finally {
                m_players[playerIndex] = null;
            }
        }
        return res;
    }
    
    /**
     * @see ISoundController.deallocate(byte)
     */
    public boolean deallocate(byte playerIndex) {   
        boolean res = false;
        if (m_players != null &&
                playerIndex > -1 && playerIndex < m_players.length
                && m_players[playerIndex] != null
                && m_players[playerIndex].getState() 
                    == Player.PREFETCHED) {
            try {
                m_players[playerIndex].deallocate();
                res = true;
            } catch (Exception e) {
                res = false;
            }
        }
        return res;
    }

    /**
     * @see ISoundController.play(byte)
     */
    public boolean play(byte playerIndex) {
        boolean res = false;
        if (m_players != null &&
                playerIndex > -1 && playerIndex < m_players.length
                && m_players[playerIndex] != null
//                && m_players[playerIndex].getState() 
//                    == Player.PREFETCHED
                    ) {
            try {
                m_players[playerIndex].setLoopCount(1);
                m_players[playerIndex].start();
                res = true;
            } catch (Exception e) {
                res = false;
            }
        }
        return res;
    }

    /**
     * @see ISoundController.getPlayerState(byte)
     */
    public byte getPlayerState(byte playerIndex) {
        byte res = MXMEConstants.SOUND_PLAYER_STATE_ERROR;
        if (m_players != null &&
                playerIndex > -1 && playerIndex < m_players.length
                && m_players[playerIndex] != null) {
            switch (m_players[playerIndex].getState()) {
            case Player.REALIZED:
                res = MXMEConstants.SOUND_PLAYER_STATE_REALIZED;
                break;
            case Player.PREFETCHED:
                res = MXMEConstants.SOUND_PLAYER_STATE_PREFETCHED;
                break;
            case Player.STARTED:
                res = MXMEConstants.SOUND_PLAYER_STATE_STARTED;
                break;
            default:
                res = MXMEConstants.SOUND_PLAYER_STATE_ERROR;
                break;
            }
        }
        return res;
    }

    /**
     * @see ISoundController.stop(byte)
     */
    public boolean stop(byte playerIndex) {
        boolean res = false;
        if (m_players != null &&
                playerIndex > -1 && playerIndex < m_players.length
                && m_players[playerIndex] != null
                && m_players[playerIndex].getState() 
                    == Player.STARTED) {
            try {
                m_players[playerIndex].stop();
                res = true;
            } catch (Exception e) {
                res = false;
            }
        }
        return res;
    }
    
    /**
     * @see ISoundController.destroy()
     */
    public void destroy() {
        if (m_players != null) {
            for (int i = 0; i < m_players.length; i++) {
                if (m_players[i] != null) {
                    m_players[i].close();
                    m_players[i] = null;
                }
            }
        }
    }

    public byte getFirstPrefetchedPlayer() {
        byte res = -1;
        if (m_players != null) {
            for (int i = 0; i < m_players.length; i++) {
                if (m_players[i] != null) {
                    Player p = m_players[i];
                    int state = p.getState();
                    if (state != Player.PREFETCHED && state != Player.STARTED) {
                        res = (byte)i;
                        break;
                    }                }
            }
        }
        return res;
    }

    public boolean isSoundSupported() {
        return true;
    }
}
