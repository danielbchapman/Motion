package com.danielbchapman.physics.toxiclibs;

import toxi.geom.Vec3D;
import toxi.physics3d.VerletParticle3D;
import toxi.physics3d.behaviors.GravityBehavior3D;

public class AngularGravityBehavior3D extends GravityBehavior3D
{

  public AngularGravityBehavior3D(Vec3D gravity)
  {
    super(gravity);
  }
  
  @Override
  public void apply(VerletParticle3D p)
  {
    if(p instanceof Point)
    {
      Point px = (Point) p;
      px.addAngularForce(scaledForce.scale(5f));
    }
    super.apply(p);
  }

}
