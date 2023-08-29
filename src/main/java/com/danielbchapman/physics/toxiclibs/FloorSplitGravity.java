package com.danielbchapman.physics.toxiclibs;

import com.danielbchapman.motion.core.SaveableParticleBehavior3D;

import lombok.Data;
import lombok.EqualsAndHashCode;
import toxi.geom.Vec3D;
import toxi.physics3d.VerletParticle3D;

@Data
@EqualsAndHashCode(callSuper = true, doNotUseGetters = true)
public class FloorSplitGravity extends SaveableParticleBehavior3D<FloorSplitGravity>
{
  float breakLine = ((float)ActionsOLD.HEIGHT) / 3.05f;
  // Vec3D original = new Vec3D();
  public FloorSplitGravity(Vec3D gravity)
  {
    vars.force = gravity;
    vars.backup = gravity.copy();
    setStopPoint(breakLine);
  }

  public void apply(VerletParticle3D p)
  {
    PointOLD px = null;
    if(p instanceof PointOLD)
      px = (PointOLD) p;
    if(p.y > vars.userC)
    {
      if(!px.isEnableRotation())
      {
        //apply(px, p, Vec3D.randomVector().scaleSelf(10f), Vec3D.randomVector().scaleSelf(10f));
        px.setEnableRotation(true);
        
        //reset home to random vector
        int randX = Util.rand(0, ActionsOLD.WIDTH);
        int randY = Util.rand(0,  breakLine + ActionsOLD.HEIGHT - breakLine) + (int)breakLine;
        px.setHome(new Vec3D(randX, randY, 0));
        return;
      }
      
      return;
    }
      
    if (px != null)
    {
      if(px.enableRotation)
        px.addAngularForce(vars.scaledForce.scale(5f));
    }
    
    apply(px, p, vars.scaledForce, vars.scaledForce.scale(5f));
  }
  
  private void apply(PointOLD px, VerletParticle3D p, Vec3D force, Vec3D angular)
  {
    p.addForce(force);
    if(px != null && px.isEnableRotation())
      px.addAngularForce(angular);
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
  public FloorSplitGravity load(String data)
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

  public void setDriftPoint(float f)
  {
    vars.userB = f;
  }
  
  public float getDriftPoint(float f)
  {
    return vars.userB;
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
