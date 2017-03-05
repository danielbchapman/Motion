package com.danielbchapman.physics.toxiclibs;

import java.util.ArrayList;

import processing.core.PGraphics;
import toxi.geom.Vec3D;

public class ParticleLayer extends Layer
{
  int gridX;
  int gridY;
  int spacing;
  String data = "This is the end of days";
  ArrayList<Emitter<?>> emitters = new ArrayList<>();
  public ParticleLayer()
  {
    for(int i = 0; i < 10; i++)
    {
      float x = (float) Actions.engine.width / (float)10;
      x -= 75f;
      x *= i+1;
      emitters.add(new LetterEmitter(data, new Vec3D(x, -100, 0), Vec3D.randomVector(), 5000, 250, 2f, 25));
    }
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
    
    for(Emitter<?> e : emitters)
      e.draw(g);
  }

  @Override
  public void go(MotionEngine engine)
  {
  }

  @Override
  public void update()
  {
    for(Emitter<?> e : emitters)
      e.update(System.currentTimeMillis());
  }
}
