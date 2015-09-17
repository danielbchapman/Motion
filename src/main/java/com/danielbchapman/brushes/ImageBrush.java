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


//FIXME DOCUMENT THIS IN WHAT FIELDS ARE USED and make this editable
public class ImageBrush extends VectorBrush
{
  public boolean isFadingBrush = true;
  public boolean isVariableBrush = true;
  
  public String brushFileName = "core/brushes/images/soft-messy.png";
  
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
      loadedImage = Actions.engine.loadImage(brushFileName);
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
