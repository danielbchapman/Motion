package shows.troubledwater;

import com.danielbchapman.physics.toxiclibs.PointWrapper;

public class BrushPoint extends PointWrapper<RainBrush>
{
  public BrushPoint(float x, float y, float z, float w)
  {
    super(x, y, z , w);
    RainBrush wrap = new RainBrush();
    this.setWrap(wrap);
    
  }
}
