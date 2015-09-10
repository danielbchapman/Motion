package com.danielbchapman.brushes;

import processing.core.PGraphics;
import toxi.geom.Vec3D;

public abstract class VectorBrush extends SaveableBrush
{
  float splitSize = 2f;
  boolean idle = false;
  /**
   * A method to draw this brush at a specific point. This 
   * will be called multiple times per large vector so 
   * speed would be advised.
   * @param g the graphics context
   * @param p the point to draw at (world based, not change based)  
   * 
   */
  public abstract void applyBrush(PGraphics g, Vec3D p);
  
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
      if(!idle)
        applyBrush(g, vars.position);
      firstPass = true;
      idle = true;
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
        if(vars.position.equals(lastPosition))
        {
          idle = true;
        }
        if(!idle)
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
        lastPosition = this.vars.position;
      }
    }
    
    g.popMatrix();
    
    //Update position
  }

  @Override
  public String getName()
  {
    return "Vector Brush";
  } 

}
