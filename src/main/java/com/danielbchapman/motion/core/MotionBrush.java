package com.danielbchapman.motion.core;

import lombok.Data;

@Data
public abstract class MotionBrush
{
  public boolean mouseDown = false;
  public int mouseX, mouseY, pmouseX, pmouseY;
 
  public static void copyTo(MotionBrush a, MotionBrush b)
  {
    b.mouseDown = a.mouseDown;
    b.mouseX = a.mouseX;
    b.pmouseX = a.mouseX;
    b.mouseY = a.mouseY;
    b.pmouseY = a.pmouseY;
  }
 
  public boolean isActive()
  {
    return mouseDown;
  }
  
  public abstract MotionBrush copy();
}
