package shows.troubledwater;

import processing.core.PGraphics;

import com.danielbchapman.physics.toxiclibs.MotionEngine;

public class RestLayer extends PaintingLayer
{

  public RestLayer(MotionEngine engine)
  {
    super(engine);
  }

  @Override
  public String getSpecificName()
  {
    return "Rest";
  }

  int i = 0;
  public void draw(PGraphics g)
  {
    if(i % 30 == 0)
    {
      g.background(0);
      i = 0;
    }
    i++;      
  }
}
