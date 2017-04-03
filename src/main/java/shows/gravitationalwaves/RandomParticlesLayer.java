package shows.gravitationalwaves;

import java.util.ArrayList;
import java.util.Map;

import processing.core.PConstants;
import processing.core.PGraphics;
import toxi.geom.Vec3D;
import toxi.physics3d.VerletParticle3D;

import com.danielbchapman.layers.BleedingLayer;
import com.danielbchapman.physics.toxiclibs.OLDAbstractEmitter;
import com.danielbchapman.physics.toxiclibs.Actions;
import com.danielbchapman.physics.toxiclibs.Cue;
import com.danielbchapman.physics.toxiclibs.OLDEmitter;
import com.danielbchapman.physics.toxiclibs.ExplodeBehaviorInverse;
import com.danielbchapman.physics.toxiclibs.MotionEngine;
import com.danielbchapman.physics.toxiclibs.MotionInteractiveBehavior;
import com.danielbchapman.physics.toxiclibs.Point;

public class RandomParticlesLayer extends BleedingLayer
{
  ArrayList<OLDEmitter<Point>> emitters = new ArrayList<>();

  ArrayList<MotionInteractiveBehavior> behaviors = new ArrayList<>();

  public RandomParticlesLayer()
  {
    for (int i = 0; i < 4; i++)
    {
      ExplodeBehaviorInverse explode = new ExplodeBehaviorInverse(new Vec3D(-1f, 0, 0), -100f);
      explode.setPosition(new Vec3D(10, 200 * i, 0));
      explode.setMax(100f);
      explode.setMinimum(0);
      behaviors.add(explode);
    }

    for (int i = 0; i < 10; i++)
    {
      OLDEmitter<Point> e = new OLDEmitter<Point>(new Vec3D(1400, i * 100 - 100, 0), new Vec3D(-1, 0, 0), 25000, 50, 1f, 35)
      {
        @Override
        public Point createPoint(float x, float y, float z, float w)
        {
          return new Point(x, y, z, w);
        }

        @Override
        public void draw(PGraphics g)
        {
          for (Point p : children)
          {
            g.point(p.x, p.y, p.z);
          }
        }
      };

      emitters.add(e);
    }
  }

  @Override
  public void reanderAfterBleed(PGraphics g)
  {
    // g.background(0, 32);
    g.pushMatrix();
    g.stroke(255, 255, 255, 64);
    g.strokeWeight(3f);
    g.fill(255);
    for (OLDEmitter<?> e : emitters)
    {
      e.draw(g);
    }
    
    g.ellipseMode(PConstants.CENTER);
//    for(MotionInteractiveBehavior b : behaviors)
//      g.ellipse(b.vars.position.x, b.vars.position.y, 40, 40);
    g.popMatrix();
  }

  @Override
  public Point[] init()
  {
    return new Point[] {};
  }

  @Override
  public void update()
  {
    long time = System.currentTimeMillis();
    for (OLDEmitter<?> e : emitters)
      e.update(time);

//    for (MotionInteractiveBehavior b : behaviors)
//      Actions.engine.addBehavior(b);
  }

  @Override
  public void go(MotionEngine engine)
  {
    try
    {
      Cue q = Cue.create("reset",  
          Actions.dragToNone,
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
    return "random-particles-layer";
  }
}
