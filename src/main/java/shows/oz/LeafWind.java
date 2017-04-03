package shows.oz;

import java.util.ArrayList;

import com.danielbchapman.motion.core.IPhysicsBrush;
import com.danielbchapman.motion.core.Motion;
import com.danielbchapman.motion.core.MotionBrush;
import com.danielbchapman.motion.core.MotionMouseEvent;
import com.danielbchapman.motion.core.PhysicsScene;

import processing.core.PGraphics;
import toxi.geom.Vec3D;
import toxi.physics3d.VerletPhysics3D;
import toxi.physics3d.behaviors.GravityBehavior3D;

public class LeafWind extends PhysicsScene
{
  public float amount = 1.0f;
  ArrayList<LeafEmitter> leaves = new ArrayList<>();

  @Override
  public void initialize(Motion motion)
  {
    physics = new VerletPhysics3D();
    physics.setDrag(0.02f);
    //physics.addBehavior(new GravityBehavior3D(new Vec3D(0, .1f, 0)));
    for(int i = 0; i < 10; i++)
    {
      Vec3D wind = new Vec3D(-1, 0, 0);
      Vec3D pos = new Vec3D(50 * i, 50, 0);
      LeafEmitter e = new LeafEmitter(physics, pos, wind, 20000, 100, 0f, 100);  
      e.physics = this.physics;
      leaves.add(e);      
    }
  }
  
  @Override
  public boolean isPersistent()
  {
    return false;
  }
  
  @Override
  public void update(long time)
  { 
    super.update(time);
    leaves.forEach(e -> e.update(time) );
  }
  
  @Override
  public void draw(PGraphics g)
  { 
    g.background(0,0,0,0);
    g.line(0, 0, 500, 500);
    leaves.forEach(e -> {
      e.draw(g);
    });
  }
  
  @Override
  public void applyBrush(MotionBrush brush, PGraphics g, MotionMouseEvent point)
  {
    super.applyBrush(brush, g, point);
//    if(brush instanceof IPhysicsBrush)
//    {
//      IPhysicsBrush vert = (IPhysicsBrush) brush;
//      vert.setPosition(new Vec3D(point) );
//      active.add(vert);
//      System.out.println("applying brush");
//    }
  }
}
