package com.danielbchapman.physics.toxiclibs;

import lombok.Data;

@Data
public class Point extends Vec3
{
  Vec3 home;
  float mass = 1.0f;
  
  public Point()
  {
    this(new Vec3(0,0,0), 1f);
  }
  public Point(Vec3 location, float mass)
  {
    this(location, location, mass);
  }
  
  public Point(Vec3 location, Vec3 home, float mass)
  {
    x = location.x;
    y = location.y;
    z = location.z;
    this.mass = mass;
    this.home = new Vec3(home);//clone to home
  }
  
}
