package com.danielbchapman.physics.toxiclibs;

import java.util.ArrayList;

import com.danielbchapman.brushes.SaveableBrush;

import processing.core.PGraphics;
import toxi.geom.Vec3D;

public class EmitterLayer extends Layer
{
  int gridX;
  int gridY;
  int spacing;
  String data = "This is the end of days";
  ArrayList<Emitter<?>> emitters = new ArrayList<>();
  public EmitterLayer()
  {
    int count = 25;
    for(int i = 0; i < count; i++)
    {
      float x = ((float) Actions.engine.width -75f) / (float)count;
      x *= i+1;
      emitters.add(new LetterEmitter(data, new Vec3D(x, -100, 0), Vec3D.randomVector(), 15000, 250, 2f, 25));
    }
    
    for(int i = 0; i < count; i++)
    {
      float x = ((float) Actions.engine.width -45f) / (float)count;
      x *= i+1;
      emitters.add(new LetterEmitter(data, new Vec3D(x, -100, 150), Vec3D.randomVector(), 15000, 250, 2f, 25));
    }
  }
  
  public Point[] init()
  {
    Point[] grid = new Point[0];
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
