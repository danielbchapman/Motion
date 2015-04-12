package com.danielbchapman.artwork;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import processing.core.PGraphics;

public class Fadeable
{
  public long start = -1L;
  public long count;
  public int opacity;
  public int opacityStart;
  public int opacityEnd;
  private int opacityDelta;
  public long delay;
  public boolean running;
  public boolean finished;
  public static final int MAX = 255;
  public boolean debug = false;

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
    if(start < 0L){
      start = time;
    }
    
    long delta = time - start;
    if(delta <= 0 || (delta - delay) <= 0){
      opacity = opacityStart;
      running = false;
    } else if(delta > (count+delay)){
      finished = true;
      return color;
    } else {
      float amount = ((float)(delta-delay)) / ((float)count);
      opacity = (int) (opacityStart + (amount * opacityDelta));
    }
    if(debug)
    {
      System.out.println("[Fadeable]::Debug Information");
      System.out.println("Opacity -> " + opacity);
      System.out.println("Opacity Start -> " + opacityStart);
      System.out.println("Opacity End -> " + opacityEnd);
      System.out.println("Count -> " + count);
      System.out.println("Delay -> " + delay);
      System.out.println("Arguments: time:" + time + " color:" + Integer.toHexString(color));
      System.out.println("Opacity -> " + opacity);
    }
    return ( opacity << 24) | (0x00FFFFFF &  color);
  }
}
