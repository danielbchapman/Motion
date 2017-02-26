package com.danielbchapman.motion.core;

import processing.core.PApplet;

public class Main
{
  public static void main(String ... args)
  {
    Motion.WIDTH = 800;
    Motion.HEIGHT = 600;
    Motion motion = new Motion();
    PApplet.runSketch(new String[]{"MAIN"}, motion);
  }
}
