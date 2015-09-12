package com.danielbchapman.layers;

import processing.core.PGraphics;

import com.danielbchapman.brushes.EllipseBrush;
import com.danielbchapman.brushes.ImageBrush;
import com.danielbchapman.brushes.SaveableBrush;
import com.danielbchapman.brushes.VectorBrush;
import com.danielbchapman.physics.toxiclibs.Layer;
import com.danielbchapman.physics.toxiclibs.MotionEngine;
import com.danielbchapman.physics.toxiclibs.Point;

public class BleedingCanvasLayer extends Layer
{
  boolean blank;
  MotionEngine engine;
  int mode = 0;
  
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
    if(mode % 2 == 0)
    {
      ImageBrush brush = new ImageBrush();
      engine.setBrush(brush);
    }
    else
    {
      EllipseBrush brush = new EllipseBrush();
      engine.setBrush(brush);      
    }
    mode++;
        
    blank = false;//clear the canvas
  }
  @Override
  public void renderBrush(SaveableBrush brush, PGraphics g, int currentFrame)
  {
    brush.update();
    brush.draw(g);
  }
}
