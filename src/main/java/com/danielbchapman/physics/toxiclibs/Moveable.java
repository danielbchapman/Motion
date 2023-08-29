package com.danielbchapman.physics.toxiclibs;

public interface Moveable
{
  /**
   * Return a pointer to the array of points
   * created by this Moveable instance.
   * 
   * It is critical that this array of points return as a group
   * as the moveable instance might be destroyed by an emitter
   * if the lifespan is expired.
   * @return Point[] representing this shape.
   * 
   */
  public <T extends PointOLD> PointOLD[] getPoints();
}
