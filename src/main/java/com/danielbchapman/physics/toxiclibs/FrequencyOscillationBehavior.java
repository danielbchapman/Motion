package com.danielbchapman.physics.toxiclibs;

import lombok.Data;
import toxi.geom.Vec3D;
import toxi.math.waves.SineWave;
import toxi.physics3d.VerletParticle3D;
import toxi.physics3d.behaviors.ParticleBehavior3D;


/**
 * An oscillator that works over the X Axis
 */
@Data
public class FrequencyOscillationBehavior extends SineWave implements ParticleBehavior3D
{
  ForceVariables vars = new ForceVariables();
  boolean enabled = false;
  long start = System.currentTimeMillis();
  float offset = 0L;
  float val2 = 0f;
  public FrequencyOscillationBehavior()
  {
    super(1f, hertzToRadians(20f, 1f));
    vars.force = new Vec3D(0, 0, 0.1f);
  }
  
  @Override
  public void apply(VerletParticle3D p)
  {
    //Apply magnitude 
    Vec3D force = vars.force.copy();
    //float off = offset+(p.x/100f);
    float off = (offset + p.x) / 100f;
    float forceBasedOnX = (float) Math.sin((double)off);//use X
    force.normalizeTo(0.1f * forceBasedOnX);
    p.addForce(force);
  }

  @Override
  public void configure(float timeStep)
  {
    vars.timeStep = timeStep;
  }
  
  @Override
  public float update()
  {
    long elapsed = start - System.currentTimeMillis();
    //offset = ((float)elapsed) / 1000f;
    offset++;
    //offset = ((float)(System.currentTimeMillis() - start)) / 1000f * 20f;
    return super.update();
  }

}
