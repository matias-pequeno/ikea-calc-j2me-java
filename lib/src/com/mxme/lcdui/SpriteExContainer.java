/*
 * SpriteExContainer.java
 *
 * Created on Mar 3, 2009, 9:17:25 PM
 *
 * Copyright (C) 2008 Oceanic Images Studios, Inc.  All Rights Reserved.
 * This software is the proprietary information of Oceanic Images Studios.
 *
 */

package com.mxme.lcdui;

/**
 * Author: Administrador
 * Date: Mar 3, 2009
 * Current revision: 0
 * Last modified: Mar 3, 2009
 * By: Administrador
 * Reviewers:
 *
 * eXtended Sprite
 *
 */
public class SpriteExContainer extends ImageContainer
{
    public SpriteEx sprite = null;

    public SpriteExContainer(String fileName, boolean flipHorizontal, boolean hasAnimation, byte[] spriteFrames, boolean animLoops, boolean animPlaysOnce) {
        super(fileName);
    }
}
