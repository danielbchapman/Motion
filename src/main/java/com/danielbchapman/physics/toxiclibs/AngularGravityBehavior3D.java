package com.danielbchapman.physics.toxiclibs;

import lombok.Data;
import lombok.ToString;
import toxi.geom.Vec3D;
import toxi.physics3d.VerletParticle3D;
import toxi.physics3d.behaviors.GravityBehavior3D;

@Data
public class AngularGravityBehavior3D extends GravityBehavior3D
{
  Vec3D original = new Vec3D();
  public AngularGravityBehavior3D(Vec3D gravity)
  {
    super(gravity);
    original = gravity.copy();
  }
  
  public void updateMagnitude(float newValue)
  {
    Vec3D f = getOriginal().copy().normalize();
    f.scaleSelf(newValue);
    setForce(f);;
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
  
  @Override
  public String toString()
  {
    return super.toString() + " " + getForce().magnitude() + " " + getForce();
  }

}
