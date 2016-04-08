package com.danielbchapman.layers;

import com.danielbchapman.physics.toxiclibs.Layer;
import com.danielbchapman.physics.toxiclibs.MotionEngine;
import com.danielbchapman.physics.toxiclibs.Point;

import processing.core.PGraphics;

public class ClearLayer extends Layer
{
  boolean clear = false;
  @Override
  public Point[] init()
  {
    return new Point[]{};
  }

  @Override
  public void render(PGraphics g)
  {
    if(!clear)
    {
      g.clear();
      g.background(0);
      clear = true;
    }
  }

  @Override
  public void update()
  {
  }

  @Override
  public void go(MotionEngine engine)
  {
    System.out.println("Clear Called");
    clear = false;
  }

}
