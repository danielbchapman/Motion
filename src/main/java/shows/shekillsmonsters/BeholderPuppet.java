package shows.shekillsmonsters;

import javafx.beans.property.SimpleObjectProperty;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PShape;
import toxi.geom.Vec2D;
import toxi.geom.Vec3D;

import com.danielbchapman.artwork.Shapes;
import com.danielbchapman.physics.toxiclibs.Layer;
import com.danielbchapman.physics.toxiclibs.MotionEngine;
import com.danielbchapman.physics.toxiclibs.Point;
import com.danielbchapman.physics.toxiclibs.Transform;

public class BeholderPuppet extends Layer
{
  PShape mainEye;
  PShape axes;
  Point lookTo = new Point(400, 300, 0, 1);
  Point beholder = new Point(400,200, -500, 1);
  int mX = 400;
  int mY = 300;
  
  int zOffset = 350;
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
  boolean forward = true;
  @Override
  public Point[] init()
  {
    return new Point[0];
  }

  public void initShapes(PGraphics g)
  {
    mainEye = Shapes.texturedSphere(g, 100, engine.loadImage("core/placeholders/checkers.jpg"), 35);
    axes = Shapes.axes(g);
  }
  @Override
  public void render(PGraphics g)
  {
    mousePressed();
    if(mainEye == null)
    {
      initShapes(g);
    }
    
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
    if(forward)
      move += 1f/120f; //60 frames to center
    else
      move -= 1f/120f;
    //Animate left/right
    if(move > 2f)
    {
      forward = false;
      move = 2f;
    }
    else if (move < 0f)
    {
      move = 0;
      forward = true;
    }
      
    
    lookTo.x = Transform.size(move - 1f, 800);
    //beholder.x = Transform.size(move-1f,  600) + 100;
    g.ellipse(mX, mY, 24, 24);
    g.pushMatrix();
    g.translate(mX, 200, mY-zOffset);//follow 
    g.sphere(40);
    g.popMatrix();
  }
  
  public void drawMainEye(PGraphics g)
  {
    g.pushMatrix();
    g.translate(beholder.x, beholder.y, 0);
    
    Vec3D from = new Vec3D(mX, 0, mY-zOffset);
    viewFrom(g,  from, new Vec3D(beholder.x, beholder.y, 0));
    //g.shape(mainEye);
    g.shape(mainEye);
    g.shape(axes);
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
    float x = to.x - from.x;
    float y = to.y -  from.y;
    float z = to.z - from.z;
    
    float rX = (float) Math.atan2(x, z);
    float rZ = (float) Math.atan2(y, x);
    
    System.out.println("(" + x  +", " + y + ", " + z + ") | rx = " + rX + " " + rZ + " | " + PConstants.PI);
    System.out.println("\trX: " + PApplet.degrees(rX));
    System.out.println("\trY: " + PApplet.degrees(0));
    System.out.println("\trZ: " + PApplet.degrees(rZ));
    //g.rotateZ(rZ);
    g.rotateY(rX);
  }
  
  void mousePressed()
  {
    mX = engine.mouseX;
    mY = engine.mouseY;
  }
}
