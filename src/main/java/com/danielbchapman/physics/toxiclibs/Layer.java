package com.danielbchapman.physics.toxiclibs;

import com.danielbchapman.brushes.SaveableBrush;

import processing.core.PApplet;
import processing.core.PGraphics;


/**
 * The scene class renders a series of objects to the graphics context and is responsible
 * for all their updates from the user interface. This allows the user to define events as 
 * needed and can work as a series of cues/applied forces etc...
 */

public abstract class Layer
{
  protected PointOLD[] points;
  protected PApplet applet;
  public MotionEngine engine;
  protected SaveableBrush brush;
  /**
   * @param points the objects to draw
   */
  @SuppressWarnings("This is a terrible design and needs to be eliminated immediately. This means constructors can't have variables.")
  /**
   * Return the name for this layer (unique) to be controlled by OSC
   * @return by default this returns the class name.
   */
  public String getName()
  {
    return this.getClass().getName();
  }
  
  public abstract PointOLD[] init();
  
  /**
   * This is the first call to render a layer. It is called
   * before the brushes are called to render.
   * 
   * @param g  The PGraphics instance
   * 
   */
  public abstract void render(PGraphics g);
  
  /**
   * Actions to take after rendering the brushes {@link #renderBrush(SaveableBrush, PGraphics)}. 
   * As most layers will not need this it is not mandatory
   * @param graphics 
   */
  public void renderAfterBrushes(PGraphics graphics)
  {
  }
  
  /**
   * Called before each draw method  
   */
  public abstract void update();
  
  /**
   * If this layer is intended to be painted on then
   * accept this brush and paint with it using whatever
   * rules are applicable. Otherwise ignore it.
   * 
   * This is called after the render method so it can be applied 
   * in the drawing loop. An inactive brush will be passed in as null.
   * The method {@link #renderAfterBrushes(PGraphics)} is called after 
   * this method.
   * 
   * @param brush the brush to draw
   */
  public void renderBrush(SaveableBrush brush, PGraphics g, int currentFrame)
  {
  }
  /**
   * Fire a cue if needed.
   * @param engine the engine to use.
   */
  public abstract void go(MotionEngine engine);
}
