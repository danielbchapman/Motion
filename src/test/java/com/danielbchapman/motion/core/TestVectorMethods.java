package com.danielbchapman.motion.core;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Test;

import test.Log;
import toxi.geom.Vec3D;

public class TestVectorMethods
{
  @Test
  public void vectorMethod()
  {
    MotionMouseEvent source = new MotionMouseEvent();
    source.left = true;
    MotionMouseEvent point = source.copy(new Vec3D(50,50,0));
    MotionMouseEvent last = source.copy(new Vec3D(0,0,0));
    MotionBrush b = new VectorMouseBrush();
    b.setDown(true);
    b.splitSize = 5f;
    b.last = last.copy();
    ArrayList<Vec3D> points = new ArrayList<>();
    boolean shouldBeActive = b.checkActive(point);
    if(shouldBeActive)
    {
      if(!b.isDown())
        b.setDown(true, point);
      
      if(!b.isVectorBrush())
        points.add(point);     
      else 
      {
        if(b.last == null)//Start draw
        {
          System.out.println("no vector");
          points.add(point); 
        }
        else
        {
          Vec3D scalarPoint = new Vec3D(point);
          Vec3D scalar = scalarPoint.sub(b.last);
          float mag = scalar.magnitude();
          int steps = (int) (mag / b.splitSize);
          if(steps <= 1)
          { 
            boolean draw = b.applyWhenIdle() || mag >= 1;//pixel-space, so less than 1 is the same point
            
            if(draw)
            {
              points.add(point);
              b.last = point;
            }
          }
          else //Multiple points
          {
            for(int i = 0; i < steps; i++)
            {
              float subMag = ((float)i) * b.splitSize;
              Vec3D newSub = scalar.getNormalizedTo(subMag);
              Vec3D newPoint = b.last.add(newSub);
              MotionMouseEvent copy = point.copy(newPoint);
              points.add(copy);
            }
            
            //Draw at point for the last one
            points.add(point);
            b.last = point;
          }
        }
      }
    }
    else
    {
      if(b.isDown())
        b.setDown(false);
    }
    
    Log.debug("Start at (" + last.x + ", " + last.y + ")");
    Log.debug("Click at (" + point.x + ", " + point.y + ")");
    for(Vec3D v : points)
    {
      Log.debug("\t-(" + v.x + ", " + v.y + ")" );
    }
  }
}
