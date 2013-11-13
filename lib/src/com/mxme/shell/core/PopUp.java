/*
 * PopUp.java
 *
 * Created on Dec 29, 2008, 8:12:29 PM
 *
 * Copyright (C) 2008 XXX  All Rights Reserved.
 * This software is the proprietary information of Oceanic Images Studios.
 *
 */

package com.mxme.shell.core;

// Java library
import javax.microedition.lcdui.Graphics;
// MatuX library
import com.mxme.shell.Element;
import com.mxme.shell.Shell;

/**
 * Author: MatuX
 * Date: Dec 29, 2008
 * Current revision: 0
 * Last modified: Dec 29, 2008
 * By: MatuX
 * Reviewers:
 *
 */
public class PopUp extends Element
{
    protected int m_title, m_text;
    public boolean isEnabled;

    /**
     * Constructor
     * 
     */
    public PopUp(int title, int text)
    {
        m_title = title;
        m_text = text;
    }

    /****
     *  Initialize this element
     *
     * @param none
     */
    public void initialize()
    {
        isEnabled = false;
    }

    /****
     *  DeInitialize this element
     *
     * @param none
     */
    public void deinitialize()
    {

    }

    /****
     *  Enable this element
     *
     * @param none
     */
    public void enable()
    {
        isEnabled = true;
        setSoftButtons(Shell.m_nHidden, Shell.m_nHidden);
    }

    /****
     *  Disable this element
     *
     * @param none
     */
    public void disable()
    {
        isEnabled = false;
    }

    /****
     *   Process the element
     *
     * @param int p_nAction, data to be sent to the processing function
     * @return boolean, was the key handled?
     */
    public boolean keyPressed(int p_nAction, int keyCode)
    {
        // we always have priority
        return true;
    }

    public boolean keyReleased(int p_nAction, int keyCode)
    {
        // we always have priority
        return true;
    }

    /****
     * Paint the element.<br>
     *  <br>
     * All drawing assumes we have a big enough Clip to draw what we need.
     * This method also draws a standard layout that can be overriden in the childs.
     *
     * @param g, Graphics object
     */
    public void paint(Graphics g)
    {
    
    }

}
