package com.danielbchapman.physics.toxiclibs;

import processing.core.PApplet;
import processing.core.PGraphics;

/**
 * A Wrapper class for Syphon/Spout
 */
public interface IGraphicsShareClient
{
  public void initialize(PApplet app, String name, String serverName, int width, int height);
  public void cleanup();
  public void read(PGraphics g);
  
}
