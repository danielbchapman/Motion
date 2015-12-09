package com.danielbchapman.brushes;

import com.danielbchapman.physics.toxiclibs.MotionInteractiveBehavior;

public class SmallBrush extends ImageBrush
{
  public SmallBrush()
  {
    super.brushFileName = "core/brushes/images/soft-messy.png";
    super.fadeTime = 5000;
    super.opacityStart = 128;
    super.opacityEnd =64;
    super.sizeStart = .3f;
    super.sizeEnd = 0.15f;
  }
  
  @Override
  public MotionInteractiveBehavior copy()
  {
    SmallBrush copy = new SmallBrush();
    copy.vars = this.vars.clone();
    return copy;
  }
}
