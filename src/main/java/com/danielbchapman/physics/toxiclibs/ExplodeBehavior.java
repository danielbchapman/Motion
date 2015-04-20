package com.danielbchapman.physics.toxiclibs;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import toxi.geom.Vec3D;
import toxi.physics3d.VerletParticle3D;
import toxi.physics3d.behaviors.ParticleBehavior3D;


/**
 * The ExplodeBehavior class provides a perpendicular force as well as as an opposite
 * for that causes the points to be pushed away in a spherical shape.
 *
 */
public class ExplodeBehavior extends MotionInteractiveBehavior
{
  static final Map<String, String> FIELD_NAMES;
  static {
    HashMap<String, String> m = new HashMap<>();
    m.put(ForceVariables.Fields.MAGNITUDE, "Magniture");
    m.put(ForceVariables.Fields.MAX_FORCE, "Max Force");
    m.put(ForceVariables.Fields.MIN_FORCE, "Min Force");
    m.put(ForceVariables.Fields.POS_X, "Position");
    m.put(ForceVariables.Fields.POS_Y, "Position");
    m.put(ForceVariables.Fields.POS_Z, "Position");
    m.put(ForceVariables.Fields.FOR_X, "Force");
    m.put(ForceVariables.Fields.FOR_Y, "Force");
    m.put(ForceVariables.Fields.FOR_Z, "Force");
    
    FIELD_NAMES = Collections.unmodifiableMap(m);
  }
//  protected float force;
//  protected float timeStep;
//  protected float maxForce = 10f;
//  protected float minForce = 0.1f;
//  protected Vec3D location;
//  protected Vec3D direction;

  public ExplodeBehavior()
  {
    this(new Vec3D(), 0f);
  }
  
  public ExplodeBehavior(Vec3D direction, float magnitude)
  {
    vars.force = direction;
    vars.magnitude = magnitude;
    vars.maxForce = 10f;
    vars.minForce = 0.1f;
  }
  
  @Override
  public void apply(VerletParticle3D p)
  {
    Vec3D distanceV = p.sub(vars.position);
    float distance = distanceV.magnitude();
    float modifier = vars.magnitude / distance;// / mag; // 1 / n^2
    
    if(modifier > vars.maxForce)
      modifier =  vars.maxForce;
    
    if(modifier <  vars.minForce)
      return;//skip
    //noise

    //Away and in the direction 
    Vec3D vForce =  vars.force.normalizeTo(modifier);
    distanceV = distanceV.normalizeTo(modifier);  
    vForce = vForce.add(distanceV);
    if(p instanceof Point)
    {
      ((Point)p).addAngularForce(vForce.scale(5f));
    }
    p.addForce(vForce);
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
    return "Explode Behavior";
  }

}
