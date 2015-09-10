package com.danielbchapman.brushes;

import processing.core.PConstants;
import processing.core.PGraphics;

import com.danielbchapman.physics.toxiclibs.MotionInteractiveBehavior;

public class EllipseBrush extends SaveableBrush
{
  
  @Override
  public String getName()
  {
    return "Ellipse Brush";
  }

  @Override
  public void draw(PGraphics g)
  {
    if(vars.position == null)
      return;
    g.pushMatrix();
    g.stroke(255);
    g.fill(255);
    g.translate(vars.position.x, vars.position.y);
    g.ellipseMode(PConstants.CENTER);
    g.ellipse(0, 0, 10, 10);
    g.popMatrix();
    
    //Update position
    lastPosition = this.vars.position;
  }

  @Override
  public MotionInteractiveBehavior copy()
  {
    //TODO Auto Generated Sub
    throw new RuntimeException("Not Implemented...");
    
  }
}
