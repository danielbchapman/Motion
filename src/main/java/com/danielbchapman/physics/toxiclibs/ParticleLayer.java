package com.danielbchapman.physics.toxiclibs;

import java.util.Random;

import processing.core.PGraphics;

public class ParticleLayer extends Layer
{
  int gridX;
  int gridY;
  int spacing;
  
  public ParticleLayer()
  {
  }
  
  public Point[] init()
  {
    gridX = 120/2;
    gridY = 76/2;
    spacing = 40;
    Point[] grid = new Point[gridX * gridY];
  
    for(int i = 0; i < gridY; i++)
    {
      for(int j = 0; j < gridX; j++)
      {
        grid[i * gridX + j] = new Point(j * spacing, i * spacing, 0, 1f);
      }
    }
    
    try
    {
      Thread.sleep(200);
    }
    catch (InterruptedException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    return grid;
  }

  /* (non-Javadoc)
   * @see com.danielbchapman.physics.toxiclibs.Scene#render(processing.core.PGraphics)
   */
  @Override
  public void render(PGraphics g)
  {
    g.background(0); //black
    g.strokeWeight(2f);
    g.stroke(255);
    g.fill(255, 255, 255);
    g.pushMatrix();
    g.translate(10, 10, 0);//Offset to see correctly
    for(int i = 0; i < points.length; i++)
    {
      g.pushMatrix();
      Point p  = points[i];
      
      g.translate(p.x, p.y, p.z);
      Point.rotation(g, p);
      //g.rotate((p.angular.magnitude())/360f, p.angular.x, p.angular.y, p.angular.z);
      g.text("Word", 0,0,0);
      //Rotation based on angular
//      g.translate(-p.x, -p.y, -p.z);
//      g.rotate(-p.angular.x);
      g.popMatrix();
      //g.point(p.x, p.y, p.z); 
    }
    
    g.popMatrix();
  }

  @Override
  public void go(MotionEngine engine)
  {
  }
}
