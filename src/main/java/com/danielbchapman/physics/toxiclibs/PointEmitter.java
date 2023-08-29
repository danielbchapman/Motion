package com.danielbchapman.physics.toxiclibs;

import processing.core.PGraphics;
import toxi.geom.Vec3D;

public class PointEmitter extends OLDEmitter<PointOLD>
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
    for(PointOLD p : children)
     g.point(p.x,  p.y, p.z); 
    g.popMatrix();
  }

  @Override
  public PointOLD createPoint(float x, float y, float z, float w)
  {
    return new PointOLD(x,y,z,w);
  }
}
