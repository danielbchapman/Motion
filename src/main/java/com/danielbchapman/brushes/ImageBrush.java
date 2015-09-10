package com.danielbchapman.brushes;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.danielbchapman.physics.toxiclibs.MotionInteractiveBehavior;
import com.danielbchapman.physics.toxiclibs.PersistentVariables;

import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;

public class ImageBrush extends SaveableBrush
{
  static final Map<String, String> FIELD_NAMES;
  static {
    HashMap<String, String> m = new HashMap<>();
    m.put(PersistentVariables.Fields.MAGNITUDE, "Magniture");
    m.put(PersistentVariables.Fields.MAX_FORCE, "Max Force");
    m.put(PersistentVariables.Fields.MIN_FORCE, "Min Force");
    m.put(PersistentVariables.Fields.POS_X, "Position");
    m.put(PersistentVariables.Fields.POS_Y, "Position");
    m.put(PersistentVariables.Fields.POS_Z, "Position");
    m.put(PersistentVariables.Fields.FOR_X, "Force");
    m.put(PersistentVariables.Fields.FOR_Y, "Force");
    m.put(PersistentVariables.Fields.FOR_Z, "Force");
    
    FIELD_NAMES = Collections.unmodifiableMap(m);
  }
  
  //Cache
  PImage loadedImage;
  
  @Override
  public String getName()
  {
    return "Image Brush";
  }

  @Override
  public void draw(PGraphics g)
  {
    g.pushMatrix();
    g.stroke(255);
    g.fill(255);
    g.translate(vars.position.x, vars.position.y);
    g.ellipseMode(PConstants.CENTER);
    g.ellipse(0, 0, 10, 10);
    g.popMatrix();
    
    lastPosition = this.vars.position;
  }

  @Override
  public MotionInteractiveBehavior copy()
  {
    ImageBrush copy = new ImageBrush();
    copy.vars = this.vars.clone();
    return copy;
  }
}
