package shows.troubledwater;

import java.util.ArrayList;

import processing.core.PGraphics;
import toxi.geom.Vec3D;

import com.danielbchapman.brushes.ImageBrush;
import com.danielbchapman.layers.BleedingCanvasLayer;
import com.danielbchapman.physics.toxiclibs.Actions;
import com.danielbchapman.physics.toxiclibs.Emitter;
import com.danielbchapman.physics.toxiclibs.Layer;
import com.danielbchapman.physics.toxiclibs.MotionEngine;
import com.danielbchapman.physics.toxiclibs.Point;

public class RainLayer extends BleedingCanvasLayer
{
  public RainLayer(MotionEngine engine)
  {
    super(engine);
  }

  boolean first = true;
  RainEmitter rain;

  ArrayList<BrushPoint> list = new ArrayList<>();
  
  @Override
  public Point[] init()
  {
    rain = new RainEmitter(this, new Vec3D(200, 200, 0), Vec3D.randomVector(), 15000, 1000, 2f, 1000);
    return new Point[0];
  }

  @Override
  public void render(PGraphics g)
  {
    if (first)
    { 
      first = false;
    }
    g.fill(0,0,255, 2);
    g.stroke(0,0,255, 2);
    g.rect(0, 0, this.engine.getWidth(), this.engine.getHeight());
    rain.draw(g);      
  }

  @Override
  public void update()
  {
    long sysTime = System.currentTimeMillis();
    rain.update(sysTime);

  }

  @Override
  public void go(MotionEngine engine)
  {
    // engine.addBehavior(Actions.gravity);
    engine.getPhysics().setDrag(0.5f);
    Actions.gravity.setGravity(new Vec3D(.45f, .85f, 0));

    try
    {
      engine.getPhysics().addBehavior(Actions.gravity);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}
