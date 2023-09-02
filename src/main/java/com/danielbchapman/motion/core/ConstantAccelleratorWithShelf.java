package com.danielbchapman.motion.core;

import lombok.Data;
import lombok.EqualsAndHashCode;
import toxi.geom.Vec3D;
import toxi.physics3d.VerletParticle3D;

@Data
@EqualsAndHashCode(callSuper = true, doNotUseGetters = true)
public class ConstantAccelleratorWithShelf extends SaveableParticleBehavior3D<ConstantAccelleratorWithShelf>
{

  public ConstantAccelleratorWithShelf(Vec3D gravity)
  {
    vars.force = gravity;
    //X constraints 
    vars.userAMax = 10000f;
    vars.userAMin = 0;
    //Y constraints
    vars.userBMax = 10000f;
    vars.userBMin = 0;
    //Z constraints
    vars.userCMax = 10000f;
    vars.userCMin = 0;
  }

  //Simple gravity with no bounce, this just stops particles that hit a threshold.
  public void apply(VerletParticle3D p)
  {
  	Vec3D force = vars.scaledForce.copy();
  	  	
  	if(p.x > vars.userAMax || p.x < vars.userAMin) 
  	{
  		force.x = 0;
  		return;
  	}
  	
    if(p.y > vars.userBMax || p.x < vars.userBMin) 
    {
      force.y = 0;
      return;
    }
     
    if(p.z > vars.userCMax || p.z < vars.userCMin) 
    {
    	force.z = 0;
    	return;
    }
    
    if (p instanceof Point)
    {
      Point px = (Point) p;
      if(px.enableRotation)
        px.addAngularForce(vars.scaledForce.scale(5f));
    }
    
    p.addForce(force);
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
  public ConstantAccelleratorWithShelf load(String data)
  {
    this.vars = PersistentVariables.fromLine(data);
    return this;
  }
  @Override
  public String save()
  {
    System.out.println(this + " AngularGravity? " + vars.running);
    return PersistentVariables.toLine(vars);
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
