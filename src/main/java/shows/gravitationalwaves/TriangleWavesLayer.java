package shows.gravitationalwaves;

import com.danielbchapman.layers.BleedingLayer;
import com.danielbchapman.physics.toxiclibs.AbstractEmitter;
import com.danielbchapman.physics.toxiclibs.Actions;
import com.danielbchapman.physics.toxiclibs.Cue;
import com.danielbchapman.physics.toxiclibs.MotionEngine;
import com.danielbchapman.physics.toxiclibs.Point;

import processing.core.PGraphics;
import toxi.geom.Vec3D;

public class TriangleWavesLayer extends BleedingLayer
{
  public TriangleWavesLayer()
  {
    setFadeAmount(16);
  }

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
      
  AbstractEmitter<SplineWave> spline = 
      new AbstractEmitter<SplineWave>(new Vec3D(600, 600, 0))
      {

        @Override
        public SplineWave onEmit(long time)
        {
          return new SplineWave(0f, 0f, 0f);
        }

        @Override
        public void draw(PGraphics g)
        {
          g.strokeWeight(3f);
          g.stroke(255, 0, 0);
          for(SplineWave s : children)
            s.draw(g);
        }
    
      };
  boolean initialized;
  @Override
  public Point[] init()
  {
    return new Point[]{};
  }

  @Override
  public void reanderAfterBleed(PGraphics g)
  {
    g.text("TRIANGLE WAVES", 25, 25, 10);
    if(!initialized)
    {
      Actions.engine.getPhysics().setDrag(0f);
      initialized = true;
    }
    emitter.draw(g);
    emitterCenter.draw(g);
    spline.draw(g);
  }

  @Override
  public void update()
  {
    long time = System.currentTimeMillis();
    emitter.update(time);
    emitterCenter.update(time);
    spline.update(time);
  }

  @Override
  public void go(MotionEngine engine)
  {
    try
    {
      Cue q = Cue.create("reset",        
          Actions.gravityOff,
          Actions.homeOff,
          Actions.homeLinearOff);
      
      clear();
      q.go(this,  engine);
    }
    catch (Exception e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  @Override
  public String getName()
  {
    return "triangle-waves";
  }

}
