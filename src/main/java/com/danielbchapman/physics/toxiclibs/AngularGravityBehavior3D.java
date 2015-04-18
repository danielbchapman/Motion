package com.danielbchapman.physics.toxiclibs;

import java.sql.Savepoint;

import lombok.Data;
import toxi.geom.Vec3D;
import toxi.physics3d.VerletParticle3D;
import toxi.physics3d.behaviors.ParticleBehavior3D;

@Data
public class AngularGravityBehavior3D extends SaveableParticleBehavior3D<AngularGravityBehavior3D>
{

  public void configure(float timeStep)
  {
    vars.timeStep = timeStep;
    setForce(vars.force);
  }

  /**
   * @return the force
   */
  public Vec3D getForce()
  {
    return vars.force;
  }

  public void setForce(Vec3D force) {
      this.vars.force = force;
      this.vars.scaledForce = force.scale(vars.timeStep * vars.timeStep);
  }

  // END FIX TWO
  ForceVariables vars = new ForceVariables();

  // Vec3D original = new Vec3D();
  public AngularGravityBehavior3D(Vec3D gravity)
  {
    vars.force = gravity;
    vars.backup = gravity.copy();
  }

  public void updateMagnitude(float newValue)
  {
    Vec3D f = vars.backup.copy().normalize();
    f.scaleSelf(newValue);
    setForce(f);
  }

  public void apply(VerletParticle3D p)
  {
    if (p instanceof Point)
    {
      Point px = (Point) p;
      px.addAngularForce(vars.scaledForce.scale(5f));
    }
    
    p.addForce(vars.scaledForce);
  }

  @Override
  public String toString()
  {
    return super.toString() + " " + getForce().magnitude() + " " + getForce();
  }

  @Override
  public String save()
  {
    System.out.println(this + " running? " + vars.running);
    return ForceVariables.toLine(vars);
  }

  @Override
  public AngularGravityBehavior3D load(String data)
  {
    this.vars = ForceVariables.fromLine(data);
    return this;
  }

}
