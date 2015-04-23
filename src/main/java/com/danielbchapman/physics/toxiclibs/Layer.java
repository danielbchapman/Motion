package com.danielbchapman.physics.toxiclibs;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.event.KeyEvent;


/**
 * The scene class renders a series of objects to the graphics context and is responsible
 * for all their updates from the user interface. This allows the user to define events as 
 * needed and can work as a series of cues/applied forces etc...
 */

public abstract class Layer
{
  protected Point[] points;
  protected PApplet applet;
  protected MotionEngine engine;
  /**
   * @param points the objects to draw
   */
  public Layer()
  {
    points = init();
  }
  
  public abstract Point[] init();
  
  public abstract void render(PGraphics graphics);
  
  /**
   * Called before each draw method  
   */
  public abstract void update();
  
  /**
   * Fire a cue if needed.
   * @param engine the engine to use.
   */
  public abstract void go(MotionEngine engine);
}
