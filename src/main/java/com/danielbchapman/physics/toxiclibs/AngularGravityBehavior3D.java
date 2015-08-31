package com.danielbchapman.physics.toxiclibs;

import lombok.Data;
import toxi.geom.Vec3D;
import toxi.physics3d.VerletParticle3D;

@Data
public class AngularGravityBehavior3D extends SaveableParticleBehavior3D<AngularGravityBehavior3D>
{

  public AngularGravityBehavior3D()
  {
    setStopPoint(((float)Actions.HEIGHT) / 2f);
    vars.userC = Actions.HEIGHT;
  }
  // Vec3D original = new Vec3D();
  public AngularGravityBehavior3D(Vec3D gravity)
  {
    vars.force = gravity;
    vars.backup = gravity.copy();
    vars.userC = Actions.HEIGHT;
  }

  public void apply(VerletParticle3D p)
  {
    if(p.y > vars.userC)
    {
      return;//Don't apply force, it is on the floor
    }
      
    if (p instanceof Point)
    {
      Point px = (Point) p;
      if(px.enableRotation)
        px.addAngularForce(vars.scaledForce.scale(5f));
    }
    
    p.addForce(vars.scaledForce);
  }
  
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

  public float getStopPoint()
  {
    return vars.userC;
  }

  @Override
  public AngularGravityBehavior3D load(String data)
  {
    this.vars = ForceVariables.fromLine(data);
    return this;
  }
  @Override
  public String save()
  {
    System.out.println(this + " AngularGravity? " + vars.running);
    return ForceVariables.toLine(vars);
  }

  public void setForce(Vec3D force) {
      this.vars.force = force;
      this.vars.scaledForce = force.scale(vars.timeStep * vars.timeStep);
  }

  public void setGravity(Vec3D v)
  {
    vars.force = v.copy();
    vars.backup = v.copy();
  }

  public void setStopPoint(float stop)
  {
    vars.userC = stop;
  }

  @Override
  public String toString()
  {
    return super.toString() + " " + getForce().magnitude() + " " + getForce();
  }

  public void updateMagnitude(float newValue)
  {
    Vec3D f = vars.backup.copy().normalize();
    f.scaleSelf(newValue);
    setForce(f);
  }

}
