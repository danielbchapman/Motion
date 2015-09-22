package shows.shekillsmonsters;

import processing.core.PConstants;
import processing.core.PGraphics;
import toxi.geom.Vec2D;
import toxi.geom.Vec3D;

import com.danielbchapman.physics.toxiclibs.Layer;
import com.danielbchapman.physics.toxiclibs.MotionEngine;
import com.danielbchapman.physics.toxiclibs.Point;
import com.danielbchapman.physics.toxiclibs.Transform;

public class BeholderPuppet extends Layer
{

  Point lookTo = new Point(0, 0, 0, 1);
  Point beholder = new Point(400,200, 200, 1);
  int mX = 400;
  int mY = 300;
  Point[] eyes = new Point[]
      {
        new Point(200, 100, 85, 1),
        new Point(250, 400, 85, 1),
        new Point(300,  100, 85, 1),
        new Point(350, 200, 85, 1),
        new Point(400, 100, 85, 1),
        new Point(450, 200, 85, 1),
        new Point(500, 100, 85, 1)
      };
  
  float move = 0;
  @Override
  public Point[] init()
  {
    return new Point[0];
  }

  @Override
  public void render(PGraphics g)
  {
    g.background(0, 0, 64);
    g.lights();
    g.pushMatrix();
    g.stroke(255);
    g.fill(255);
    g.translate(lookTo.x, 75);//Top of screen
    g.ellipse(0,0,10,10);
    g.popMatrix();
    
    drawMainEye(g);
    for(int i = 0; i < eyes.length; i++)
      ;//drawLittleEye(g, eyes[i], 25);
    
    //Animate this across to check tracking...
    move += 1f/120f; //60 frames to center
    //Animate left/right
    if(move > 2f)
      move = 0f;
    
    lookTo.x = Transform.size(move - 1f, 800);
    beholder.x = Transform.size(move-1f,  600) + 100;
    g.ellipse(mX, mY, 25, 25);
  }
  
  public void drawMainEye(PGraphics g)
  {
    g.pushMatrix();
    
    g.translate(beholder.x, beholder.y, 0);
    Vec3D from = new Vec3D(0, 0, 0);
    viewFrom(g,  from, new Vec3D(mX - beholder.x, 600, mY - beholder.y));
    g.box(50);
    g.popMatrix();
  }
  
  public void drawLittleEye(PGraphics g, Point p, float radius)
  {
    g.pushMatrix();
    g.translate(p.x, p.y, p.z);
    viewFrom(g,  beholder, new Vec3D(engine.mouseX, 50, engine.mouseY));
    g.box(radius);
    
    g.popMatrix();    
  }

  @Override
  public void update()
  {
  }

  @Override
  public void go(MotionEngine engine)
  {
    System.out.println("GO");
  }
  
  public String getName()
  {
    return "Beholder";
  }
  
  public void viewFrom(PGraphics g, Vec3D from, Vec3D to)
  {
    float x = from.x - to.x;
    float y = from.y -  to.y;
    float z = from.z - to.z;
    
    float rX = (float) Math.atan2(x, y);
    float rZ = (float) Math.atan2(z, y);
    
    System.out.println("(" + x  +", " + y + ", " + z + ") | rx = " + rX + " " + rZ);
    g.rotateY(rX);
    g.rotate(rZ);
  }
  
  void mousePressed()
  {
    mX = engine.mouseX;
    mY = engine.mouseY;
  }
}
