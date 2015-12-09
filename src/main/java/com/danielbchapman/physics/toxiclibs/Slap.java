package com.danielbchapman.physics.toxiclibs;

import java.util.Random;

import toxi.geom.Vec3D;
import toxi.physics3d.VerletParticle3D;
import toxi.physics3d.behaviors.ParticleBehavior3D;

public class Slap implements ParticleBehavior3D
{
  protected float force = 20f;
  protected float maxForce = 10f;
  protected float minForce = 0.1f;
  protected float radius = 65;
  protected float timeStep;
  protected float randomNoise = 0f;
  protected float scaleNoise = 0f;
  protected Vec3D location;
  protected Vec3D slap;
  Random noiseGen = new Random();
  public Slap(Vec3D location, Vec3D direction, float force)
  {
    this.location = location;
    this.force = force;
    slap = direction.normalize();
  }
  
  @Override
  public void apply(VerletParticle3D p)
  {
    float mag = p.sub(location).magnitude();
    float modifier = force / mag;// / mag; // 1 / n^2
    
    if(modifier > maxForce)
      modifier = maxForce;
    
    if(modifier < minForce)
      return;//skip
    //noise
    if(scaleNoise > 0){
      if(noiseGen.nextBoolean())
        modifier += noiseGen.nextFloat() * scaleNoise;
      else
        modifier -= noiseGen.nextFloat() * scaleNoise;
    }
    
    Vec3D pSlap = slap.normalizeTo(modifier);
    if(randomNoise > 0){
      pSlap.x += noiseGen.nextFloat() * randomNoise;
      pSlap.y += noiseGen.nextFloat() * randomNoise;
      pSlap.z += noiseGen.nextFloat() * randomNoise;
    }
    pSlap.scale(timeStep);
    
    p.addForce(pSlap);
    
  }
  @Override
  public void configure(float timeStep)
  {
    this.timeStep = timeStep;
  }
}
