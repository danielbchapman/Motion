package shows.oz;

import java.util.ArrayList;

import com.danielbchapman.motion.core.AbstractEmitter;
import com.danielbchapman.motion.core.Motion;
import com.danielbchapman.physics.toxiclibs.Point;

import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PShape;
import toxi.geom.Vec3D;
import toxi.physics3d.VerletPhysics3D;

public class PoppyEmitter extends AbstractEmitter<Poppy>
{
  static PShape[] leafShapes;
  Motion motion;
  int randomHigh;
  
  
  public PoppyEmitter(Motion motion, VerletPhysics3D physics, Vec3D position, Vec3D heading, int lifeSpan, int rate, float randomVector, int randomTime)
  {
    super(physics, position, heading, lifeSpan, rate, randomVector, randomTime);
    this.motion = motion;
  }

  @Override
  public Poppy createPoint(float x, float y, float z, float w)
  {
    Poppy l = new Poppy(x, y, z, w);
    l.angular = new Point(motion.random(25), motion.random(25), motion.random(25), 0);
    return l;
  }

  public static synchronized void initializeLeaves(PGraphics g)
  {
    ArrayList<PShape> shapes = new ArrayList<PShape>();
    
    for(int i = 0; i < 10; i++)
    {
      PShape l = g.createShape(PConstants.RECT, 0, 0, 40, 40);
      l.beginShape();
      l.vertex(20, 0);
      l.vertex(40, 20);
      l.vertex(20, 40);
      l.vertex(0, 20);
      l.fill(255, i * 50 % 255, 0);
      l.stroke(0, 255, 0);
      l.endShape();
      
      shapes.add(l);
    }
    
    leafShapes = new PShape[shapes.size()];
    for(int i = 0; i < shapes.size(); i++)
      leafShapes[i] = shapes.get(i);
  }
  
  PImage[] sprites;
  
  @Override
  public void draw(PGraphics g)
  {
    if(sprites == null)
    { //Stubbed in in hopes of a dynamic constructor
      ArrayList<PImage> tmp = new ArrayList<>();
      tmp.add(motion.loadImage("content/poppy/poppy1.png"));
      tmp.add(motion.loadImage("content/poppy/poppy2.png"));
      tmp.add(motion.loadImage("content/poppy/poppy3.png"));
      sprites = new PImage[tmp.size()];
      for(int i = 0; i < tmp.size(); i++)
        sprites[i] = tmp.get(i);
    }
    if(leafShapes == null)
    {
      initializeLeaves(g);
    }
    
//    PShape shape = g.createShape(PConstants.RECT, 0, 0, 128, 40);
//    shape.beginShape();
//    shape.vertex(20, 0);
//    shape.vertex(40, 20);
//    shape.vertex(20, 40);
//    shape.vertex(0, 20);
//    shape.fill(255, 255, 0);
//    shape.stroke(0, 255, 0);
//    shape.endShape();
    
    int i = 0;
    int length = sprites.length;
    for(Poppy p : this.children)
    {
      if(p.image < 0){
        p.image = i % length;
      }
      g.pushMatrix();
      g.stroke(255,0,0);
      g.fill(255,0,0);
      //Rotate on X
      //g.rotate(p.x);
      g.translate(p.x, p.y, p.z);
//      Point.rotation(g, p);
      g.image(sprites[p.image % length] , 0, 0, p.size, p.size);
      g.popMatrix();
      i = i+1;//++ seemed to not do anything?
    }
  }

}
