package com.danielbchapman.motion.core;

import lombok.Data;


public class MouseBrush extends MotionBrush
{
  /* (non-Javadoc)
   * @see com.danielbchapman.motion.core.MotionBrush#setMouseDown(boolean)
   */
  @Override
  public void setMouseDown(boolean val)
  { 
    //Link active to "down"
    this.mouseDown = val;
  }
  
  @Override
  public MotionBrush copy()
  {
    MouseBrush copy = new MouseBrush();
    MotionBrush.copyTo(this, copy);
    return copy;
  }
}
