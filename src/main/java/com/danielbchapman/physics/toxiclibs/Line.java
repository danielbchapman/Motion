package com.danielbchapman.physics.toxiclibs;

import lombok.Data;
import processing.core.PConstants;
import processing.core.PGraphics;

public class Line extends Shape
{
  Point[] points;
  
  public Line(Point[] points)
  {
    this.points = points;
  }
  @Override
  public void render(PGraphics g)
  {
   
    Point current = points[0];
    Point next;
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
    
    for(Point p : points)
      buf.append("\n\t").append(p);
    
    buf.append("\n");
    return buf.toString();
  }
}
