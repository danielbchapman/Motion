package com.danielbchapman.layers;

import com.danielbchapman.brushes.old.SaveableBrush;
import com.danielbchapman.motion.core.BaseScene;
import com.danielbchapman.motion.core.Motion;
import com.danielbchapman.physics.toxiclibs.ActionsOLD;
import com.danielbchapman.physics.toxiclibs.Layer;

import lombok.Getter;
import lombok.Setter;
import processing.core.PConstants;
import processing.core.PGraphics;

public abstract class BleedingLayer extends BaseScene
{
  @Getter
  @Setter
  public boolean bleeding = true;
  private boolean first = false;
  
  @Getter
  @Setter
  public int fadeAmount = 2;
  
  /**
   * Clears the paint from this layer.  
   */
  public void clear()
  {
    first = true;
  }
  
  public abstract void reanderAfterBleed(PGraphics g);
  
  @Override
  public void draw(PGraphics g)
  {
    if (first)
    { 
      g.background(0);
      first = false;
    }
    if(bleeding)
    {
      g.fill(0,0,0, fadeAmount);
      g.stroke(0,0,0, fadeAmount);
      g.rectMode(PConstants.CORNER);
      g.rect(0, 0, Motion.WIDTH, Motion.HEIGHT);
    }
    
    reanderAfterBleed(g);
  }
}
