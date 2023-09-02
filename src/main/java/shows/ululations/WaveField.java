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
	
	//SINE
	public float sinPercent;
	public float sinRatePeriod = 6000;
	public float sinScalar = 1.0f;
	public float sinPeriod = 10.0f;
	
	//COSINE
	public float cosPercent;
	public float cosRatePeriod = 10000;
	public float cosScalar = 4.0f;
	public float cosPeriod = 12.0f;
	
	public void update(long time)
	{
		if(startTime < 0) {
			startTime = time;
		}
		animationX += animationOffsetX;
		animationY += animationOffsetY;		
		interpolate = ((float) (currentTime - startTime)) / (float) period;
		
		Motion.MOTION.noiseDetail(detailOctaves, detail);
		
		//SINE
		sinPercent = ((float)(startTime - time)) / sinRatePeriod;
//		System.out.println("X: " + animationX + " Y:" + animationY);
	}
	
	@Override
	public void apply(VerletParticle3D p)
	{

		if(p.z > 100)
			return;

		float noiseFalloff = PApplet.lerp(10f, 0f, p.z / maxZ);
		//float noise = Motion.MOTION.noise(p.x + animationX, p.y);
//		float forceZ = Math.sin(p.x)
		//p.addForce(new Vec3D(0,0, noise * noiseFalloff * scalar));
		//System.out.println("Scalar: " + scalar + " noise: " + noise + " animationX:" + animationX);
		float delta = (float) (Math.sin(p.x / sinPeriod + sinPercent * 10f) * Math.PI / 180f) * sinScalar;
		float cosDelta = (float) (Math.cos(p.y / cosPeriod + cosPercent * 10f) * Math.PI / 180f) * cosScalar;
		//p.addForce(new Vec3D(0,0, noise * noiseFalloff * scalar));
		p.addForce(new Vec3D(0, 0 , (delta * sinScalar) + (cosDelta * cosScalar)));
		//System.out.println("delta-" + delta + " p:" + sinPercent);
	}

	@Override
	public void configure(float arg0)
	{
	}

}
