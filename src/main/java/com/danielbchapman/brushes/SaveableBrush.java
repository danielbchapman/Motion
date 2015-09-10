package com.danielbchapman.brushes;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import toxi.geom.Vec3D;
import toxi.physics3d.VerletParticle3D;

import com.danielbchapman.physics.toxiclibs.MotionInteractiveBehavior;
import com.danielbchapman.physics.toxiclibs.PersistentVariables;



/**
 * A simple implementation of the String Serialize pattern that
 * loads the variables from a file or saves those variables to the
 * file.
 */
public abstract class SaveableBrush extends MotionInteractiveBehavior implements IBrush
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
  
  //Volatile variables
  @Getter
  protected boolean drawing = false;
  protected boolean firstPass = false;
  protected Vec3D lastPosition = null;
  /**
   * The variables used for storage.
   */
  protected PersistentVariables vars = new PersistentVariables();
  
  @Override
  public void apply(VerletParticle3D p)
  { 
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
  public void setPosition(Vec3D location)
  {
    this.vars.position = location;
  }


  @Override
  public Vec3D getLastPosition()
  {
    return lastPosition;
  }

  @Override
  public Vec3D getCurrentPosition()
  {
    return this.vars.position;
  }

  @Override
  public void endDraw()
  {
    drawing = false;
    firstPass = false;
    lastPosition = null;
  }

  @Override
  public void startDraw()
  {
    drawing = true;
    firstPass = false;
    lastPosition = null;
  }
}
