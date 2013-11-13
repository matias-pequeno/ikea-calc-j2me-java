package com.mxme.common;

import javax.microedition.midlet.MIDlet;

public interface IDeviceController {
    
    /**
     * Checks whether vibration is supported or not
     * @return
     */
    boolean isVibrationSupported();
    
    /**
     * Makes the phone vibrate for the specified time
     * @param duration Time
     */
    void vibrate(int duration);
    
    /**
     * Sets a game to control
     * @param game Game to control
     */
    void setGame(MIDlet game);
}
