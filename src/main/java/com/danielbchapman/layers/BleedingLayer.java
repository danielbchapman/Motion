package com.danielbchapman.layers;

import com.danielbchapman.brushes.SaveableBrush;
import com.danielbchapman.physics.toxiclibs.Actions;
import com.danielbchapman.physics.toxiclibs.Layer;

import lombok.Getter;
import lombok.Setter;
import processing.core.PConstants;
import processing.core.PGraphics;

public abstract class BleedingLayer extends Layer
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
    first = false;
  }
  public abstract void reanderAfterBleed(PGraphics g);
  
  @Override
  public void render(PGraphics g)
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
      g.rect(0, 0, Actions.WIDTH, Actions.HEIGHT);
    }
    
    reanderAfterBleed(g);
  }
  
  @Override
  public void renderBrush(SaveableBrush brush, PGraphics g, int currentFrame)
  {
    brush.draw(g);
  }
}
