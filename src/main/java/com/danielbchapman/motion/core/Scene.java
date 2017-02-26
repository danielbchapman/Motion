package com.danielbchapman.motion.core;

import java.util.HashMap;

import processing.core.PGraphics;

abstract class Scene
{
  public abstract void applyBrushBeforeDraw(MotionBrush brush, PGraphics g);
  public abstract void applyBrushAfterDraw(MotionBrush brush, PGraphics g);
  public abstract void afterBrushes(PGraphics g);
  /**
   * @return true if this is a 2D context, else return false if 3D.
   */
  public abstract boolean is2D();
  /**
   * @return The unique name for this layer. It is used by OSC for targeting  
   */
  public abstract String getName();
  /**
   * The constructor for this class. 
   * Use a default constructor and do all initialization in this method. It 
   * will allow for scenes to be reset.
   * @param motion the engine this class can be initialized with  
   */
  public abstract void initialize(Motion motion);
  
  /**
   * Returns a custom key map to be used by the engine. The
   * Engine will call the get method for each request if you 
   * need to modify hot-keys on the fly. This puts ultimate control
   * in the scene, you should have a method to advance to the next scene.  
   */
  public abstract HashMap<KeyCombo, MotionEvent> getKeyMap();
  
  
  /**
   * Update is called before the draw method and is passed a time 
   * if needed by physics engines.
   */
  public abstract void update(long time);
  
  
  /**
   * The draw method with the context that needs to be targeted passed. 
   * This does not prevent the scene from using offscreen contexts or other
   * contexts.
   * 
   * If you request a 2D context a 2D context will be passed.
   * @param g PGraphics Context  
   * 
   */
  public abstract void draw(PGraphics g);
  
  /**
   * A cleanup method for shutting down any system specific
   * resources. It is only called on reset.  
   */
  public abstract void shutdown();
}
