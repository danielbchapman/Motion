package com.danielbchapman.motion.core;

import toxi.geom.Vec3D;
import lombok.ToString;

@ToString()
public class MotionMouseEvent extends Vec3D
{
  public MotionMouseEvent()
  {
  }
  
  public MotionMouseEvent(Vec3D vec)
  {
    super(vec);
  }
  public boolean left;
  public boolean right;
  public boolean center;
  public int pmouseX;
  public int pmouseY;
  public int pmouseZ = 0;
  
  public MotionMouseEvent copy()
  {
    return copy(this);
  }
  
  public MotionMouseEvent copy(Vec3D newPos)
  {
    MotionMouseEvent copy = new MotionMouseEvent(newPos);
    copy.center = center;
    copy.left = left;
    copy.right = right;
    copy.pmouseX = pmouseX;
    copy.pmouseY = pmouseY;
    copy.pmouseZ = pmouseZ;
    
    return copy;
  }
}
