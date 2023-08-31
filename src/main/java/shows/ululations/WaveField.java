package shows.ululations;

import com.danielbchapman.motion.core.Motion;

import processing.core.PApplet;
import toxi.geom.Vec3D;
import toxi.physics3d.VerletParticle3D;
import toxi.physics3d.behaviors.ParticleBehavior3D; 

public class WaveField implements ParticleBehavior3D 
{
	public long startTime = -1;
	public long currentTime = -1;
	public long period = 1000;
	public float interpolate = 0f;
	
	public float animationOffsetX = 0.01f;//0.0005f;
	public float animationOffsetY = 0.01f;//0.0003f;
	public float animationX = 0;
	public float animationY = 0;
	
	//SCALARS
	public float maxZ = 75f;
	public int detailOctaves = 4;
	public float detail = 0.65f;
	public float scalar = 0.1f;
	
	public void update(long time)
	{
		if(startTime < 0) {
			startTime = time;
		}
		animationX += animationOffsetX;
		animationY += animationOffsetY;		
		interpolate = ((float) (currentTime - startTime)) / (float) period;
		
		Motion.MOTION.noiseDetail(detailOctaves, detail);
//		System.out.println("X: " + animationX + " Y:" + animationY);
	}
	
	@Override
	public void apply(VerletParticle3D p)
	{
		//Shelf
		if(p.z > 100)
			return;

		float noiseFalloff = PApplet.lerp(10f, 0f, p.z / maxZ);
		float noise = Motion.MOTION.noise(p.x + animationX, p.y);
		//float forceZ = Math.sin(p.x)
		p.addForce(new Vec3D(0,0, noise * noiseFalloff * scalar));
		//System.out.println("Scalar: " + scalar + " noise: " + noise + " animationX:" + animationX);
		// TODO Auto-generated method stub

	}

	@Override
	public void configure(float arg0)
	{
	}

}
