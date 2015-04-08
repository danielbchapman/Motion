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
    gridX = (int) (120 *1.0f);
    gridY = (int) (76* 1.0f);
    spacing = 10 *2;
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
      Point p  = points[i];
      //g.text("Word", p.x, p.y, p.z);
      g.point(p.x, p.y, p.z); 
    }
    
    g.popMatrix();
  }
 
}
