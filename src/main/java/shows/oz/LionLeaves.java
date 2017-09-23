package shows.oz;

import java.util.ArrayList;

import com.danielbchapman.motion.core.Log;
import com.danielbchapman.motion.core.Motion;
import com.danielbchapman.motion.core.MotionBrush;
import com.danielbchapman.motion.core.MotionMouseEvent;
import com.danielbchapman.motion.core.PhysicsScene;
import com.danielbchapman.physics.toxiclibs.Actions;

import processing.core.PGraphics;
import processing.core.PShape;
import shows.test.PlaybackCue;
import shows.test.SimpleWindBehavior;
import toxi.geom.Vec3D;
import toxi.physics3d.VerletPhysics3D;

public class LionLeaves extends PhysicsScene
{
  public float amount = 1.0f;
  boolean left = false;
  boolean right = true;
  boolean top = true;
  float zBase = 400;
  
  ArrayList<LeafEmitterYellow> topLeaves = new ArrayList<>();
  ArrayList<LeafEmitterYellow> leftLeaves = new ArrayList<>();
  ArrayList<LeafEmitterYellow> rightLeaves = new ArrayList<>();

  SimpleWindBehavior wind = new SimpleWindBehavior(new Vec3D(-0.05f, 0, 0));
  PlaybackCue flutter;
  @Override
  public void initialize(Motion motion)
  {
    flutter = new PlaybackCue("captures/snag-wind-going-left", "brushes/minimal-wind-affect", "Flutter the Wind", motion.width, motion.height, 0, 0);
    int life = 10000;
    physics = new VerletPhysics3D();
    physics.setDrag(0.02f);
    physics.addBehavior(wind);
    physics.addBehavior(Actions.gravity);
    //physics.addBehavior(new GravityBehavior3D(new Vec3D(0, .1f, 0)));
    int spacing = 50;
    int widthCells = (Motion.WIDTH / spacing) + 2;
    int heightCells = (Motion.HEIGHT / spacing);
    
    //Top
    for(int i = -2; i < Motion.WIDTH / 50; i++)
    {
      Vec3D wind = new Vec3D(0, 0, 0);
      Vec3D pos = new Vec3D(spacing * i, 100, zBase);
      LeafEmitterYellow e = new LeafEmitterYellow(motion, physics, pos, wind, 5000, 100, 2f, 100);  
      e.physics = this.physics;
      topLeaves.add(e);      
    }
    
    //Left
    for(int i = 0; i < heightCells; i++)
    {
      Vec3D wind = new Vec3D(-1, 0, 0);
      Vec3D pos = new Vec3D(100, i * spacing, zBase);
      LeafEmitterYellow e = new LeafEmitterYellow(motion, physics, pos, wind, 20000, 100, 2f, 100);  
      e.physics = this.physics;
      leftLeaves.add(e);
    }
    
    //Right
    for(int i = 0; i < heightCells; i++)
    {
      Vec3D wind = new Vec3D(-1, 0, 0);
      Vec3D pos = new Vec3D(Motion.WIDTH - 100, i * spacing, zBase);
      LeafEmitterYellow e = new LeafEmitterYellow(motion, physics, pos, wind, 20000, 100, 2f, 100);  
      e.physics = this.physics;
      rightLeaves.add(e);
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
    if(right)
      rightLeaves.forEach(e -> e.update(time) );
    if(top)
      topLeaves.forEach(e -> e.update(time) );
      
  }
  
  @Override
  public void draw(PGraphics g)
  { 
    g.clear();//no motion trails
    g.fill(0,0,0, 0);
    topLeaves.forEach(e -> {
      e.draw(g);
    });
    leftLeaves.forEach(e -> {
      e.draw(g);
    });
    rightLeaves.forEach(e -> {
      e.draw(g);
    });
  }
  
  int cycle = 0;
  

  @Override
  public void go(Motion motion)
  {
    if(flutter != null)
    {
      motion.runPlayback(flutter.name, flutter.actions, flutter.brush);
    }
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
