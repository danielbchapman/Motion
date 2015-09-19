package shows.shekillsmonsters;

import processing.core.PConstants;
import processing.core.PGraphics;
import toxi.geom.Vec2D;

import com.danielbchapman.physics.toxiclibs.Layer;
import com.danielbchapman.physics.toxiclibs.MotionEngine;
import com.danielbchapman.physics.toxiclibs.Point;
import com.danielbchapman.physics.toxiclibs.Transform;

public class BeholderPuppet extends Layer
{

  Point lookTo = new Point(0, 0, 0, 1);
  Point beholder = new Point(0,0, 0, 1);
  
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
    
    
    //Animate this across to check tracking...
    move += 1f/120f; //60 frames to center
    //Animate left/right
    if(move > 2f)
      move = 0f;
    
    lookTo.x = Transform.size(move - 1f, 800);
  }
  
  public void drawMainEye(PGraphics g)
  {
    
    Vec2D baseX = new Vec2D(1, 0);
    
    int mx = engine.mouseX;
    float tangent = ((float) mx) / 400;
    System.out.println(tangent + " (" + mx + " " + lookTo.y + ")");
    g.pushMatrix();

    g.box(100, 100, 100);
    g.translate(400, 200, 0);
    g.rotateY(tangent * PConstants.PI);
    g.box(50);
    
    g.popMatrix();
  }
  
  public void drawLittleEye(PGraphics g, float radius)
  {
    
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
}
