/*
 * Wall.java
 *
 * Created on Mar 5, 2009, 12:50:42 PM
 *
 * Copyright (C) 2009 XXX  All Rights Reserved.
 * This software is the proprietary information of XXX
 *
 */

package calculator;

import java.util.Vector;
import java.util.Enumeration;

/**
 * Author: MatuX
 * Date: Mar 5, 2009
 * Current revision: 0
 * Last modified: Mar 5, 2009
 * By: MatuX
 * Reviewers:
 *
 */
public class Wall
{
    private static final int CACHED_WINDOWS = 2;

    private int measure;
    private Vector window = new Vector(CACHED_WINDOWS, CACHED_WINDOWS);
    private Vector door = new Vector(CACHED_WINDOWS, CACHED_WINDOWS);

    /**
     * Default constructor
     * 
     */
    public Wall()
    {
        // hack
        window.addElement(new Integer(0));
        door.addElement(new Integer(0));
    }

    /**
     * Create a Wall with a given measure
     *
     * @param widthCentimeters
     * @param heightCentimeters
     */
    public Wall(int widthCentimeters, int heightCentimeters)
    {
        this();
        setWallMeasure(widthCentimeters, heightCentimeters);
    }

    /**
     * Sets the base measure of the wall
     *
     * @param widthCentimeters
     * @param heightCentimeters
     */
    public void setWallMeasure(int widthCentimeters, int heightCentimeters)
    {
        measure = widthCentimeters | (heightCentimeters << 16);
    }

    /**
     * Adds a Windows
     *
     * @param widthCentimeters
     * @param heightCentimeters
     */
    public void addWindow(int widthCentimeters, int heightCentimeters)
    {
        //window.addElement(new Integer(widthCentimeters|(heightCentimeters << 16)));
        window.setElementAt(new Integer(widthCentimeters|(heightCentimeters << 16)), 0);
    }

    /**
     * Removes a Window
     *
     * @param index
     */
    public void removeWindow(int index)
    {
        window.removeElementAt(index);
    }

    /**
     * Adds a Door
     *
     * @param widthCentimeters
     * @param heightCentimeters
     */
    public void addDoor(int widthCentimeters, int heightCentimeters)
    {
        //door.addElement(new Integer(widthCentimeters|(heightCentimeters << 16)));
        door.setElementAt(new Integer(widthCentimeters|(heightCentimeters << 16)), 0);
    }

    /**
     * Removes a Door
     *
     * @param index
     */
    public void removeDoor(int index)
    {
        door.removeElementAt(index);
    }

    public int getWidth()       { return (measure & 0xFFFF); }
    public int getHeight()      { return (measure >> 16); }
    public int getWindowWidth(int i) { int m = ((Integer)window.elementAt(i)).intValue(); return (m & 0xFFFF); }
    public int getWindowHeight(int i){ int m = ((Integer)window.elementAt(i)).intValue(); return (m >> 16); }
    public int getDoorWidth(int i)   { int m = ((Integer)door.elementAt(i)).intValue(); return (m & 0xFFFF); }
    public int getDoorHeight(int i)  { int m = ((Integer)door.elementAt(i)).intValue(); return (m >> 16); }

    /**
     * Calculates the CM2 of each wall, minus its windows.
     *
     * @return resulting CM2
     */
    public int calculateCM2()
    {
        if( measure == 0 )
            return 0;
        
        // Get wall CM2
        int wallCM2 = (measure & 0xFFFF) * (measure >> 16);
        // Get windows CM2
        int windowCM2 = 0;
        Enumeration w = window.elements();
        while( w.hasMoreElements() )
        {
            int m = ((Integer)w.nextElement()).intValue();
            windowCM2 += (m & 0xFFFF) * (m >> 16);
        }
        // Get doors CM2
        int doorCM2 = 0;
        Enumeration d = door.elements();
        while( d.hasMoreElements() )
        {
            int m = ((Integer)d.nextElement()).intValue();
            doorCM2 += (m & 0xFFFF) * (m >> 16);
        }

        // Calculate and return the result
        return wallCM2 - windowCM2 - doorCM2;
    }

    public String calculateM2AsString()
    {
        int cm2 = calculateCM2();
        if( cm2 > 0 )
        {
            String m2 = String.valueOf(cm2 / 10000);
            int mod = (cm2 % 10000) / 1000;
            if( mod >= 0 )
                m2 += "," + String.valueOf(mod);
            m2 += "m2";

            return m2;
        }
        else
            return "";

    }

}
