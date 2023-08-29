package com.danielbchapman.motion.core;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;
import processing.core.PGraphics;
import toxi.physics3d.VerletPhysics3D;
import toxi.physics3d.behaviors.ParticleBehavior3D;



/**
 * A scene that has a built in physics engine for initialization. It
 * also has all the logic necessary to accept PhysicsBrushes so you 
 * don't need to deal with handling them on each layer.
 *
 * @author Daniel B. Chapman 
 * @since Apr 2, 2017
 */

public class PhysicsScene extends BaseScene
{
  @Getter
  @Setter
  protected VerletPhysics3D physics;
  
  protected ArrayList<IPhysicsBrush> active = new ArrayList<>();
  @Getter
  @Setter
  protected ArrayList<ParticleBehavior3D> behaviors = new ArrayList<>();
  
  public boolean isActive(ParticleBehavior3D behavior)
  {
    return behaviors.contains(behavior);
  }
  
  public boolean addBehavior(ParticleBehavior3D behavior)
  {
    if (behaviors.contains(behavior))
      return false;
    // System.out.println("Adding Behavior: " + behavior);
    physics.addBehavior(behavior);
    if (behavior instanceof SaveableParticleBehavior3D)
    {
      // System.out.println("Marking behavior as running: " + behavior);
      ((SaveableParticleBehavior3D<?>) behavior).setRunning(true);
      // System.out.println(((SaveableParticleBehavior3D<?>) behavior).vars.running);
    }

    behaviors.add(behavior);
    return true;
  }

  public void removeBehavior(ParticleBehavior3D behavior)
  {
    physics.removeBehavior(behavior);
    // if (activeBehaviors.remove(behavior))
    // System.out.println("Removing Behavior: " + behavior);

    if (behavior instanceof SaveableParticleBehavior3D)
    {
      System.out.println("Marking behavior as not running: " + behavior);
      ((SaveableParticleBehavior3D<?>) behavior).setRunning(false);
      System.out.println(((SaveableParticleBehavior3D<?>) behavior).vars.running);
    }
  }
  
  @Override
  public void initialize(Motion motion)
  {
    physics = new VerletPhysics3D();
  }
  
  @Override
  public void update(long time)
  {
    active.forEach(b -> physics.addBehavior(b));
    physics.update();
    active.forEach(b -> physics.removeBehavior(b));
    active.clear(); // Update the previous forces then dump the list.
  }
  
  @Override
  public void applyBrush(MotionBrush brush, PGraphics g, MotionMouseEvent point)
  {
    
     if(brush instanceof IPhysicsBrush)
     {
       //System.out.println("Adding " + brush);
       IPhysicsBrush b = (IPhysicsBrush) brush;
       b.setPosition(point);
       active.add(b); 
     }
  }  
}
