package com.mxme.common.midp20;

import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;

import com.mxme.common.IDeviceController;

public class DeviceController implements IDeviceController{
    
    private Display m_display;
    
    /**
     * @see com.matux.lib.common.IDeviceController.vibrate(long)
     */
    public void vibrate(int duration) {
       m_display.vibrate(duration);
    }


    /**
     * @see com.matux.lib.common.IDeviceController.setGame(MIDlet)
     */
    public void setGame(MIDlet game) {
        m_display = Display.getDisplay(game);
    }


    /**
     * @see com.matux.lib.common.IDeviceController.isVibrationSupported()
     */
    public boolean isVibrationSupported() {
        return m_display.vibrate(0);
    }
}
