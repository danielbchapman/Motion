package com.danielbchapman.physics.toxiclibs;

import toxi.geom.Vec3D;
import toxi.physics3d.VerletParticle3D;
import toxi.physics3d.behaviors.ConstantForceBehavior3D;

public class HomeBehavior3D extends ConstantForceBehavior3D
{
  
  protected float easing = 0.05f;
  protected float max = 0.5f;
  
  public HomeBehavior3D(Vec3D force)
  {
    super(force);
  }

  boolean enabled;
  float strength; 

  @Override
  public void apply(VerletParticle3D p3d)
  {
    if(!enabled)
      return;    
      
    if(p3d instanceof Point)
    {
      Point p = (Point) p3d;
      Vec3D f = p.home.sub(p);//.scale(0.05f);//easing 
      float mag = f.magnitude();
      float stop = 0.05f; //halt
      
      if(f.magnitude() < stop)//same pixel
      {
        p.x = p.home.x;
        p.y = p.home.y;
        p.z = p.home.z;
        return;
      }
      
      if(mag > max)
        f.normalizeTo(max);
      f = f.scale(easing); //easing
      setForce(f);
      p.addForce(scaledForce);
      //Debug
      if(p.home.x == 0 && p.home.y == 0)
      {
        System.out.println("Point");
        System.out.println("\tlocation: " + p.toString());
        System.out.println("\thome    : " + p.home);
        System.out.println("\tforce   : " + f);
        System.out.println("\tdistance   : " + f.magnitude());
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
