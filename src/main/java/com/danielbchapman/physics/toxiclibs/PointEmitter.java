package com.danielbchapman.physics.toxiclibs;

import processing.core.PGraphics;
import toxi.geom.Vec3D;

public class PointEmitter extends Emitter<Point>
{
  /**
   * @param position
   * @param heading
   * @param lifeSpan
   */
  public PointEmitter(Vec3D position, Vec3D heading, int lifeSpan)
  {
    super(position, heading, lifeSpan, 250, 0, 0);
  }

  @Override
  public void draw(PGraphics g)
  {
    g.pushMatrix();
    for(Point p : children)
     g.point(p.x,  p.y, p.z); 
    g.popMatrix();
  }

  @Override
  public Point createPoint(float x, float y, float z, float w)
  {
    return new Point(x,y,z,w);
  }
}
