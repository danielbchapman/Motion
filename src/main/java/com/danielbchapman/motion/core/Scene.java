package com.danielbchapman.motion.core;

import java.util.HashMap;
import java.util.function.Consumer;

import processing.core.PGraphics;
import toxi.geom.Vec3D;
import toxi.physics3d.VerletPhysics3D;

public abstract class Scene
{
  boolean initialized = false;
  public abstract boolean isVerletPhysic3D();
  /**
   * Determines whether this scene should apply brushes before 
   * or after the draw method. Scenes that rely on physics may 
   * wish to apply this information before the visuals. 
   * 
   * This defaults to true. Only one choice is available per scene.
   */
  public abstract boolean applyBrushesAfterDraw();
  public abstract void applyBrush(MotionBrush brush, PGraphics g, MotionMouseEvent point);
  public abstract void afterBrushes(PGraphics g);
 
  public abstract VerletPhysics3D get3DPhysics();
  /**
   * Override this method if you need a 2D context for some reason.
   */
  public boolean is2D()
  {
    return false;
  }
  
  public abstract void go(Motion motion);
  public void go(Motion motion, Integer cue)
  {
    System.out.println("GO TO FIRED");
  }
  
  /**
   * @return true if this scene should maintain its state if navigated away from
   * and false if it should reset. (You probably want false).  
   */
  public abstract boolean isPersistent();
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
   * 
   * Return null if not used
   */
  public abstract HashMap<KeyCombo, Consumer<Motion>> getKeyMap();
 
  /**
   * Returns a map of the property types that should be available to edit
   * in the scene tools editor. If null, then no types are assumed.
   */
  public abstract HashMap<String, Class<?>> getPropertyTypes();
  
  /**
   * Update is called before the draw method and is passed a time 
   * if needed by physics engines. Brushes will be updated independently.
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
   * Draw to the debug layer if you need to.
   * @param g the graphics context to draw on
   * 
   */
  public void debug(PGraphics g){};
  
  /**
   * Draw to the overlay layer if needed (help, data etc...)
   * @param clear The clear layer is cleared before each frame call (useful 
   * for text or data).  
   */
  public void overlay(PGraphics clear){};
  
  /**
   * Draw to a persistent overlay context
   * @param overlayPaths The persistent layer does not clear itself and is 
   * useful for capturing events over time.
   */
  public void overlayPaths(PGraphics overlayPaths){};
  /**
   * A cleanup method for shutting down any system specific
   * resources. It is only called on reset.  
   */
  public abstract void shutdown();
}
