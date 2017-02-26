package com.danielbchapman.motion.core;

import lombok.Data;

@Data
public abstract class MotionBrush
{
  public boolean left = false;
  public boolean center = false;
  public boolean right = false;
  
  public int mouseX; 
  public int mouseY;
  public int mouseZ;
  public int pmouseX;
  public int pmouseY;
  public int pmouseZ;
 
  public static void copyTo(MotionBrush a, MotionBrush b)
  {
    b.mouseX = a.mouseX;
    b.pmouseX = a.mouseX;
    b.mouseY = a.mouseY;
    b.pmouseY = a.pmouseY;
    b.mouseZ = a.mouseZ;
    b.pmouseZ = a.pmouseZ;
    b.left = a.left;
    b.right = a.right;
    b.center = a.center;
  }
 
  public boolean isActive()
  {
    return left || center || right;
  }
  
  public abstract MotionBrush copy();
}
