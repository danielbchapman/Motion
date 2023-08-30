package shows.ululations;

import java.util.ArrayList;

import com.danielbchapman.motion.core.Actions;
import com.danielbchapman.motion.core.Emitter;
import com.danielbchapman.motion.core.HomeBehavior3D;
import com.danielbchapman.motion.core.Motion;
import com.danielbchapman.motion.core.MotionBrush;
import com.danielbchapman.motion.core.PhysicsScene;
import com.danielbchapman.motion.core.Point;

import processing.core.PGraphics;
import shows.test.SimpleWindBehavior;
import toxi.geom.Vec3D;
import toxi.physics3d.VerletParticle3D;

public class PointGridLayer extends PhysicsScene
{
  ArrayList<Emitter<Point>> emitters = new ArrayList<>();
  
  ArrayList<MotionBrush> behaviors = new ArrayList<>();
	boolean first = true;
	
  @Override
  public void draw(PGraphics g)
  {
  	super.draw(g);
  	g.background(0);
//  	if(first)
//  	{
//  		//Draw black
//  		g.clear();
//  		g.background(0);
//  		first = false;
//  	}
    //g.background(0, 32);
    g.pushMatrix();
    VerletParticle3D p;
    for(int i = 0; i < points.length; i++)
    {
    	p = points[i];
    	if(p == null) {
    		boolean debug = true;
    	}
    	g.strokeWeight(2);
    	g.stroke(255);
    	g.point(p.x, p.y, p.z);
    }
    g.popMatrix();
  }

  Point[] points;
  
  @Override
  public void initialize(Motion motion)
  {
  	super.initialize(motion);
  	first = true;
  	points = new Point[128*80]; //10 pixel offset 10K
  	int i = 0;
  	for(int x = 0; x < 128; x++) 
  	{
  		for(int y = 0; y < 80; y++)
  		{  			
  			Point p = new Point(x * 10, y * 10, 0, 1);
  			points[i] = p;
  			physics.addParticle(p);
  			i++;
  		}
  	}
  	
  	try
		{
			Actions.homeTo10f.call();
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  	physics.addBehavior(Actions.home);
  	physics.setDrag(0.2f);
  	
  	
  //behaviors.add(new ExplodeBehaviorInverse(new Vec3D(-1f, 0, 0), -50f));
//    for(int i = 0; i < 14; i++)
//    {
//      Emitter<Point> e = 
//          new Emitter<Point>(physics, new Vec3D(i * 128, 400, 0), new Vec3D(-1, 0, 0), 25000, 200, 1f, 200)
//          {
//            @Override
//            public Point createPoint(float x, float y, float z, float w)
//            {
//              return new Point(x, y, z, w);
//            }
//    
//            @Override
//            public void draw(PGraphics g)
//            {
//              for(int i = 0; i < children.size() - 1; i++)
//              {
//              	Point a = children.get(i);
//              	Point b = children.get(i+1);
//                
//                g.line(a.x, a.y, a.z, b.x, b.y, b.z);
//              }
//              for(Point p : children)
//              {
//                g.point(p.x,  p.y, p.z);
//              }
//            }
//          };
//          
//          emitters.add(e);
//    }
  }

  @Override
  public void update(long time)
  {
  	super.update(time);
    for(Emitter<?> e : emitters)
      e.update(time);
   
//      ActionsOLD.engine.addBehavior(b);
  }

  @Override
  public void go(Motion motion)
  {
  	// TODO Auto-generated method stub
  	super.go(motion);
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
//
//  @Override
//  public String getName()
//  {
//    return "PointGridLayer";
//  }
}
