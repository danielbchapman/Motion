package com.danielbchapman.physics.toxiclibs;

import lombok.Data;
import toxi.geom.Vec3D;
import toxi.physics3d.VerletParticle3D;

@Data
public class Point extends VerletParticle3D
{
  Vec3D home;
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
  
}
