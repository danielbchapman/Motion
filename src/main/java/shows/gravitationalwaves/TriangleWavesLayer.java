package shows.gravitationalwaves;

import com.danielbchapman.physics.toxiclibs.Actions;
import com.danielbchapman.physics.toxiclibs.Layer;
import com.danielbchapman.physics.toxiclibs.MotionEngine;
import com.danielbchapman.physics.toxiclibs.Point;

import processing.core.PGraphics;
import toxi.geom.Vec3D;

public class TriangleWavesLayer extends Layer
{
  TriangleWaveEmitter emitter = 
      new TriangleWaveEmitter(new Vec3D(720, 360, 0))
      {

        @Override
        public TriangleWave onEmit(long time)
        {
          TriangleWaveCrossing wave = new TriangleWaveCrossing(0f, 0f, 0f,(float) Math.PI / 3 * 2); //60
          return wave;          
        }
    
      };
  TriangleWaveEmitter emitterCenter = 
      new TriangleWaveEmitter(new Vec3D(400, 400, 0))
      {
        {
          setRandomTime(500);
          setTimeStep(50);
        }
        @Override
        public TriangleWave onEmit(long time)
        {
          TriangleWave wave = new TriangleWave(0f, 0f, 0f,(float) Math.PI / 3 * 2); //60
          wave.weight = Math.abs(time % 1000 - 500) / 150f;
          wave.color = (int) (time % 255);
          return wave;          
        }
    
      };
  boolean initialized;
  @Override
  public Point[] init()
  {
    return new Point[]{};
  }

  @Override
  public void render(PGraphics g)
  {
    g.background(0, 64, 0);
    g.fill(255);
    g.text("TRIANGLE WAVES", 25, 25, 10);
    if(!initialized)
    {
      Actions.engine.getPhysics().setDrag(0f);
      initialized = true;
    }
    emitter.draw(g);
    emitterCenter.draw(g);
  }

  @Override
  public void update()
  {
    emitter.update(System.currentTimeMillis());
    emitterCenter.update(System.currentTimeMillis());
  }

  @Override
  public void go(MotionEngine engine)
  {
    
  }
  
  @Override
  public String getName()
  {
    return "triangle-waves";
  }

}
