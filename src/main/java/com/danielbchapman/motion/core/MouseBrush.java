package com.danielbchapman.motion.core;

import lombok.Data;


public class MouseBrush extends MotionBrush
{
  @Override
  public MotionBrush copy()
  {
    MouseBrush copy = new MouseBrush();
    MotionBrush.copyTo(this, copy);
    return copy;
  }
}
