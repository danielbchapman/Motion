package com.danielbchapman.physics.toxiclibs;

import lombok.Data;
import lombok.ToString;
import toxi.geom.Vec3D;
import toxi.physics3d.VerletParticle3D;

@Data
@ToString(callSuper=true)
public class Point extends VerletParticle3D
{
  Vec3D home;
  //Rotation x/y/z I'm making the assumption our points are "flat" objects for calculations
  Vec3D angular = new Vec3D(0,0,0);
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
    this.home = new Vec3D(x, y, z);
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
  public VerletParticle3D scaleVelocity(float scl) {
    angPrevious.interpolateToSelf(angular, 1f - scl);//drag
    return super.scaleVelocity(scl);
  }
}
