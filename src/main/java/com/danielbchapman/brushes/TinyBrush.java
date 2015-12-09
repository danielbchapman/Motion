package com.danielbchapman.brushes;

import com.danielbchapman.physics.toxiclibs.MotionInteractiveBehavior;

public class TinyBrush extends ImageBrush
{
  public TinyBrush()
  {
    super.brushFileName = "core/brushes/images/round.png";
    super.fadeTime = 5000;
    super.opacityStart = 196;
    super.opacityEnd =64;
    super.sizeStart = .1f;
    super.sizeEnd = 0.05f;
  }
  
  @Override
  public MotionInteractiveBehavior copy()
  {
    TinyBrush copy = new TinyBrush();
    copy.vars = this.vars.clone();
    return copy;
  }
}
