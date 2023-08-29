package com.danielbchapman.physics.toxiclibs;

import processing.core.PGraphics;

public abstract class Shape
{
  PointOLD[] points;
  
  /**
   * The render method takes the array of points (configured however you want) and
   * draws it to the graphics object. This is completely free-form and can work in any 
   * way you see fit. This method should not try to update forces for the object at hand,
   * the physics engine will apply all forces to this object. 
   * 
   * @param graphics the PGraphics instance to render to. Assume that you must set all
   * graphical objects.
   * 
   */
  public abstract void render(PGraphics graphics);
}
