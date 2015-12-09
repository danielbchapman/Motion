package shows.troubledwater;

import com.danielbchapman.layers.BleedingCanvasLayer;
import com.danielbchapman.physics.toxiclibs.MotionEngine;

public abstract class PaintingLayer extends BleedingCanvasLayer
{

  public PaintingLayer(MotionEngine engine)
  {
    super(engine);
  }

  @Override
  public String getName()
  {
    return getSpecificName();
  }
  
  public abstract String getSpecificName();
    
}
