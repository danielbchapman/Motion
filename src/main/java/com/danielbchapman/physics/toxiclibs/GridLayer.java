package com.danielbchapman.physics.toxiclibs;

import java.util.Random;

import processing.core.PGraphics;

public class GridLayer extends Layer
{
  int gridX;
  int gridY;
  int spacing;
  Line[] lines;
  
  public GridLayer()
  {
  }
  
  public Point[] init()
  {
    gridX = 120;
    gridY = 76;
    spacing = 10;
    Point[] grid = new Point[gridX * gridY];
    lines = new Line[gridX + gridY]; //lines
    for(int i = 0; i < gridY; i++)
    {
      for(int j = 0; j < gridX; j++)
      {
        grid[i * gridX + j] = new Point(j * spacing, i * spacing, 0, 1f);
      }
    }
    
    for(int i = 0; i < gridY; i++)
    {
      Point[] row = new Point[gridX];
      for(int j = 0; j < gridX; j++)
      {
        row[j] = grid[i*gridX + j];
      }
      Line line = new Line(row);
      lines[i] = line;
    }
    
    for(int i = 0; i < gridX; i++)
    {
      Point[] col = new Point[gridY];
      for(int j = 0; j < gridY; j++)
      {
        col[j] = grid[i  + gridX * j];
      }
      Line line = new Line(col);
      lines[i+gridY] = line;
    }
    
    for(int i = 0; i < lines.length; i++)
      System.out.println(lines[i]);
    
    try
    {
      Thread.sleep(200);
    }
    catch (InterruptedException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    for(int i = 0 ; i < lines.length; i++)
    {
      System.out.println("Line from -> " + lines[i].points[0] + " to " + lines[i].points[lines[i].points.length -1]);
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
    g.pushMatrix();
    g.translate(10, 10, 0);//Offset to see correctly
    for(int i = 0; i < lines.length; i++)
    {
      //System.out.println("r: " + lines[i]);
      lines[i].render(g); 
    }
    
    g.popMatrix();
  }
  
  /**
   * A random preset for animation that forces all the particles to one
   * point offscreen so that they can "crawl" onto the screen.  
   */
  public void offscreen()
  {
    Random rand = new Random();
    boolean direction = false;
    int padding = 100;
    int height = gridY * spacing + padding;
    int width = gridX * spacing + padding;
    for(int i = 0; i < lines.length; i++)
    {
      if(i < gridY)//horizontal
      {
        direction = rand.nextBoolean();
        
        if(direction)
          for(Point p : lines[i].points)
            p.x = width;
        else
          for(Point p : lines[i].points)
            p.x = -width;
      }
      else
      {
        direction = rand.nextBoolean();
        
        if(direction)
          for(Point p : lines[i].points)
            p.x = height;
        else
          for(Point p : lines[i].points)
            p.x = -height;
      }
    }
  }
  
  /**
   * A pretty awesome happy accident...
   * 
   * A random preset for animation that forces all the particles to one
   * point offscreen so that they can "crawl" onto the screen.  
   */
  public void offscreenHappyAccident()
  {
    Random rand = new Random();
    boolean direction = false;
    int padding = 100;
    int height = gridY * spacing + padding;
    int width = gridX * spacing + padding;
    for(int i = 0; i < lines.length; i++)
    {
      if(i < gridY)//horizontal
      {
        direction = rand.nextBoolean();
        
        if(direction)
          for(Point p : lines[i].points)
            p.x = width;
        else
          for(Point p : lines[i].points)
            p.x = -width;
      }
      else
      {
        direction = rand.nextBoolean();
        
        if(direction)
          for(Point p : lines[i].points)
            p.x = height;
        else
          for(Point p : lines[i].points)
            p.x = -height;
      }
    }
  }

  @Override
  public void go(MotionEngine engine)
  {
  }

  @Override
  public void update()
  {
  }

}
