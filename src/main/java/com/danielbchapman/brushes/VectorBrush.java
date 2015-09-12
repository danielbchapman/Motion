package com.danielbchapman.brushes;

import com.danielbchapman.physics.toxiclibs.MotionInteractiveBehavior;
import com.danielbchapman.physics.toxiclibs.Util;

import lombok.Getter;
import lombok.Setter;
import processing.core.PGraphics;
import toxi.geom.Vec3D;

public abstract class VectorBrush extends SaveableBrush
{
  float splitSize = 2f;
  boolean idle = false;
  @Getter
  @Setter
  protected int color;
  @Getter
  @Setter
  protected int opacityStart = 255;
  protected int opacityEnd = 16;
  protected int minOpacity = 16;
  protected int maxOpacity = 255;
  
  protected float sizeStart = 1.0f;
  protected float sizeEnd = 0.2f;
  protected float minSize = 0.1f;
  protected float maxSize = 2.0f;
  
  protected long fadeTime = 1000; // 1 second
  protected long sizeTime = 2000;
  public abstract boolean isFadingBrush();
  public abstract boolean isVariableBrush();
  /**
   * A method that can be called before any drawing operations occur but 
   * after the update method is called to set opacity
   */
  public void beforeDraw()
  {
  }
  /**
   * A method to draw this brush at a specific point. This 
   * will be called multiple times per large vector so 
   * speed would be advised.
   * @param g the graphics context
   * @param p the point to draw at (world based, not change based)  
   * @param opacity the opacity to draw this stroke with
   * @param sizeModifier a 0f-1f percentage modifier for the size larger values are
   * permitted. 
   */
  public abstract void applyBrush(PGraphics g, Vec3D p, int opacity, float sizeModifier);

  @Override
  public void draw(PGraphics g)
  {
    tick();
    if(vars.position == null)
      return;
    g.pushMatrix();
    g.stroke(255);
    g.fill(255);
    
    int currentOpacity = opacityStart;
    long elapsed = System.currentTimeMillis() - mouseDown;
    if(isFadingBrush())
    {
      currentOpacity =
          (int) Util.fade(
              opacityStart, 
              opacityEnd, 
              fadeTime, 
              elapsed, 
              minOpacity, 
              maxOpacity);
    }
    
    float sizeModifier = sizeStart;
    if(isVariableBrush())
    {
      sizeModifier = Util.fade(
          sizeStart, 
          sizeEnd, 
          sizeTime,
          elapsed,
          minSize,
          maxSize);
    }
    
    if(lastPosition == null || !firstPass) //start the brush
    {
      this.lastPosition = vars.position;
      if(!idle)
        applyBrush(g, vars.position, currentOpacity, sizeModifier);
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
          applyBrush(g, vars.position, currentOpacity, sizeModifier);
      }
      else //Paint multiple points
      {
        //first point already exists, skip it (last)
        for(int i = 1; i < steps; i++)
        {
          float subset = i * splitSize;
          Vec3D newSub = scalar.getNormalizedTo(subset);
          Vec3D newPos = lastPosition.add(newSub);
          applyBrush(g, newPos, currentOpacity, sizeModifier);
        }
        //Draw the new point
        applyBrush(g, vars.position, currentOpacity, sizeModifier);
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
  

@Override
public MotionInteractiveBehavior copy() {
	// TODO Auto-generated method stub
	return null;
}
}
