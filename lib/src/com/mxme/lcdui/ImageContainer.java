/*
 * ImageContainer.java
 *
 * Created on Mar 3, 2009, 9:16:02 PM
 *
 * Copyright (C) 2009 XXX  All Rights Reserved.
 * This software is the proprietary information of XXX.
 *
 */

package com.mxme.lcdui;

// Java ME imports
import javax.microedition.lcdui.Image;

/**
 * Author: MatuX
 * Date: Mar 3, 2009
 * Current revision: 0
 * Last modified: Mar 3, 2009
 * By: MatuX
 * Reviewers:
 *
 */
public class ImageContainer
{
    /**
     * eXtended Image
     */
    public String filename;
    public Image image = null;

    public ImageContainer(String f) {
        filename = f;
    }

}
