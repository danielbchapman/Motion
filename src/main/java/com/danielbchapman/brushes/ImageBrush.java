package com.danielbchapman.brushes;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.danielbchapman.physics.toxiclibs.Actions;
import com.danielbchapman.physics.toxiclibs.MotionInteractiveBehavior;
import com.danielbchapman.physics.toxiclibs.PersistentVariables;

import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;
import toxi.geom.Vec3D;


//FIXME DOCUMENT THIS IN WHAT FIELDS ARE USED
public class ImageBrush extends VectorBrush
{
  public boolean isFadingBrush = true;
  public boolean isVariableBrush = true;
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
    m.put(PersistentVariables.Fields.USER_C, "Image");
    
    FIELD_NAMES = Collections.unmodifiableMap(m);
  }
  
  public ImageBrush()
  {
    vars.userAMax = 100;
    vars.userAMin = 100;
  }
  //Cache
  PImage loadedImage;
  
  @Override
  public String getName()
  {
    return "Image Brush";
  }

  @Override
  public MotionInteractiveBehavior copy()
  {
    ImageBrush copy = new ImageBrush();
    copy.vars = this.vars.clone();
    return copy;
  }

  @Override
  public void applyBrush(PGraphics g, Vec3D p, int opacity, float sizeMod)
  {
    if(loadedImage == null)
    {
      loadedImage = Actions.engine.loadImage("core/brushes/images/soft-messy.png");
    }
    
    g.pushMatrix();
    g.translate(p.x, p.y);//p.z);
    g.tint(255, 128); //use opacity in the future
    g.imageMode(PConstants.CENTER);
    g.tint(255, opacity);
    g.image(loadedImage, 0, 0, vars.userAMax * sizeMod, vars.userAMin * sizeMod);
    g.imageMode(PConstants.CORNER);
    g.popMatrix();
  }

  @Override
  public boolean isFadingBrush()
  {
    return true;
  }

  @Override
  public boolean isVariableBrush()
  {
    return true;
  }
}
