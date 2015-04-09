package com.danielbchapman.artwork;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import processing.core.PGraphics;

public class Fadeable
{
  public long start = 0L;
  public long count;
  public int opacity;
  public int opacityStart;
  public int opacityEnd;
  private int opacityDelta;
  public long delay;
  public boolean running;
  public boolean finished;
  public static final int MAX = 255;

  public Fadeable(int opacityStart, int opacityEnd, long count, long delay)
  {
    this.opacity = opacityStart;
    this.opacityStart = opacityStart;
    this.opacityEnd = opacityEnd;
    this.opacityDelta = opacityEnd - opacityStart;
    this.count = count;
    this.delay = delay;
  }
  
  public int color(long time, int color)
  {
    if(finished)
      return color;
    running = true;
    if(start == 0L){
      start = time;
    }
    
    long delta = time - start;
    if(delta < 0){
      opacity = opacityStart;
      running = false;
    } else if(delta > count){
      finished = true;
      return color;
    } else {
      float amount = delta / count;
      opacity = (int) (opacityStart + (amount * opacityDelta));
    }
    return ( opacity << 24) | (0x00FFFFFF &  color);
  }
}
