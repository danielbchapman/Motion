package com.danielbchapman.brushes;

import processing.core.PGraphics;
import toxi.geom.Vec3D;



/**
 * The IBrush is an interface that provides common functionality for 
 * brushes used in the Motion engine. These brushes may not
 * be interactive behaviors like the force brushes but instead
 * are uses to draw directly to the screen.
 * 
 * Often these will share similar variables but the IBrush interface
 * is interested in making sure that vectors can be connected and that
 * "paint" can be spread to the canvas.
 *
 */
public interface IBrush
{
  /**
   * Draw this brush to the current canvas. Once the draw is complete
   * the currentPosition should be set to the last position.
   * 
   *
   * @param g the image to draw
   * 
   */
  //FIXME Design Flaw This should be handled at a higher level by the motion engine, repeating code is a smell.
  public void draw(PGraphics g);
  
  /**
   * Returns the last position this brush
   * was drawn {@link #draw(PGraphics)} at.
   * @return The last position or null if this is the first draw
   */
  public Vec3D getLastPosition();
  
  /**
   * Returns the current position of this brush.
   */
  public Vec3D getCurrentPosition();
  
  /**
   * Called when this brush should dtop drawing to the 
   * canvas.
   * 
   */
  public void endDraw();
  
  /**
   * Called when this brush first begins to draw to the canvas
   * 
   */
  public void startDraw();
}
