package com.danielbchapman.physics.toxiclibs;

import com.danielbchapman.brushes.SaveableBrush;

import processing.core.PGraphics;

public class BlackoutLayer extends Layer
{

  @Override
  public Point[] init()
  {
    return new Point[0];
  }

  @Override
  public void render(PGraphics graphics)
  {
    graphics.background(0);
  }

  @Override
  public void update()
  {
  }

  @Override
  public void go(MotionEngine engine)
  { 
  }
  
  @Override
  public String getName()
  {
    return this.getClass().getName();
  }
}
