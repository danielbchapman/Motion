package com.danielbchapman.motion.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.danielbchapman.physics.toxiclibs.PersistentVariables;

import lombok.Getter;
import lombok.Setter;
import toxi.geom.Vec3D;

public abstract class PhysicsBrush extends MotionBrush implements IPhysicsBrush
{
  static final Map<String, String> FIELD_NAMES;
  static
  {
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
  // protected float force;
  // protected float timeStep;
  // protected float maxForce = 10f;
  // protected float minForce = 0.1f;
  // protected Vec3D location;
  // protected Vec3D direction;

  @Override
  public void configure(float timeStep)
  {
    vars.timeStep = timeStep;
  }

  @Override
  public void setPosition(Vec3D location)
  {
    vars.position.x = location.x;
    vars.position.y = location.y;
    vars.position.z = location.z;
  }

  @Override
  public MotionBrush deepCopy()
  {
    try
    {
      Class<? extends PhysicsBrush> clazz = this.getClass();
      PhysicsBrush copy = clazz.newInstance();
      copy.vars = this.vars.copy();
      return copy;
    }
    catch (InstantiationException | IllegalAccessException e)
    {
      throw new RuntimeException(e.getMessage(), e);
    }
  };
}
