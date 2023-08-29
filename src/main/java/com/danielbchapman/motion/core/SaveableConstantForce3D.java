package com.danielbchapman.motion.core;

import toxi.geom.Vec3D;
import toxi.physics3d.VerletParticle3D;

public class SaveableConstantForce3D extends SaveableParticleBehavior3D<SaveableConstantForce3D>
{
  public SaveableConstantForce3D(Vec3D force)
  {
    vars.force = force;
  }
  @Override
  public void apply(VerletParticle3D p)
  {
    p.addForce(vars.scaledForce);
  }

  @Override
  public void configure(float timeStep)
  {
    vars.timeStep = timeStep;
    setForce(vars.force);
  }
  
  public Vec3D getForce()
  {
    return vars.force;
  }
  public void setForce(Vec3D force)
  {
    vars.force = force;
    vars.scaledForce = force.scale(vars.timeStep);
  }

  @Override
  public String save()
  {
    System.out.println(this + " running? " + vars.running);
   return PersistentVariables.toLine(vars); 
  }

  @Override
  public SaveableConstantForce3D load(String data)
  {
    vars = PersistentVariables.fromLine(data);
    return this;
  }

}
