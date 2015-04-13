package com.danielbchapman.physics.toxiclibs;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.event.KeyEvent;


/**
 * The scene class renders a series of objects to the graphics context and is responsible
 * for all their updates from the user interface. This allows the user to define events as 
 * needed and can work as a series of cues/applied forces etc...
 *
 ***************************************************************************
 * @author Daniel B. Chapman 
 * <br /><i><b>Light Assistant</b></i> copyright Daniel B. Chapman
 * @since Mar 22, 2015
 * @version Development
 * @link http://www.lightassistant.com
 ***************************************************************************
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
}
