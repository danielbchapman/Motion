package shows.ululations;

import com.danielbchapman.motion.core.PointWrapper;

public class BrushPoint extends PointWrapper<RainBrush>
{
  public BrushPoint(float x, float y, float z, float w)
  {
    super(x, y, z , w);
    RainBrush wrap = new RainBrush();
    this.setWrap(wrap);
    
  }
}
