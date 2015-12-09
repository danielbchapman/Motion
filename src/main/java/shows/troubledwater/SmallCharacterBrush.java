package shows.troubledwater;

import com.danielbchapman.brushes.ImageBrush;
import com.danielbchapman.physics.toxiclibs.MotionInteractiveBehavior;

public class SmallCharacterBrush extends ImageBrush
{
  public SmallCharacterBrush()
  {
    super.brushFileName = "core/brushes/images/round.png";
    super.fadeTime = 5000;
    super.opacityStart = 255;
    super.opacityEnd = 128;
    super.sizeStart = .1f;
    super.sizeEnd = 0.02f;
  }
  
  @Override
  public MotionInteractiveBehavior copy()
  {
    SmallCharacterBrush copy = new SmallCharacterBrush();
    copy.vars = this.vars.clone();
    return copy;
  }
}
