package com.danielbchapman.brushes;

import processing.core.PConstants;
import processing.core.PGraphics;
import toxi.geom.Vec3D;

import com.danielbchapman.physics.toxiclibs.MotionInteractiveBehavior;

public class EllipseBrush extends VectorBrushPORT
{
  
  @Override
  public String getName()
  {
    return "Ellipse Brush";
  }

  @Override
  public void applyBrush(PGraphics g, Vec3D p, int opacity, float sizeMod)
  {
    g.pushMatrix();
    g.translate(p.x, p.y, p.z);
    g.fill(color);
    g.stroke(color);
    g.ellipseMode(PConstants.CENTER);
    g.ellipse(0, 0, 10, 10);
    g.popMatrix();
  }

  @Override
  public boolean isFadingBrush()
  {
    return false;
  }

  @Override
  public boolean isVariableSizeBrush()
  {
    return false;
  }

  @Override
  public VectorBrushPORT copyBrushVariables() {
	return new EllipseBrush();
  }
}
