package com.danielbchapman.physics.toxiclibs;

import org.junit.Test;

import toxi.geom.Vec3D;
import toxi.physics3d.VerletParticle3D;
import toxi.physics3d.behaviors.ParticleBehavior3D;


/**
 * The linear home behavior clears the velocity from the particle and attempts to 
 * ease it home using the easing (constant * distance) until it snaps home.
 *
 */

public class HomeBehaviorLinear3D implements ParticleBehavior3D
{
  ForceVariables vars = new ForceVariables();
  public boolean enabled = false;
  public HomeBehaviorLinear3D(float easing, float min, float max)
  {
    vars.maxForce = max;
    vars.minForce = min;
    vars.userA = easing;
  }
  @Override
  public void apply(VerletParticle3D particle)
  {
    if(particle.isLocked())
      return;
    if(particle instanceof Point)
    {
      Point p = (Point) particle;
      Vec3D dir = p.sub(p.home);
      float mag = dir.magnitude();
      float delta = vars.userA * mag;
      
      if(delta > vars.maxForce)
        delta = mag - vars.maxForce;
      
      dir = dir.normalizeTo(mag - delta);//move X units of distance
      
      p.x = p.home.x + dir.x;
      p.y = p.home.y + dir.y;
      p.z = p.home.z + dir.z;
    }

     
  }
  @Override
  public void configure(float timeStep)
  {
    //Ignore...
  }  
}
