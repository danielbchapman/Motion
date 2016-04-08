package shows.gravitationalwaves;

import com.danielbchapman.layers.BleedingLayer;
import com.danielbchapman.physics.toxiclibs.AbstractEmitter;
import com.danielbchapman.physics.toxiclibs.ExplodeBehavior;
import com.danielbchapman.physics.toxiclibs.ExplodeBehaviorInverse;
import com.danielbchapman.physics.toxiclibs.LetterEmitter;
import com.danielbchapman.physics.toxiclibs.MotionEngine;
import com.danielbchapman.physics.toxiclibs.MotionInteractiveBehavior;
import com.danielbchapman.physics.toxiclibs.Point;

import processing.core.PConstants;
import processing.core.PGraphics;
import toxi.geom.Vec3D;
import toxi.physics3d.behaviors.AttractionBehavior3D;
import toxi.physics3d.behaviors.ParticleBehavior3D;

public class GalaxyLayer extends BleedingLayer
{
  Vec3D pos;
  Point center;
  Point arm1;
  Point arm2;
  
  MotionInteractiveBehavior attractorA;
  MotionInteractiveBehavior attractorB;
  
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
      
  Point[] points;
  
  public GalaxyLayer(MotionEngine engine)
  {
    this.engine = engine;
    //VerletSpring3D ca1 = new VerletSpring3D(a, b, len, str)
  }
  @Override
  public void reanderAfterBleed(PGraphics g)
  {
    g.pushMatrix();
    g.stroke(255);
    g.fill(255);
    g.text("GALAXY LAYER", 25, 25);
    g.popMatrix();
    
    g.ellipseMode(PConstants.CENTER);
    g.pushMatrix();
    g.ellipse(center.x, center.y, 25, 25);
    g.popMatrix();
    
    g.pushMatrix();
    g.ellipse(arm1.x, arm1.y, 25, 25);
    g.popMatrix();
    
    g.pushMatrix();
    g.ellipse(arm2.x, arm2.y, 25, 25);
    g.popMatrix();
    
    g.stroke(255, 0, 255);
    g.fill(0, 255, 255);
    //Draw Reds
    if(attractorA != null)
    {
      g.pushMatrix();
      g.ellipse(attractorA.vars.getPosition().x, attractorA.vars.getPosition().y, 25, 25);
      g.popMatrix();
      
      g.pushMatrix();
      g.ellipse(attractorB.vars.getPosition().x, attractorB.vars.getPosition().y, 25, 25);
      g.popMatrix();  
    }
    
    spline.draw(g);
  }

  @Override
  public Point[] init()
  {
    if(points != null)
      return points;
    
    pos = new Vec3D(720, 360, 0);
    
    center = new Point(pos.x, pos.y, pos.z, 1);
    center.lock();
    arm1 = new Point(pos.x, pos.y + 100, pos.z, 1);
    arm2 = new Point(pos.x, pos.y - 100, pos.z, 1);
    
    points = new Point[]{ center, arm1, arm2 };
    return points;
  }

  long start = -1L;
  private Vec3D newLocation(long time)
  {
    if(start < 0)
      start = time;
    
    long delta = time - start;
    float rads = ((float) delta) / 5000f;
    Vec3D newVec = new Vec3D(0, 100, 0);
    newVec.rotateZ(rads);
    
    return newVec;
  }
  
  @Override
  public void update()
  {
    if(attractorA == null)
    {
      attractorA = new ExplodeBehavior();//new AttractionBehavior3D(new Vec3D(0,100,0), 1000f, 10f);
      attractorB = new ExplodeBehavior();
      
      attractorA.vars.setMagnitude(25f);
      attractorB.vars.setMagnitude(25f);
    }
    
    long base = System.currentTimeMillis();
    
    Vec3D arm = newLocation(base);
    attractorA.setPosition(arm.copy().add(center.x, center.y, 0));;
    attractorB.setPosition(arm.copy().rotateZ((float) Math.PI).add(center.x, center.y, 0));
    
    engine.addBehavior(attractorA);
    engine.addBehavior(attractorB);
    spline.update(base);
  }

  @Override
  public void go(MotionEngine engine)
  { 
  }
  
  @Override
  public String getName()
  {
    return "galaxy-layer"; 
  }

}
