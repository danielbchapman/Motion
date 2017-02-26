package com.danielbchapman.motion.core;

import processing.core.PGraphics;
import toxi.geom.Vec3D;


/**
 * A simple pass-through brush that sends the mouse
 * events to the scene without applying any other 
 * changes.
 */
public class VectorMouseBrush extends MotionBrush
{
  @Override
  public MotionBrush copy()
  {
    VectorMouseBrush copy = new VectorMouseBrush();
    MotionBrush.copyTo(this, copy);
    return copy;
  }


  @Override
  public void update(long time)
  { 
  }

  @Override
  public void applyBrush(PGraphics g, MotionMouseEvent point)
  {
  }
  
  @Override
  public boolean isVectorBrush()
  {
    return true;
  }
  
  @Override
  public boolean applyWhenIdle()
  {
    return true;
  }
}
