package com.shekillsmonsters;

import com.danielbchapman.brushes.ImageBrush;
import com.danielbchapman.physics.toxiclibs.MotionInteractiveBehavior;

public class CharacterNameBrush extends ImageBrush
{
  public CharacterNameBrush()
  {
    super.brushFileName = "core/brushes/images/round.png";
    super.fadeTime = 5000;
    super.opacityStart = 128;
    super.opacityEnd =64;
    super.sizeStart = .3f;
    super.sizeEnd = 0.2f;
  }
  
  @Override
  public MotionInteractiveBehavior copy()
  {
    CharacterNameBrush copy = new CharacterNameBrush();
    copy.vars = this.vars.clone();
    return copy;
  }
}
