package com.danielbchapman.artwork;

import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;

import com.danielbchapman.physics.vertlet.Point3D;

public class Line
{
  PVector rotation = new PVector();
  
  public Line()
  {
  }
  
  /**
   * 
   * @param g
   * @param points
   * @param rows
   * @param cols
   * @param spacing <Return Description>  
   * 
   */
  public void drawGrid(PGraphics g, Point3D[] points, int rows, int cols, int skip)
  {

    if(skip < 1)
      skip = 1;
    
    g.noFill();
    g.strokeWeight(1);
    g.noFill();
    
    //Horizontal    
    for(int r = 0; r < cols; r+= skip)
    {
//      g.pushMatrix();
      g.beginShape();
      for(int i = 0; i < rows; i++)
      {
        PVector p = points[r * cols + i].position;
        g.vertex(p.x, p.y, p.z);
      }

      g.endShape();
//      g.popMatrix();
    }
    
    //Vertical
//    for(int c = 0; c < rows; c+= skip)
//    {
//      g.pushMatrix();
//      g.beginShape();
//      for(int i = 0; i < rows; i++)
//      {
//        PVector p = points[c * cols + i].position;
//        g.vertex(p.x, p.y, p.z);
//      }
//
//      g.endShape();
//      g.popMatrix();      
//    }
  }
}
