package com.danielbchapman.brushes;

import processing.core.PConstants;
import processing.core.PGraphics;
import toxi.geom.Vec3D;

import com.danielbchapman.physics.toxiclibs.MotionInteractiveBehavior;

public class EllipseBrush extends VectorBrush
{
  
  @Override
  public String getName()
  {
    return "Ellipse Brush";
  }

  @Override
  public void applyBrush(PGraphics g, Vec3D p)
  {
    g.pushMatrix();
    g.translate(p.x, p.y, p.z);
    g.ellipseMode(PConstants.CENTER);
    g.ellipse(0, 0, 10, 10);
    g.popMatrix();
  }
  
  @Override
  public MotionInteractiveBehavior copy()
  {
    EllipseBrush x = new EllipseBrush();
    x.vars = this.vars.clone();
    return x;
  }
}
