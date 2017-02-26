package com.danielbchapman.motion.core;

import java.util.HashMap;

import processing.core.PGraphics;
import processing.opengl.PGraphics2D;


/**
 * A simple resting layer so that the system will not consume much in the way
 * of resources. It does not do anything other than wait for the next command.
 */
public final class RestScene extends Scene
{
  @Override
  public String getName()
  {
    return "rest";
  }

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
  public boolean is2D()
  { 
    return true;
  }

  @Override
  public void initialize(Motion motion)
  { 
  }

  @Override
  public HashMap<KeyCombo, MotionEvent> getKeyMap()
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
}
