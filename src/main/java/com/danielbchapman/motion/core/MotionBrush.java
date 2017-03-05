package com.danielbchapman.motion.core;

import lombok.Data;
import processing.core.PGraphics;


/**
 * <p>
 * A class that acts as a paint-brush in the virtual world. This
 * class maps all the data for sub-classes which can then extend
 * and apply their own variables.
 * </p>
 * 
 * <p>
 * The motion brush class models a paint-brush that is loaded with 
 * some sort of paint. There are variables that can be set to affect
 * whether this paint is infinite, scales over distance, changes size,
 * and so on. These variables will be calculated internally and can be 
 * applied to physical forces as well and the calculations are applied 
 * inside the subclasses of the brush.
 * </p>
 */
@Data
public abstract class MotionBrush implements ICloneable<MotionBrush>
{
  //Vector Methods
  private boolean down;
  public int framesDrawn = 0;
  /**
   * The distance to force a redraw on this point 
   */
  public float splitSize = 2f;
  MotionMouseEvent last = null;
  long startTime;
  
  public boolean isVectorBrush()
  {
    return false;
  }
  
  public boolean applyWhenIdle()
  {
    return false;
  }
  
  public abstract void applyBrush(PGraphics g, MotionMouseEvent point);
  
  /**
   * An update method called prior to any drawing methods should
   * it be needed.
   */
  public abstract void update(long time);
 
  public boolean checkActive(MotionMouseEvent m)
  {
    return m.left || m.right || m.center;
  }
  
  public boolean isDown()
  {
    return this.down;
  }
  
  public void setDown(boolean val, MotionMouseEvent e)
  {
    if(val == false)
    {
      framesDrawn = 0;
      last = null;
    }
    
    this.down = val;
    this.last = e;
    this.startTime = System.currentTimeMillis();
  }
  
  public abstract MotionBrush deepCopy();
  
  @Override
  public MotionBrush clone()
  {
    MotionBrush copy = deepCopy();
    copy.down = down;
    copy.last = last.copy();
    copy.framesDrawn = framesDrawn;
    copy.splitSize = splitSize;
    copy.startTime = startTime;
    return copy;
  }
}
