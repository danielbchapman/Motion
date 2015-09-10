package com.danielbchapman.layers;

import processing.core.PGraphics;

import com.danielbchapman.brushes.SaveableBrush;
import com.danielbchapman.brushes.VectorBrush;
import com.danielbchapman.physics.toxiclibs.Layer;
import com.danielbchapman.physics.toxiclibs.MotionEngine;
import com.danielbchapman.physics.toxiclibs.Point;

public class BleedingCanvasLayer extends Layer
{
  boolean blank;
  MotionEngine engine;
  
  public BleedingCanvasLayer(MotionEngine engine)
  {
    this.engine = engine;
  }
  
  @Override
  public Point[] init()
  {
    return new Point[0];
  }

  @Override
  public void render(PGraphics g)
  {
    //If this isn't setup set it up
    if(!blank)
    {
      g.background(0);
      blank = true;
    }
  }

  @Override
  public void update()
  {

  }

  @Override
  public void go(MotionEngine engine)
  {
    System.out.println("GO FIRED");
    VectorBrush brush = new VectorBrush();
    engine.setBrush(brush);  
    blank = false;//clear the canvas
  }
  @Override
  public void renderBrush(SaveableBrush brush, PGraphics g)
  {
    brush.draw(g);
  }
}
