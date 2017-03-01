package com.danielbchapman.motion.tools;

import java.util.HashMap;
import java.util.function.Consumer;

import processing.core.PGraphics;

import com.danielbchapman.motion.core.BaseScene;
import com.danielbchapman.motion.core.KeyCombo;
import com.danielbchapman.motion.core.Motion;
import com.danielbchapman.motion.core.MotionBrush;
import com.danielbchapman.motion.core.MotionMouseEvent;
import com.danielbchapman.motion.core.Scene;


/**
 * A simple resting layer so that the system will not consume much in the way
 * of resources. It does not do anything other than wait for the next command.
 */
public final class RestScene extends BaseScene
{
  @Override
  public String getName()
  {
    return "rest";
  }

  @Override
  public void afterBrushes(PGraphics g)
  { 
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

  @Override
  public boolean applyBrushesAfterDraw()
  { 
    return true;
  }

  @Override
  public void applyBrush(MotionBrush brush, PGraphics g, MotionMouseEvent point)
  {
  }

  @Override
  public void go(Motion motion)
  { 
  }
}