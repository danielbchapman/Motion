package shows.gravitationalwaves;

import java.util.ArrayList;
import java.util.Map;

import processing.core.PGraphics;
import toxi.geom.Vec3D;
import toxi.physics3d.VerletParticle3D;

import com.danielbchapman.layers.BleedingLayer;
import com.danielbchapman.physics.toxiclibs.OLDAbstractEmitter;
import com.danielbchapman.physics.toxiclibs.ActionsOLD;
import com.danielbchapman.physics.toxiclibs.Cue;
import com.danielbchapman.physics.toxiclibs.OLDEmitter;
import com.danielbchapman.physics.toxiclibs.ExplodeBehaviorInverse;
import com.danielbchapman.physics.toxiclibs.MotionEngine;
import com.danielbchapman.physics.toxiclibs.MotionInteractiveBehavior;
import com.danielbchapman.physics.toxiclibs.PointOLD;

public class RandomParticleLinesLayer extends BleedingLayer
{
  ArrayList<OLDEmitter<PointOLD>> emitters = new ArrayList<>();
  
  ArrayList<MotionInteractiveBehavior> behaviors = new ArrayList<>();
  

  public RandomParticleLinesLayer()
  {
    behaviors.add(new ExplodeBehaviorInverse(new Vec3D(-1f, 0, 0), -50f));
    for(int i = 0; i < 14; i++)
    {
      OLDEmitter<PointOLD> e = 
          new OLDEmitter<PointOLD>(new Vec3D(i * 128, 400, 0), new Vec3D(-1, 0, 0), 25000, 200, 1f, 200)
          {
            @Override
            public PointOLD createPoint(float x, float y, float z, float w)
            {
              return new PointOLD(x, y, z, w);
            }
    
            @Override
            public void draw(PGraphics g)
            {
              for(int i = 0; i < children.size() - 1; i++)
              {
                PointOLD a = children.get(i);
                PointOLD b = children.get(i+1);
                
                g.line(a.x, a.y, a.z, b.x, b.y, b.z);
              }
              for(PointOLD p : children)
              {
                g.point(p.x,  p.y, p.z);
              }
            }
          };
          
          emitters.add(e);
    }
  }
  
  @Override
  public void reanderAfterBleed(PGraphics g)
  {
    //g.background(0, 32);
    g.pushMatrix();
    g.stroke(255, 255, 255, 32);
    g.strokeWeight(3f);
    g.fill(255);
    for(OLDEmitter<?> e : emitters)
    {
      e.draw(g);
    }
    g.popMatrix();
  }

  @Override
  public PointOLD[] init()
  {
    return new PointOLD[]{};
  }

  @Override
  public void update()
  {
    long time = System.currentTimeMillis();
    for(OLDEmitter<?> e : emitters)
      e.update(time);
    
    for(MotionInteractiveBehavior b : behaviors)
      ActionsOLD.engine.addBehavior(b);
  }

  @Override
  public void go(MotionEngine engine)
  {
    try
    {
      Cue q = Cue.create("reset",   
          ActionsOLD.dragToNone,
          ActionsOLD.gravityOff,
          ActionsOLD.homeOff,
          ActionsOLD.homeLinearOff);
      
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
    return "random-particles-lines-layer";
  }
}
