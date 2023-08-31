package shows.ululations;

import java.util.ArrayList;

import com.danielbchapman.motion.core.Actions;
import com.danielbchapman.motion.core.Emitter;
import com.danielbchapman.motion.core.HomeBehavior3D;
import com.danielbchapman.motion.core.Motion;
import com.danielbchapman.motion.core.MotionBrush;
import com.danielbchapman.motion.core.PhysicsScene;
import com.danielbchapman.motion.core.Point;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PShape;
import shows.test.SimpleWindBehavior;
import toxi.geom.Vec3D;
import toxi.physics3d.VerletParticle3D;

public class PointGridLayer extends PhysicsScene
{
  ArrayList<Emitter<Point>> emitters = new ArrayList<>();
  
  ArrayList<MotionBrush> behaviors = new ArrayList<>();
	boolean first = true;
	WaveField waves = new WaveField();
	PImage texture;
	PGraphics sprite; 
	PShape mesh;
	
	float deg90 = (float) (90f * 180f / Math.PI);
	public PointGridLayer()
	{
		super();
		//640, 1000, 33 / This is the side view
//		createPropFloat("cameraX", Motion.WIDTH / 2.0f);
//		createPropFloat("cameraY", Motion.HEIGHT / 2.0f);
//		createPropFloat("cameraZ", (float) ((Motion.HEIGHT/2.0) / Math.tan(Math.PI*30.0 / 180.0)));
		
		createPropFloat("cameraX", 640);
		createPropFloat("cameraY", 1000);
		createPropFloat("cameraZ", 33);
		
		texture = Motion.MOTION.loadImage("content/sprites/light.png");
		sprite = Motion.MOTION.createGraphics(texture.width, texture.height);
		sprite.beginDraw();
		sprite.clear();
		sprite.fill(255,0,0,0);
		sprite.rect(0, 0, sprite.width, sprite.height);
		//sprite.image(cache, 0, 0);
		sprite.stroke(255);
		sprite.strokeWeight(5);
		sprite.point(sprite.width / 2, sprite.height / 2);
		sprite.tint(255, 128);
		sprite.endDraw();
		
		mesh = Motion.MOTION.createShape();
		mesh.beginShape();
		mesh.vertex(20, 0);
		mesh.vertex(40, 20);
		mesh.vertex(20, 40);
		mesh.vertex(0, 20);
		mesh.fill(255, 255, 255, 128);
		mesh.stroke(0, 255, 0);
		mesh.endShape();
		
	}
	
  @Override
  public void draw(PGraphics g)
  {
  	super.draw(g);
  	
  	/*camera(width/2.0, height/2.0, (height/2.0) / tan(PI*30.0 /
  	 * 180.0), width/2.0, height/2.0, 0, 0, 1, 0)</b>. This function is similar
  	 * to <b>gluLookAt()</b> in OpenGL, but it first clears the current camera settings.
  	 * 
  	 */
//  	float eyeX =(float) (Motion.WIDTH / 2.0);
//  	float eyeY = (float) (Motion.HEIGHT / 2.0) + 200;
//  	float eyeZ = (float) ((Motion.HEIGHT/2.0) / Math.tan(Math.PI*30.0 / 180.0));
//  	
  	float eyeX = getPropFloat("cameraX");
  	float eyeY = getPropFloat("cameraY");
  	float eyeZ = getPropFloat("cameraZ");
  	float centerX = (float) (Motion.WIDTH / 2.0);
  	float centerY = (float) (Motion.HEIGHT / 2.0);
  	float centerZ = 0;
  	
  	float upX = 0;
  	float upY = 1;
  	float upZ = 0;
  	g.camera(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
  	g.blendMode(PConstants.BLEND);
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
    g.blendMode(PConstants.ADD);
    VerletParticle3D p;
    for(int i = 0; i < points.length; i++)
    {
    	p = points[i];
    	
    	g.pushMatrix();
    	//g.blendMode(PConstants.ADD);
//    	g.stroke(255, 0, 0);
//    	g.fill(255, 0, 0);
    	g.translate(p.x, p.y, p.z);
    	//g.shape(mesh);
    	//g.sphere(10f);
//    	g.rotateX(deg90);
//    	g.rotateZ(deg90);
    	//g.image(texture, 0,  0,  20,  20);

    	g.strokeWeight(4);
    	g.stroke(255, 255, 255, 255);
    	g.point(p.x, p.y, p.z);
    	g.popMatrix();
    }
    g.popMatrix();
  }

  Point[] points;
  
  @Override
  public void initialize(Motion motion)
  {
  	super.initialize(motion);
  	//Initialize First Pass
  	first = true;
  	
  	int xP = 128;
  	int yP = 80;
  	int spacingX = 10;
  	int spacingY = 10;
  	points = new Point[xP*yP]; //10 pixel offset 10K
  	int i = 0;
  	for(int x = 0; x < xP; x++) 
  	{
  		for(int y = 0; y < yP; y++)
  		{  			
  			Point p = new Point(x * spacingX, y * spacingY, 0, 1);
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
  	waves = new WaveField();
  	physics.addBehavior(Actions.home);
  	physics.setDrag(0.2f);
  	physics.addBehavior(waves);
  	
  	
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
  	waves.update(time);
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