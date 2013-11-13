/*
 * PaintCalculator.java
 *
 * Created on Mar 5, 2009, 12:48:39 PM
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
public class PaintCalculator
{
    private static final int CACHED_WALLS = 4;

    public int currentWall = 0;

    public Vector walls = new Vector(CACHED_WALLS, CACHED_WALLS);

    /*public PaintCalculator()
    {

    }*/

    public Vector resetWalls(int amount)
    {
        Calculator.paintCalculator.walls.removeAllElements();
        for( int i = 0; i < amount; i++ )
            walls.addElement(new Wall());
        return walls;
    }

    public int calculateCM2()
    {
        int cm2 = 0;
        Enumeration e = walls.elements();
        while( e.hasMoreElements() )
            cm2 += ((Wall)e.nextElement()).calculateCM2();
        return cm2;
    }

}

