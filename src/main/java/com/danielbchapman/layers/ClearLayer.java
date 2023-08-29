package com.danielbchapman.layers;

import com.danielbchapman.motion.core.BaseScene;
import com.danielbchapman.motion.core.Motion;
import com.danielbchapman.physics.toxiclibs.Layer;
import com.danielbchapman.physics.toxiclibs.MotionEngine;
import com.danielbchapman.physics.toxiclibs.PointOLD;

import processing.core.PGraphics;

public class ClearLayer extends BaseScene
{
  boolean clear = false;

  @Override
  public void draw(PGraphics g)
  {
    if(!clear)
    {
      g.clear();
      g.background(0);
      clear = true;
    }
  }
  
  public void go(Motion engine)
  {
    System.out.println("Clear Called");
    clear = false;
  }

}
