package com.danielbchapman.brushes;

import processing.core.PConstants;
import processing.core.PGraphics;
import toxi.geom.Vec2D;
import toxi.geom.Vec3D;

import com.danielbchapman.physics.toxiclibs.MotionInteractiveBehavior;

public class VectorBrush extends SaveableBrush
{
  float splitSize = 2f;
  
  public void applyBrush(PGraphics g, Vec3D p)
  {
    g.pushMatrix();
    g.translate(p.x, p.y, p.z);
    g.ellipseMode(PConstants.CENTER);
    g.ellipse(0, 0, 10, 10);
    g.popMatrix();
  }
  
  @Override
  public void draw(PGraphics g)
  {
    if(vars.position == null)
      return;
    g.pushMatrix();
    g.stroke(255);
    g.fill(255);
    
    if(lastPosition == null || !firstPass) //start the brush
    {
      this.lastPosition = vars.position;
      applyBrush(g, vars.position);
      firstPass = true;
    }
    else
    {
      Vec3D scalar = new Vec3D(vars.position.x, vars.position.y, vars.position.z);
      scalar.subSelf(lastPosition);
      float length = scalar.magnitude();
      int steps = (int) (length / splitSize);
      //Paint a single point
      if(steps <= 1)
      {
        applyBrush(g, vars.position);
      }
      else //Paint multiple points
      {
        //first point already exists, skip it (last)
        for(int i = 1; i < steps; i++)
        {
          float subset = i * splitSize;
          Vec3D newSub = scalar.getNormalizedTo(subset);
          Vec3D newPos = lastPosition.add(newSub);
          applyBrush(g, newPos);
        }
        //Draw the new point
        applyBrush(g, vars.position);
      }
    }
    
    g.popMatrix();
    
    //Update position
    lastPosition = this.vars.position;
  }

  @Override
  public MotionInteractiveBehavior copy()
  {
    VectorBrush x = new VectorBrush();
    x.vars = this.vars.clone();
    return x;
  }

  @Override
  public String getName()
  {
    return "Vector Brush";
  } 

}
