package com.danielbchapman.motion.core;

import toxi.geom.Vec3D;
import toxi.physics3d.behaviors.ParticleBehavior3D;

public interface IPhysicsBrush extends ParticleBehavior3D
{
  //Add the position property
  public Vec3D getPosition();
  public void setPosition(Vec3D Position);
}
