package com.danielbchapman.physics.toxiclibs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import lombok.Getter;
import lombok.Setter;
import processing.core.PGraphics;
import toxi.geom.Vec3D;
import toxi.physics3d.VerletPhysics3D;

public abstract class Emitter<T extends Point>
{
  protected PersistentVariables vars = new PersistentVariables();
  protected ArrayList<T> children = new ArrayList<>();
  protected Random rand = new Random();
  protected long lastTime = -1L;
  protected long nextDelta = -1L;
  @Getter
  @Setter
  public VerletPhysics3D physics = Actions.engine.getPhysics();
  
  public Emitter(Vec3D position, Vec3D heading, int lifeSpan, int rate, float randomVector, int randomTime)
  {
    vars.position = position;
    vars.force = heading;
    vars.userA = lifeSpan;
    vars.userB = randomVector;
    vars.userC = randomTime;
    vars.timeStep = rate;
  }
  
  public void update(long time)
  {
    
    if(lastTime == -1L)
    {
      lastTime = time;
      addPoint(time);
      return;
    }
    
    if(time - lastTime > nextDelta)
    {
      addPoint(time);
      lastTime = time;
    }
//    else
//      System.out.println("[SKIPPED] Calling update" + time + ", " + lastTime + " " + nextDelta);
  }
  boolean stop = false;
  public void stop(boolean stop)
  {
    this.stop = stop;
  }
  public abstract T createPoint(float x, float y, float z, float w);
  
  public void addPoint(long time)
  {
    if(stop)
      return;
    T p = createPoint(vars.position.x, vars.position.y, vars.position.z, 1f);
    p.addForce(vars.force);
    if(vars.userB > 0)
      p.addForce(Vec3D.randomVector().scaleSelf(vars.userB));
    if(vars.userC > 0.1f)
      nextDelta = (long) (vars.timeStep + rand.nextInt((int)vars.userC));
    else
      nextDelta = (long) vars.timeStep;
    
    p.life = (int) vars.userA;
    p.created = time;
    children.add(p);
    
    Iterator<T> it = children.iterator();
    while(it.hasNext())
    {
      Point px = it.next(); //This should probably implement a "fadeable" interface
      if(px != null)
        if(px.life < time - px.created)
        {
          Actions.engine.getPhysics().removeParticle(px);
          it.remove();
        }
    }
    
    physics.addParticle(p);
  }
  
  public abstract void draw(PGraphics g);
}
