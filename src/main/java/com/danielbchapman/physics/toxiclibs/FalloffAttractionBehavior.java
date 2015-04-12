package com.danielbchapman.physics.toxiclibs;

import toxi.geom.Vec3D;
import toxi.physics3d.VerletParticle3D;
import toxi.physics3d.behaviors.AttractionBehavior3D;

/**
 * An attraction behavior that falls off over a larger radius. This is significantly
 * more intensive than the regular behavior as it will apply to each particle in the 
 * system.
 *
 */

public class FalloffAttractionBehavior extends AttractionBehavior3D
{
  protected float minForce = 0.1f;
  protected float maxForce = 2f;

  public FalloffAttractionBehavior(Vec3D attractor, float radius, float strength, float jitter)
  {
    super(attractor, radius, strength, jitter);
  }

  @Override
  public void apply(VerletParticle3D p)
  {
    Vec3D delta = null;
    if(strength < 0)
      delta = p.sub(attractor);
    else
      delta = attractor.sub(p);
    float dist = delta.magSquared();
    
    //check to see if it is inside the sticky radius--doesn't help a "push"
    if(strength > 0)
    {
      //inside the radius
      if(dist < radiusSquared)
        return; 
    }
    
    float modifier = 0f;
    if(strength < 0)
      modifier = this.strength / dist * -1f;
    else
      modifier = this.strength / dist;
    
    if(modifier > maxForce)
      modifier = maxForce;
    
    if(modifier < minForce)
      return;

    Vec3D force = delta.normalizeTo(modifier);
    p.addForce(force);
  }

}
