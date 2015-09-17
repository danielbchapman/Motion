package shows.troubledwater;

import com.danielbchapman.brushes.ImageBrush;
import com.danielbchapman.physics.toxiclibs.MotionInteractiveBehavior;

public class CharacterBrush extends ImageBrush
{
  public CharacterBrush()
  {
    super.brushFileName = "core/brushes/images/round.png";
    super.fadeTime = 5000;
    super.opacityStart = 128;
    super.opacityEnd =64;
    super.sizeStart = .2f;
    super.sizeEnd = 0.05f;
  }
  
  @Override
  public MotionInteractiveBehavior copy()
  {
    CharacterBrush copy = new CharacterBrush();
    copy.vars = this.vars.clone();
    return copy;
  }
}
