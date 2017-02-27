package com.danielbchapman.motion.core;

import processing.core.PApplet;

import com.danielbchapman.physics.toxiclibs.Util;

public class Main
{
  public static void main(String ... args)
  {
    Motion.WIDTH = 800;
    Motion.HEIGHT = 600;
    try
    {
      Motion.PROPERTIES = Util.readProps("motion.ini", Motion.PROPERTIES);
    }
    catch(Throwable t)
    {
      System.err.println("Error reading INI file");
      t.printStackTrace();
    }
    Motion motion = new Motion();
    PApplet.runSketch(new String[]{"MAIN"}, motion);
  }
}
