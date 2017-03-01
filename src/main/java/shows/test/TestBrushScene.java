package shows.test;

import processing.core.PGraphics;

import com.danielbchapman.motion.core.BaseScene;
import com.danielbchapman.motion.core.Motion;

public class TestBrushScene extends BaseScene
{
  boolean first = true;
  @Override
  public void draw(PGraphics g)
  { 
    if(first){
      g.stroke(255, 0, 0);
      g.fill(255, 0, 0);
      g.rect(0,0, g.width, g.height);
      first = false;
    }
  }

  @Override
  public void go(Motion motion)
  {
    first = true;
  }

}
