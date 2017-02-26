package com.danielbchapman.motion.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * A simple wrapper class for a KeyCode
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KeyCombo
{
  public boolean alt;
  public boolean ctrl;
  public boolean option;
  public boolean shift;
  public char character;
  
  public KeyCombo(char c)
  {
    this.character = c;
  }
}
