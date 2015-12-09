package com.danielbchapman.physics.toxiclibs;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import toxi.geom.Vec3D;
import toxi.physics3d.VerletParticle3D;

/**
 * An attraction behavior that falls off over a larger radius. This is significantly
 * more intensive than the regular behavior as it will apply to each particle in the 
 * system.
 *
 */

public class FalloffAttractionBehavior extends MotionInteractiveBehavior
{
  static final Map<String, String> FIELD_NAMES;
  static {
    HashMap<String, String> m = new HashMap<>();
    m.put(PersistentVariables.Fields.MAGNITUDE, "Magniture");
    m.put(PersistentVariables.Fields.MAX_FORCE, "Max Force");
    m.put(PersistentVariables.Fields.MIN_FORCE, "Min Force");
    m.put(PersistentVariables.Fields.POS_X, "Position");
    m.put(PersistentVariables.Fields.POS_Y, "Position");
    m.put(PersistentVariables.Fields.POS_Z, "Position");
    m.put(PersistentVariables.Fields.FOR_X, "Force");
    m.put(PersistentVariables.Fields.FOR_Y, "Force");
    m.put(PersistentVariables.Fields.FOR_Z, "Force");
    
    FIELD_NAMES = Collections.unmodifiableMap(m);
  }

  public FalloffAttractionBehavior()
  {
    this(new Vec3D(), 10f, 10f, 0f);
  }
  public FalloffAttractionBehavior(Vec3D attractor, float radius, float strength, float jitter)
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
  public void apply(VerletParticle3D p)
  {
    Vec3D delta = null;
    if(vars.magnitude < 0)
      delta = p.sub(vars.position);
    else
      delta = vars.position.sub(p);
    float dist = delta.magSquared();
    
    //check to see if it is inside the sticky radius--doesn't help a "push"
    if(vars.magnitude > 0)
    {
      //inside the radius
      if(dist < vars.getRadiusSquared())
        return; 
    }
    
    float modifier = 0f;
    if(vars.magnitude < 0)
      modifier = vars.magnitude / dist * -1f;
    else
      modifier = vars.magnitude / dist;
    
    if(modifier > vars.maxForce)
      modifier = vars.maxForce;
    
    if(modifier < vars.minForce)
      return;

    Vec3D force = delta.normalizeTo(modifier);
    p.addForce(force);
  }

  @Override
  public void configure(float timeStep)
  {
    vars.timeStep = timeStep;
  }

  @Override
  public Map<String, String> getFieldNames()
  {
     return FIELD_NAMES;
  }

  @Override
  public String getName()
  {
    return "Falloff Attraction Behavior";
  }

  @Override
  public void setPosition(Vec3D location)
  {
    vars.position.x = location.x;
    vars.position.y = location.y;
    vars.position.z = location.z;
  }

  @Override
  public MotionInteractiveBehavior copy()
  {
    ExplodeBehavior copy = new ExplodeBehavior();
    copy.vars = vars.copy();
    
    return copy;
  }

  public void setJitter(float f)
  {
    vars.userA = f;
  }

}
