package com.danielbchapman.brushes;

import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;
import toxi.geom.Vec3D;

import com.danielbchapman.motion.core.Motion;
import com.danielbchapman.motion.core.VectorMouseBrush;
import com.danielbchapman.physics.toxiclibs.ActionsOLD;
import com.danielbchapman.physics.toxiclibs.MotionInteractiveBehavior;


//FIXME DOCUMENT THIS IN WHAT FIELDS ARE USED and make this editable
public class ImageBrush extends VectorBrushPORT
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
  public void applyBrush(PGraphics g, Vec3D p, int opacity, float sizeMod)
  {
	
    if(loadedImage == null)
    {
      loadedImage = Motion.MOTION.loadImage(brushFileName);
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
  public boolean isVariableSizeBrush()
  {
    return true;
  }

  @Override
  public VectorBrushPORT copyBrushVariables() {
	 ImageBrush copy = new ImageBrush();
	 copy.brushFileName = brushFileName;
	 copy.isFadingBrush = isFadingBrush;
	 copy.isVariableBrush = isVariableBrush;
	 
	 return copy;
  }
}
