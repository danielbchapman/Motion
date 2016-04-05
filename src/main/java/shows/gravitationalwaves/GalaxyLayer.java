package shows.gravitationalwaves;

import com.danielbchapman.layers.BleedingLayer;
import com.danielbchapman.physics.toxiclibs.Actions;
import com.danielbchapman.physics.toxiclibs.MotionEngine;
import com.danielbchapman.physics.toxiclibs.Point;

import processing.core.PConstants;
import processing.core.PGraphics;
import toxi.geom.Vec3D;

public class GalaxyLayer extends BleedingLayer
{
  Vec3D pos;
  Point center;
  Point arm1;
  Point arm2;
  
  Point[] points;
  
  public GalaxyLayer()
  {
    
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
    
//    VerletConstrainedSpring3D centerArm1 = new VerletConstrainedSpring3D(center, arm1, 25, -.2f);
//    VerletConstrainedSpring3D centerArm2 = new VerletConstrainedSpring3D(center, arm2, 25, -.2f);
//    Actions.engine.getPhysics().addSpring(centerArm1);
//    Actions.engine.getPhysics().addSpring(centerArm2);
    
    Actions.engine.getPhysics().setDrag(.2f);
    points = new Point[]{ center, arm1, arm2 };
    return points;
  }

  @Override
  public void update()
  {
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
