package shows.oz;

import java.util.ArrayList;

import com.danielbchapman.motion.core.Motion;
import com.danielbchapman.motion.core.MotionBrush;
import com.danielbchapman.motion.core.MotionMouseEvent;
import com.danielbchapman.motion.core.PhysicsScene;
import com.danielbchapman.physics.toxiclibs.ActionsOLD;

import processing.core.PGraphics;
import shows.test.SimpleWindBehavior;
import toxi.geom.Vec3D;
import toxi.physics3d.VerletPhysics3D;

public class PoppyField extends PhysicsScene
{
  public float amount = 1.0f;
  public int rate = 2000; 
  public int lifeSpan = 10000;
  
  boolean left = false;
  boolean right = true;
  boolean top = true;
  ArrayList<PoppyEmitter> topSprites = new ArrayList<>();
  ArrayList<PoppyEmitter> leftSprites = new ArrayList<>();
  ArrayList<PoppyEmitter> rightSprites = new ArrayList<>();
  SimpleWindBehavior wind = new SimpleWindBehavior(new Vec3D(-0.01f, 0.02f, 0));
  
  @Override
  public void initialize(Motion motion)
  {
    int life = 10000;
    physics = new VerletPhysics3D();
    physics.setDrag(0.02f);
    physics.addBehavior(ActionsOLD.gravity);
    physics.addBehavior(wind);
    //physics.addBehavior(new GravityBehavior3D(new Vec3D(0, .1f, 0)));
    int spacing = 50;
    int widthCells = (Motion.WIDTH / spacing) + 2;
    int heightCells = (Motion.HEIGHT / spacing);
    
    //Top
    for(int i = -2; i < Motion.WIDTH / 50; i++)
    {
      Vec3D wind = new Vec3D(0, 0, 0);
      Vec3D pos = new Vec3D(spacing * i, -100, 0);
      PoppyEmitter e = new PoppyEmitter(motion, physics, pos, wind, lifeSpan, rate, 2f, 100);  
      e.physics = this.physics;
      topSprites.add(e);      
    }
    
    //Left
    for(int i = 0; i < heightCells; i++)
    {
      Vec3D wind = new Vec3D(-1, 0, 0);
      Vec3D pos = new Vec3D(-100, i * spacing, 0);
      PoppyEmitter e = new PoppyEmitter(motion, physics, pos, wind, lifeSpan, rate, 2f, 100);  
      e.physics = this.physics;
      leftSprites.add(e);
    }
    
    //Right
    for(int i = 0; i < heightCells; i++)
    {
      Vec3D wind = new Vec3D(-1, 0, 0);
      Vec3D pos = new Vec3D(Motion.WIDTH + 100, i * spacing, 0);
      PoppyEmitter e = new PoppyEmitter(motion, physics, pos, wind, lifeSpan, rate, 2f, 100);  
      e.physics = this.physics;
      rightSprites.add(e);
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
    if(left)
      leftSprites.forEach(e -> e.update(time) );
    if(right)
      rightSprites.forEach(e -> e.update(time) );
    if(top)
      topSprites.forEach(e -> e.update(time) );
      
  }
  
  @Override
  public void draw(PGraphics g)
  { 
    g.clear();//no motion trails
    g.fill(0,0,0, 0);
    topSprites.forEach(e -> {
      e.draw(g);
    });
    leftSprites.forEach(e -> {
      e.draw(g);
    });
    rightSprites.forEach(e -> {
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
  
  @Override
  public void go(Motion motion)
  {
    System.out.println("Snow Start");
    long time = System.currentTimeMillis();
    topSprites.forEach(p -> {
      p.startSnow(time);
    });
    leftSprites.forEach(p -> {
      p.startSnow(time);
    });
    rightSprites.forEach(p -> {
      p.startSnow(time);
    });
  }
}
