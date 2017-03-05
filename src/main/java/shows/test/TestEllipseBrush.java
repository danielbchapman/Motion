package shows.test;

import processing.core.PConstants;
import processing.core.PGraphics;
import toxi.geom.Vec3D;

import com.danielbchapman.motion.core.MotionBrush;
import com.danielbchapman.motion.core.MotionMouseEvent;

public class TestEllipseBrush extends MotionBrush
{
  private boolean vectorBrush = false;
  
  public TestEllipseBrush(boolean vect)
  {
    this.vectorBrush = vect;
  }
  
  @Override
  public void applyBrush(PGraphics g, MotionMouseEvent point)
  {
    if(g.is3D())
      g.pushMatrix();

    g.color(255);
    g.stroke(255,255,0);
    g.ellipseMode(PConstants.CENTER);
    g.ellipse(point.x, point.y, 10, 10);
    
    if(g.is3D())
      g.popMatrix(); 
  }

  @Override
  public void update(long time)
  { 
  }
  
  @Override
  public boolean isVectorBrush()
  {
    return vectorBrush;
  }

  @Override
  public MotionBrush deepCopy()
  {
    TestEllipseBrush tmp = new TestEllipseBrush(vectorBrush);
    return tmp;
  }
}
