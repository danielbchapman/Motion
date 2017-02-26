package com.danielbchapman.motion.core;

import java.util.HashMap;
import java.util.function.Consumer;

import processing.core.PGraphics;


/**
 * A copy of the Scene class that implements all abstract methods
 * to cut down on verbosity.
 */
public abstract class BaseScene extends Scene
{
  @Override
  public void applyBrushBeforeDraw(MotionBrush brush, PGraphics g)
  { 
  }

  @Override
  public void applyBrushAfterDraw(MotionBrush brush, PGraphics g)
  { 
  }

  @Override
  public void afterBrushes(PGraphics g)
  { 
  }

  @Override
  public String getName()
  {
    return getClass().getSimpleName();
  }

  @Override
  public void initialize(Motion motion)
  {
  }

  @Override
  public HashMap<KeyCombo, Consumer<Motion>> getKeyMap()
  {
    return null;
  }

  @Override
  public void update(long time)
  {
  }

  @Override
  public void draw(PGraphics g)
  {
  }

  @Override
  public void shutdown()
  {
  }

  @Override
  public boolean isPersistent()
  {
    return false;
  }
}
