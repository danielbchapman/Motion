package com.danielbchapman.physics.toxiclibs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import lombok.Getter;
import lombok.Setter;
import processing.core.PGraphics;
import toxi.geom.Vec3D;

public abstract class AbstractEmitter<T extends Moveable>
{
  protected ArrayList<T> children = new ArrayList<T>();
  protected long lastTime = -1L;
  protected long nextDelta = -1L;
  protected Vec3D position;
  protected Random rand = new Random();
  
  @Getter
  @Setter
  private float randomAmount = 0f;
  
  @Getter
  @Setter
  private int randomTime = 0;
  
  @Getter
  @Setter
  private int timeStep = 1000;
  
  @Getter
  @Setter
  private int lifeSpan = 10000;
  
  private boolean running = true; 
  
  public AbstractEmitter(Vec3D position)
  {
    this.position = position.copy();
  }
  
  public void update(long time)
  {
    if(lastTime == -1L)
    {
      lastTime = time;
      addPoints(time);
      return;
    }
    
    if(time - lastTime > nextDelta)
    {
      addPoints(time);
      lastTime = time;
    }
  }
  
  public void addPoints(long time)
  {
    if(!running)
      return;
    
    System.out.println("Emitting points");
    T instance = onEmit(time);
    children.add(instance);
    
    for(Point p : instance.getPoints())
    {
      if(randomAmount > 0)
        p.addForce(Vec3D.randomVector().scaleSelf(randomAmount));
      
      if(randomTime > 0)
        nextDelta = timeStep + rand.nextInt(randomTime);
      else
        nextDelta = timeStep;
      
      p.life = lifeSpan;
      p.created = time;
      Actions.engine.getPhysics().addParticle(p);
    }
    
    //Remove expired children
    Iterator<T> it = children.iterator();
    while(it.hasNext())
    {
      Moveable m = it.next();
      if(m != null)
      {
        Point[] points = m.getPoints();
        if(points.length > 0)
        {
          if(points[0].life < time - points[0].created)
          {
            //Clear the points from the engine
            for(Point p : points)
              Actions.engine.getPhysics().removeParticle(p);
            
            //Clear the object
            it.remove();
          }
        }
        else
          it.remove(); //no points in this shape
      }
      else
        it.remove(); //movable is null
    }
  }
  
  public void stop()
  {
    running = false;
  }
  
  public void start()
  {
    running = true;
  }
  
  public abstract T onEmit(long time);
  public abstract void draw(PGraphics g);
}
