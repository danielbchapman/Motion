package com.danielbchapman.motion.core;

import processing.core.PGraphics;
import toxi.geom.Vec3D;


/**
 * A simple pass-through brush that sends the mouse
 * events to the scene without applying any other 
 * changes.
 */
public class MouseBrush extends MotionBrush
{

  @Override
  public void update(long time)
  { 
  }

  @Override
  public void applyBrush(PGraphics g, MotionMouseEvent point)
  {
  }

  @Override
  public MotionBrush deepCopy()
  {
    return new MouseBrush();
  } 
}
