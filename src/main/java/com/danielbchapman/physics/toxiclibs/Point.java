package com.danielbchapman.physics.toxiclibs;

import processing.core.PGraphics;
import lombok.Data;
import lombok.ToString;
import toxi.geom.Vec3D;
import toxi.physics3d.VerletParticle3D;
import toxi.physics3d.behaviors.ParticleBehavior3D;
import toxi.physics3d.constraints.ParticleConstraint3D;

@Data
@ToString(callSuper=true)
public class Point extends VerletParticle3D
{
  public Vec3D home;
  //Rotation x/y/z I'm making the assumption our points are "flat" objects for calculations
  public Vec3D angular = new Vec3D(0,0,0);
  Vec3D angPrevious = new Vec3D(0,0,0);
  Vec3D angTemp = new Vec3D(0,0,0);
  Vec3D angForce = new Vec3D(0,0,0);
  float mass = 1.0f;
  
  public Point()
  {
    this(0, 0, 0, 1);
  }
  
  public Point(float x, float y, float z, float w)
  {
    super(x, y, z, w);
    this.home = new Vec3D(x, y, z);
  }
  
  public Point(Point p)
  {
    this(p.x, p.y, p.z, p.weight);
    this.home = new Vec3D(p.home.x, p.home.y, p.home.z);
    this.angular = p.angular.copy();
    this.angPrevious = p.angPrevious.copy();
    this.angTemp = p.angTemp.copy();
    this.angForce = p.angForce.copy();
    
    if(p.behaviors != null)
      for(ParticleBehavior3D b : p.behaviors)
        this.addBehavior(b);
    
    if(p.constraints != null)
      for(ParticleConstraint3D c : p.constraints)
        this.addConstraint(c);
  }
  
  public void addAngularForce(Vec3D f)
  {
    angForce.addSelf(f);
  }
  
  @Override
  protected void applyForce()
  {
    //Angular Momentum
    angTemp.set(angular);
    angular.addSelf(angular.sub(angPrevious).addSelf(angForce.scale(weight)));
    angPrevious.set(angTemp);
    angForce.clear();
    super.applyForce();
  }
 
  @Override
  public Point copy()
  {
    return copyTranslate(0, 0, 0);
  }

  /**
   * Create a copy of this point but translate the particles so that they physics engine
   * maintains veloctiy etc... 
   * @param x the x translation
   * @param y the y translation
   * @param z the z translation
   * @return a copy of this point that is translated by the vector 
   * 
   */
  public Point copyTranslate(float x, float y, float z)
  {
    Vec3D trans = new Vec3D(x, y, z);
    Point copy = new Point(this);
    copy.addSelf(trans);
    copy.home.addSelf(trans);
    copy.temp.addSelf(trans);
    copy.prev.addSelf(trans);
    return copy;
  }
  /**
   * Create a copy of this point but translate the particles so that they physics engine
   * maintains velocity etc... but does not alter the home position of the particle. 
   * @param x the x translation
   * @param y the y translation
   * @param z the z translation
   * @return a copy of this point that is translated by the vector 
   * 
   */  
  public Point copyTranslateNoHome(float x, float y, float z)
  {
    Vec3D trans = new Vec3D(x, y, z);
    Point copy = new Point(this);
    copy.addSelf(trans);
    copy.home = this.home.copy();
    copy.temp.addSelf(trans);
    copy.prev.addSelf(trans);
    return copy;
  }
  
  @Override
  public VerletParticle3D scaleVelocity(float scl) {
    angPrevious.interpolateToSelf(angular, 1f - scl);//drag
    return super.scaleVelocity(scl);
  }
  
  public static void rotation(PGraphics g, Point p)
  {
    g.rotate((p.angular.magnitude())/360f, p.angular.x, p.angular.y, p.angular.z);
  }
}
