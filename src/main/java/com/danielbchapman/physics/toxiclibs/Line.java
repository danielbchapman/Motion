package com.danielbchapman.physics.toxiclibs;

import processing.core.PConstants;
import processing.core.PGraphics;

public class Line extends Shape
{
  PointOLD[] points;
  
  public Line(PointOLD[] points)
  {
    this.points = points;
  }
  @Override
  public void render(PGraphics g)
  {
   
    PointOLD current = points[0];
    PointOLD next;
    g.beginShape(PConstants.LINE);
    for(int i = 1; i < points.length; i++)
    {
      next = points[i];
      if(next == null)
      {
        break;
      }
        
      g.line(current.x, current.y,  current.z, next.x, next.y, next.z);
      current = next;
    }
    g.endShape(PConstants.LINE);
  }
  
  @Override
  public String toString()
  {
    StringBuilder buf = new StringBuilder();
    buf.append("Line -> " + super.toString());
    
    for(PointOLD p : points)
      buf.append("\n\t").append(p);
    
    buf.append("\n");
    return buf.toString();
  }
}
