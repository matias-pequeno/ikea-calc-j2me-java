/*
 * Boolean.java
 *
 * Created on Jan 19, 2009, 8:58:32 PM
 *
 * Copyright (C) 2008 Oceanic Images Studios, Inc.  All Rights Reserved.
 * This software is the proprietary information of Oceanic Images Studios.
 *
 */

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mxme.common;

/**
 * Author: Administrador
 * Date: Jan 19, 2009
 * Current revision: 0
 * Last modified: Jan 19, 2009
 * By: Administrador
 * Reviewers:
 *
 */

public class Boolean
{
  public static final Boolean TRUE = new Boolean(true);
  public static final Boolean FALSE = new Boolean(false);

  public Boolean(boolean b) { }

  public static Boolean valueOf(boolean b)
  {
      return (b ? TRUE : FALSE);
  }

  public static Boolean valueOf(String s)
  {
    return toBoolean(s) ? TRUE : FALSE;
  }

  private static boolean toBoolean(String name)
  {
    return name != null && name.toLowerCase().equals("true");
  }

}

