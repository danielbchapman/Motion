package com.danielbchapman.motion.core;

import lombok.Data;


/**
 * A simple wrapper class for a KeyCode
 */
@Data
public class KeyCombo
{
  public boolean alt;
  public boolean ctrl;
  public boolean option;
  public boolean shift;
  public int code;
}
