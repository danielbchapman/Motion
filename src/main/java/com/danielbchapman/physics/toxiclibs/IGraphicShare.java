package com.danielbchapman.physics.toxiclibs;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

/**
 * A simple interface that could be used for Syphon or Spout or something
 * similar. It offers an abstraction so it can be loaded natively.
 * 
 * @author danielbchapman
 *
 */
public interface IGraphicShare {
	public void initialize(PApplet app);
	public void cleanup();
	public void send(PGraphics g);
}
