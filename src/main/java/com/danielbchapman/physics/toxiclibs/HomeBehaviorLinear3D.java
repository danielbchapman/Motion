package com.danielbchapman.physics.toxiclibs;

import toxi.geom.Vec3D;
import toxi.physics3d.VerletParticle3D;


/**
 * The linear home behavior clears the velocity from the particle and attempts to 
 * ease it home using the easing (constant * distance) until it snaps home.
 *
 */

public class HomeBehaviorLinear3D extends SaveableConstantForce3D
{
  public HomeBehaviorLinear3D(float easing, float min, float max)
  {
    super(new Vec3D());//Zero Vector
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
      float maxAngle = vars.maxForce * 0.1f;//do we need this?
      float delta = vars.userA * mag;
      float magA = p.angular.magnitude();
      float deltaA = magA * vars.userA;
      if(delta > vars.maxForce)
        delta = mag - vars.maxForce;
      
      //Position
      dir = dir.normalizeTo(mag - delta);//move X units of distance
      
      p.x = p.home.x + dir.x;
      p.y = p.home.y + dir.y;
      p.z = p.home.z + dir.z;
      
      //Angular
      if(magA > 0.001f)
      {
        if(magA > maxAngle)
          p.angular = p.angular.normalizeTo(magA - deltaA);
        else
          p.angular = p.angular.normalizeTo(magA - deltaA);
      }
    }
     
  }
  @Override
  public void configure(float timeStep)
  {
    //Ignore...
  }  
}
