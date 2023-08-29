package com.danielbchapman.physics.toxiclibs;

import toxi.geom.Vec3D;
import toxi.physics3d.VerletParticle3D;

public class HomeBehavior3D extends SaveableConstantForce3D
{
  public HomeBehavior3D(Vec3D force)
  {
    super(force);
    vars.maxForce = 0.5f;
    vars.userA = 0.05f; //easing
  }

  @Override
  public void apply(VerletParticle3D p3d)
  {
    if(p3d.isLocked() || !vars.enabled)
      return;
      
    if(p3d instanceof PointOLD)
    {
      PointOLD p = (PointOLD) p3d;
      Vec3D f = p.home.sub(p);//.scale(0.05f);//easing 
      float mag = f.magnitude();
      float stop = 0.05f; //halt
      float maxAngle = vars.maxForce * 0.1f;
      
      Vec3D angular = p.angular.copy().invert();//0,0,0 == home position
      float magA = angular.magnitude();
      
      if(magA > 0.001f)
      {
        if(magA > maxAngle)
          p.addAngularForce(angular.normalizeTo(maxAngle));
        else 
          p.addAngularForce(angular);
      }
      //else don't rotate
      
      //FIXME this is fishy--shouldn't we ease first then check?
      if(mag < stop)//same pixel
      {
        p.x = p.home.x;
        p.y = p.home.y;
        p.z = p.home.z;
        return;
      }
      
      if(mag > vars.maxForce)
        f.normalizeTo(vars.maxForce);
      f = f.scale(vars.userA); //easing
      setForce(f);
      p.addForce(vars.scaledForce);
      //Debug
      boolean __debug = false;
      if(__debug && p.home.x == 0 && p.home.y == 0)
      {
        System.out.println("Point");
        System.out.println("\tlocation: " + p.toString());
        System.out.println("\thome    : " + p.home);
        System.out.println("\tforce   : " + f);
        System.out.println("\tdistance   : " + f.magnitude());
        System.out.println("\tangular   : " + p.angular.toString());
      }
      
    }
    else
      return;
    
    //Reset Vector
    super.apply(p3d);
  }

  @Override
  public void configure(float timeStep)
  {
    super.configure(timeStep * timeStep);
  }
}
