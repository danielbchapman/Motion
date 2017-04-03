package com.danielbchapman.motion.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.danielbchapman.physics.toxiclibs.PersistentVariables;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import processing.core.PGraphics;


/**
 * <p>
 * A class that acts as a paint-brush in the virtual world. This
 * class maps all the data for sub-classes which can then extend
 * and apply their own variables.
 * </p>
 * 
 * <p>
 * The motion brush class models a paint-brush that is loaded with 
 * some sort of paint. There are variables that can be set to affect
 * whether this paint is infinite, scales over distance, changes size,
 * and so on. These variables will be calculated internally and can be 
 * applied to physical forces as well and the calculations are applied 
 * inside the subclasses of the brush.
 * </p>
 */
@Data
public abstract class MotionBrush implements ICloneable<MotionBrush>, ISaveable<MotionBrush>
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
    m.put(PersistentVariables.Fields.USER_A, "UserA");
    m.put(PersistentVariables.Fields.USER_B, "UserB");
    m.put(PersistentVariables.Fields.USER_C, "UserC");
    
    FIELD_NAMES = Collections.unmodifiableMap(m);
  }
  
  @Getter
  @Setter
  public PersistentVariables vars = new PersistentVariables();
  
  //Vector Methods
  private boolean down;
  private int system = -1;
  public int framesDrawn = 0;
  /**
   * The distance to force a redraw on this point 
   */
  public float splitSize = 2f;
  MotionMouseEvent last = null;
  long startTime;
  
  public boolean isVectorBrush()
  {
    return false;
  }
  
  public boolean applyWhenIdle()
  {
    return false;
  }
  
  public abstract void applyBrush(PGraphics g, MotionMouseEvent point);
  
  /**
   * An update method called prior to any drawing methods should
   * it be needed.
   */
  public abstract void update(long time);
 
  public boolean checkActive(MotionMouseEvent m)
  {
    return m.left || m.right || m.center;
  }
  
  public boolean isDown()
  {
    return this.down;
  }
  
  public void setDown(boolean val, MotionMouseEvent e)
  {
    if(val == false)
    {
      framesDrawn = 0;
      last = null;
    }
    
    this.down = val;
    this.last = e;
    this.startTime = System.currentTimeMillis();
  }
  
  public abstract MotionBrush deepCopy();
  
  @Override
  public MotionBrush clone()
  {
    MotionBrush copy = deepCopy();
    copy.down = down;
    copy.last = last == null ? null : last.copy();
    copy.framesDrawn = framesDrawn;
    copy.splitSize = splitSize;
    copy.startTime = startTime;
    copy.system = system;
    return copy;
  }
  
  /**
   * <p>
   * Returns a listing of all the field names in the order
   * they are presented in ForceVariables.Fields so that
   * </p>
   * 
   * <p>Returning null for a field indicates it should be hidden from the UI</p> 
   * <p>Returning null for the map indicates everything should be displayed</p>
   * @return a Map giving usable names.
   * 
   */
  public Map<String, String> getFieldNames()
  {
    return FIELD_NAMES;
  }
  
  /**
   * @return the name for this class, null for the class name 
   */
  public String getName()
  {
    return this.getClass().getName();
  }
  
  
//  
//  /* (non-Javadoc)
//   * @see com.danielbchapman.motion.core.ISaveable#save()
//   */
//  public String save()
//  {
//    Gson out = new Gson();
//    String json = out.toJson(this, getClass());
//    return json;
//  }
//  
//  public MotionBrush load(String data)
//  {
//    Gson in = new Gson();
//    MotionBrush brush = in.fromJson(data, getClass());
//    return brush;
//  }
}
