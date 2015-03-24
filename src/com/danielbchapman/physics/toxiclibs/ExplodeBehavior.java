package com.danielbchapman.physics.toxiclibs;

import toxi.geom.Vec3D;
import toxi.physics3d.VerletParticle3D;
import toxi.physics3d.behaviors.ParticleBehavior3D;


/**
 * The ExplodeBehavior class provides a perpendicular force as well as as an opposite
 * for that causes the points to be pushed away in a spherical shape.
 *
 */
public class ExplodeBehavior implements ParticleBehavior3D
{
  protected float force;
  protected float timeStep;
  protected float maxForce = 10f;
  protected float minForce = 0.1f;
  protected Vec3D location;
  protected Vec3D direction;

  public ExplodeBehavior(Vec3D direction, float force)
  {
    this.direction = direction;
    this.force = force;
  }
  
  @Override
  public void apply(VerletParticle3D p)
  {
    Vec3D distanceV = p.sub(location);
    float distance = distanceV.magnitude();
    float modifier = force / distance;// / mag; // 1 / n^2
    
    if(modifier > maxForce)
      modifier = maxForce;
    
    if(modifier < minForce)
      return;//skip
    //noise

    //Away and in the direction 
    Vec3D vForce = direction.normalizeTo(modifier);
    distanceV = distanceV.normalizeTo(modifier);  
    vForce = vForce.add(distanceV);
    
    p.addForce(vForce);
  }

  @Override
  public void configure(float timeStep)
  {
    this.timeStep = timeStep;
  }

}
