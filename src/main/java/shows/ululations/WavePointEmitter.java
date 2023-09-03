package shows.ululations;

import java.util.Random;

import com.danielbchapman.motion.core.Emitter;
import com.danielbchapman.motion.core.Motion;
import com.danielbchapman.motion.core.Point;

import processing.core.PGraphics;
import processing.core.PImage;
import toxi.geom.Vec3D;
import toxi.physics3d.VerletPhysics3D;

public class WavePointEmitter extends Emitter<Point>
{
	float deg90 = (float) (90f * 180f / Math.PI);
	float deg60 = (float) (60f * 180f / Math.PI);
	PImage texture;
	public WavePointEmitter(VerletPhysics3D physics, Vec3D position, Vec3D heading, int lifeSpan, int rate,
			float randomVector, int randomTime)
	{
		super(physics, position, heading, lifeSpan, rate, randomVector, randomTime);
		texture = Motion.MOTION.loadImage("content/sprites/light.png");
	}

	@Override
	public Point createPoint(float x, float y, float z, float w)
	{
		Random rand = new Random();
    float randX = rand.nextInt(800) - 400 + x;
    float randY = rand.nextInt(200) * -100 + y;
    Point p = new Point(randX, randY, z, w);
    p.home = new Vec3D(p.x, p.y, 0);
    return p;
	}

	@Override
	public void draw(PGraphics g)
	{
		g.pushMatrix();
		g.strokeWeight(10);
		g.stroke(0, 255, 0);
		g.point(vars.position.x,  vars.position.y, vars.position.z);
		g.popMatrix();
		
		for(Point p : children)
		{
			g.pushMatrix();
			g.translate(p.x,  p.y, p.z);
			g.rotateX(-deg60);
//  	g.rotateZ(deg90);
    	g.image(texture, 0,  0,  60,  60);
			g.popMatrix();
//			g.strokeWeight(4);
//			g.stroke(255);
//			g.point(p.x, p.y, p.z);			
		}	
	}

}
