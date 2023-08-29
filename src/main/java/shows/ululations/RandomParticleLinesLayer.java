package shows.ululations;

import java.util.ArrayList;
import java.util.Map;

import processing.core.PGraphics;
import toxi.geom.Vec3D;
import toxi.physics3d.VerletParticle3D;
import toxi.physics3d.VerletPhysics3D;

import com.danielbchapman.layers.BleedingLayer;
import com.danielbchapman.motion.core.Emitter;
import com.danielbchapman.motion.core.Motion;
import com.danielbchapman.motion.core.MotionBrush;
import com.danielbchapman.motion.core.Point;

public class RandomParticleLinesLayer extends BleedingLayer
{
  ArrayList<Emitter<Point>> emitters = new ArrayList<>();
  
  ArrayList<MotionBrush> behaviors = new ArrayList<>();
  
  public VerletPhysics3D physics;
	
	@Override
	public boolean isVerletPhysic3D()
	{
		return true;
	}
	@Override
	public VerletPhysics3D get3DPhysics() {
		return physics;
	}
	
  @Override
  public void reanderAfterBleed(PGraphics g)
  {
    //g.background(0, 32);
    g.pushMatrix();
    g.stroke(255, 255, 255, 32);
    g.strokeWeight(3f);
    g.fill(255);
    for(Emitter<?> e : emitters)
    {
      e.draw(g);
    }
    g.popMatrix();
  }

  @Override
  public void initialize(Motion motion)
  {
  	super.initialize(motion);
  	physics = new VerletPhysics3D();
  	
  //behaviors.add(new ExplodeBehaviorInverse(new Vec3D(-1f, 0, 0), -50f));
    for(int i = 0; i < 14; i++)
    {
      Emitter<Point> e = 
          new Emitter<Point>(physics, new Vec3D(i * 128, 400, 0), new Vec3D(-1, 0, 0), 25000, 200, 1f, 200)
          {
            @Override
            public Point createPoint(float x, float y, float z, float w)
            {
              return new Point(x, y, z, w);
            }
    
            @Override
            public void draw(PGraphics g)
            {
              for(int i = 0; i < children.size() - 1; i++)
              {
              	Point a = children.get(i);
              	Point b = children.get(i+1);
                
                g.line(a.x, a.y, a.z, b.x, b.y, b.z);
              }
              for(Point p : children)
              {
                g.point(p.x,  p.y, p.z);
              }
            }
          };
          
          emitters.add(e);
    }
  }

  @Override
  public void update(long time)
  {
  	physics.update();
    for(Emitter<?> e : emitters)
      e.update(time);
    
    
//    for(MotionInteractiveBehavior b : behaviors)
//      ActionsOLD.engine.addBehavior(b);
  }

  @Override
  public void go(Motion motion)
  {
  	// TODO Auto-generated method stub
  	super.go(motion);
  	clear();
  }
//  @Override
//  public void go(MotionEngine engine)
//  {
//    try
//    {
//      Cue q = Cue.create("reset",   
//          ActionsOLD.dragToNone,
//          ActionsOLD.gravityOff,
//          ActionsOLD.homeOff,
//          ActionsOLD.homeLinearOff);
//      
//      clear();
//      q.go(this,  engine);
//    }
//    catch (Exception e)
//    {
//      // TODO Auto-generated catch block
//      e.printStackTrace();
//    }
//  }

  @Override
  public String getName()
  {
    return "random-particles-lines-layer";
  }
}
