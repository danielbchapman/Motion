package shows.test;

import com.danielbchapman.motion.core.IPhysicsBrush;
import com.danielbchapman.motion.core.MotionBrush;
import com.danielbchapman.motion.core.MotionMouseEvent;
import com.danielbchapman.motion.core.PhysicsBrush;
import com.danielbchapman.physics.toxiclibs.PersistentVariables;
import com.danielbchapman.physics.toxiclibs.Point;

import processing.core.PGraphics;
import toxi.geom.Vec3D;
import toxi.physics3d.VerletParticle3D;

public class TestFalloffAttractorBrush extends PhysicsBrush
{

  public TestFalloffAttractorBrush()
  {
    this(new Vec3D(), 100f, 100f, 0f);
  }
  
  
  public TestFalloffAttractorBrush(Vec3D attractor, float radius, float strength, float jitter)
  {
    vars.magnitudeMax = 200f;
    vars.magnitudeMin = 0f;
    vars.maxForceMax = 25f;
    vars.maxForceMin = 0f;
    
    vars.position = attractor;
    vars.setRadius(radius);
    vars.magnitude = strength;
    vars.userA = jitter;
    vars.minForce = 0.1f;
    vars.maxForce = 2f;
  }

  @Override
  public Vec3D getPosition()
  {
    return this.vars.position;
  }

  @Override
  public void apply(VerletParticle3D p)
  {
    //System.out.println("applying brush=> " + p);
    Vec3D delta = null;
    if (vars.magnitude < 0)
      delta = p.sub(vars.position);
    else
      delta = vars.position.sub(p);
    float dist = delta.magSquared();

    // check to see if it is inside the sticky radius--doesn't help a "push"
    if (vars.magnitude > 0)
    {
      // inside the radius
      if (dist > vars.getRadiusSquared())
        return;
    }

    float modifier = 0f;
    if (vars.magnitude < 0)
      modifier = vars.magnitude / dist * -1f;
    else
      modifier = vars.magnitude / dist;

    if (modifier > vars.maxForce)
      modifier = vars.maxForce;

    if (modifier < vars.minForce)
      return;

    Vec3D force = delta.normalizeTo(modifier);
    p.addForce(force);
  }

  @Override
  public void applyBrush(PGraphics g, MotionMouseEvent point)
  {
    this.setPosition(new Vec3D(point));
  }

  @Override
  public void update(long time)
  {
  }

  public void setJitter(float f)
  {
    vars.userA = f;
  }
}
