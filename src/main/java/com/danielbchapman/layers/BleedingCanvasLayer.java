package com.danielbchapman.layers;

import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;

import com.danielbchapman.brushes.EllipseBrush;
import com.danielbchapman.brushes.ImageBrush;
import com.danielbchapman.brushes.SaveableBrush;
import com.danielbchapman.physics.toxiclibs.Layer;
import com.danielbchapman.physics.toxiclibs.MotionEngine;
import com.danielbchapman.physics.toxiclibs.Point;

public class BleedingCanvasLayer extends Layer
{
  boolean blank;
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
      try{
        PImage image = engine.loadImage("show/OneLeaf.PNG");
        image.filter(PConstants.INVERT);
        g.image(image, 100, 50, 600, 600);
      }
      catch(Throwable t)
      {
        t.printStackTrace();
      }
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
