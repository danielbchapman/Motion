package com.danielbchapman.physics.toxiclibs;

import java.util.function.Consumer;

import lombok.Data;
import toxi.geom.Vec3D;

import com.danielbchapman.text.Utility;


/**
 * The PersistentVariables class is a collection of 
 * common values that can be easily serialized and copied by the 
 * user interface. 
 * 
 * Using these values will allow the engine to easily save your brush to 
 * a text file and restore it using the interfaces.
 * 
 * Note: this is a primitive start and should be replaced by 
 * something far more dynamic.
 */
@Data
public class PersistentVariables implements Cloneable
{
  public static class Fields
  {
    public final static String POS_X = "pos_x";
    public final static String POS_Y = "pos_y";
    public final static String POS_Z = "pos_z";
    public final static String FOR_X = "for_x";
    public final static String FOR_Y = "for_y";
    public final static String FOR_Z = "for_z";
    public final static String BAK_X = "bak_x";
    public final static String BAK_Y = "bak_y";
    public final static String BAK_Z = "bak_z";
    public final static String SCL_X = "scl_x";
    public final static String SCL_Y = "scl_y";
    public final static String SCL_Z = "scl_z";
    public final static String MAGNITUDE = "magnitude";
    public final static String MAGNITUDE_MIN = "magnitude_min";
    public final static String MAGNITUDE_MAX = "magnitude_max";
    
    public final static String RADIUS = "radius";
    public final static String RADIUS_MIN = "radius_min";
    public final static String RADIUS_MAX = "radius_max";
    
    public final static String USER_A = "userA";
    public final static String USER_A_MIN = "userA_min";
    public final static String USER_A_MAX = "userA_max";
    
    public final static String USER_B = "userB";
    public final static String USER_B_MIN = "userB_min";
    public final static String USER_B_MAX = "userB_max";
    
    public final static String USER_C = "userC";
    public final static String USER_C_MIN = "userC_min";
    public final static String USER_C_MAX = "userC_max";
    
    public final static String MAX_FORCE = "maxForce";
    public final static String MAX_FORCE_MIN = "maxForce_min";
    public final static String MAX_FORCE_MAX = "maxForce_max";
    
    public final static String MIN_FORCE = "minForce";
    public final static String MIN_FORCE_MIN = "minForce_min";
    public final static String MIN_FORCE_MAX = "minForce_max";
    
    public final static String TIME_STEP = "timeStep";
    public final static String ENABLED = "enabled";
    public final static String RUNNING = "running";
    public final static String CLASS_NAME = "className";
    public final static String PET_NAME = "petName";
    
    public final static String[] ALL_FIELDS;
    static 
    { //FIXME do we link this up? The other data min/max is meta-data for the UI
      ALL_FIELDS = new String[]
          {
          "pos_x",
          "pos_y",
          "pos_z",
          "for_x",
          "for_y",
          "for_z",
          "bak_x",
          "bak_y",
          "bak_z",
          "scl_x",
          "scl_y",
          "scl_z",
          "magnitude",
          "radius",
          "userA",
          "userB",
          "userC",
          "maxForce",
          "minForce",
          "timeStep",
          "enabled",
          "running",
          "className",
          "petName"
            };
    }
  }

  public Vec3D position = new Vec3D(0f, 0f, 0f);
  public Vec3D force = new Vec3D(1f, 1f, 1f);
  public Vec3D backup = new Vec3D(0f, 0f, 0f);
  public Vec3D scaledForce = new Vec3D(0f, 0f, 0f);
  
  public float magnitude = 0f;
  public float magnitudeMin = 0f;
  public float magnitudeMax = 10f;
  
  private float radius = 0f;
  private float radiusSquared = 0f;
  public float radiusMin = 0f;
  public float radiusMax = 0f;
  
  public float userA = 0f;
  public float userAMin = 0f;
  public float userAMax = 0f;
  
  public float userB = 0f;
  public float userBMin = 0f;
  public float userBMax = 0f;
  
  public float userC = 0f;
  public float userCMin = 0f;
  public float userCMax = 0f;
  
  public float maxForce = 0f;
  public float maxForceMin = 0f;
  public float maxForceMax = 0f;
  
  public float minForce = 0f;
  public float minForceMin = 0f;
  public float minForceMax = 0f;
  
  public float timeStep = 1f;
  public boolean enabled = true;
  public boolean running = false; //for loading/saving/do not use
  public String className = null;
  public String petName = null;

//  public static Item save(ForceVariables v, Item item)
//  {
//    if (item == null)
//      item = new Item();
//    item.setValue(Fields.POS_X, v.position.x);
//    item.setValue(Fields.POS_Y, v.position.y);
//    item.setValue(Fields.POS_Z, v.position.z);
//    
//    item.setValue(Fields.FOR_X, v.force.x);
//    item.setValue(Fields.FOR_Y, v.force.y);
//    item.setValue(Fields.FOR_Z, v.force.z);
//    
//    item.setValue(Fields.BAK_X, v.backup.x);
//    item.setValue(Fields.BAK_Y, v.backup.y);
//    item.setValue(Fields.BAK_Z, v.backup.z);
//    
//    item.setValue(Fields.SCL_X, v.scaledForce.x);
//    item.setValue(Fields.SCL_Y, v.scaledForce.y);
//    item.setValue(Fields.SCL_Z, v.scaledForce.z);
//    
//    item.setValue(Fields.MAGNITUDE, v.magnitude);
//    item.setValue(Fields.MAX_FORCE, v.maxForce);
//    item.setValue(Fields.MIN_FORCE, v.minForce);
//    item.setValue(Fields.RADIUS, v.radius);
//    item.setValue(Fields.USER_A, v.userA);
//    item.setValue(Fields.USER_B, v.userB);
//    item.setValue(Fields.USER_C, v.userC);
//    item.setValue(Fields.TIME_STEP, v.timeStep);
//    item.setValue(Fields.ENABLED, v.enabled);
//    return item;
//  }

//  public static ForceVariables load(Item i)
//  {
//    if(i == null)
//      return new ForceVariables();
//    
//    ForceVariables vars = new ForceVariables();
//    vars.force.x = i.decimal(Fields.FOR_X).floatValue();
//    vars.force.y = i.decimal(Fields.FOR_Y).floatValue();
//    vars.force.z = i.decimal(Fields.FOR_Z).floatValue();
//    
//    vars.position.x = i.decimal(Fields.POS_X).floatValue();
//    vars.position.y = i.decimal(Fields.POS_Y).floatValue();
//    vars.position.z = i.decimal(Fields.POS_Z).floatValue();
//    
//    vars.backup.x = i.decimal(Fields.BAK_X).floatValue();
//    vars.backup.y = i.decimal(Fields.BAK_Y).floatValue();
//    vars.backup.z = i.decimal(Fields.BAK_Z).floatValue();
//    
//    vars.scaledForce.x = i.decimal(Fields.SCL_X).floatValue();
//    vars.scaledForce.y = i.decimal(Fields.SCL_Y).floatValue();
//    vars.scaledForce.z = i.decimal(Fields.SCL_Z).floatValue();
//    
//    vars.radius = i.decimal(Fields.RADIUS).floatValue();
//    vars.magnitude = i.decimal(Fields.MAGNITUDE).floatValue();
//    vars.maxForce = i.decimal(Fields.MAX_FORCE).floatValue();
//    vars.minForce = i.decimal(Fields.MIN_FORCE).floatValue();
//    vars.userA = i.decimal(Fields.USER_A).floatValue();
//    vars.userB = i.decimal(Fields.USER_B).floatValue();
//    vars.userC = i.decimal(Fields.USER_C).floatValue();
//    vars.timeStep = i.decimal(Fields.TIME_STEP).floatValue();
//    vars.enabled = i.bool(Fields.ENABLED);
//    vars.running = i.bool(Fields.RUNNING);
//    
//    return vars;
//  }
  
  public static PersistentVariables fromLine(String line)
  {
    PersistentVariables v = new PersistentVariables();
    String[] values = line.split(",");//CSV
    int i = 0;
//    if(values.length != 15)//format error
//      throw new RuntimeException("Unable to parse ForceVariables");
//    
    v.position.x = Float.parseFloat(values[i++]);
    v.position.y = Float.parseFloat(values[i++]);
    v.position.z = Float.parseFloat(values[i++]);
    
    v.force.x = Float.parseFloat(values[i++]);
    v.force.y = Float.parseFloat(values[i++]);
    v.force.z = Float.parseFloat(values[i++]);
    
    v.backup.x = Float.parseFloat(values[i++]);
    v.backup.y = Float.parseFloat(values[i++]);
    v.backup.z = Float.parseFloat(values[i++]);
    
    v.scaledForce.x = Float.parseFloat(values[i++]);
    v.scaledForce.y = Float.parseFloat(values[i++]);
    v.scaledForce.z = Float.parseFloat(values[i++]);
    
    v.magnitude = Float.parseFloat(values[i++]);
    v.magnitudeMin = Float.parseFloat(values[i++]);
    v.magnitudeMax = Float.parseFloat(values[i++]);
    
    v.maxForce = Float.parseFloat(values[i++]);
    v.maxForceMin = Float.parseFloat(values[i++]);
    v.maxForceMax = Float.parseFloat(values[i++]);
    
    v.minForce = Float.parseFloat(values[i++]);
    v.minForceMin = Float.parseFloat(values[i++]);
    v.minForceMax = Float.parseFloat(values[i++]);
    
    v.setRadius(Float.parseFloat(values[i++]));
    v.radiusMin = Float.parseFloat(values[i++]);
    v.radiusMax = Float.parseFloat(values[i++]);
    
    v.userA = Float.parseFloat(values[i++]);
    v.userAMin = Float.parseFloat(values[i++]);
    v.userAMax = Float.parseFloat(values[i++]);
    
    v.userB = Float.parseFloat(values[i++]);
    v.userBMin = Float.parseFloat(values[i++]);
    v.userBMax = Float.parseFloat(values[i++]);
    
    v.userC = Float.parseFloat(values[i++]);
    v.userCMin = Float.parseFloat(values[i++]);
    v.userCMax = Float.parseFloat(values[i++]);
    
    v.timeStep = Float.parseFloat(values[i++]);
    v.enabled = Boolean.parseBoolean(values[i++]);
    v.running = Boolean.parseBoolean(values[i++]);
    v.petName = values[i++];
    
    return v;
  }
  public static String toLine(PersistentVariables v)
  {
    StringBuilder b = new StringBuilder();
    Consumer<Object> add = (x)->
    {
      b.append(x);
      b.append(",");
    };
    add.accept(v.position.x);
    add.accept(v.position.y);
    add.accept(v.position.z);
    
    add.accept(v.force.x);
    add.accept(v.force.y);
    add.accept(v.force.z);
    
    add.accept(v.backup.x);
    add.accept(v.backup.y);
    add.accept(v.backup.z);
    
    add.accept(v.scaledForce.x);
    add.accept(v.scaledForce.y);
    add.accept(v.scaledForce.z);
    
    add.accept(v.magnitude);
    add.accept(v.magnitudeMin);
    add.accept(v.magnitudeMax);
    
    add.accept(v.maxForce);
    add.accept(v.maxForceMin);
    add.accept(v.maxForceMax);
    
    add.accept(v.minForce);
    add.accept(v.minForceMin);
    add.accept(v.minForceMax);
    
    add.accept(v.radius);
    add.accept(v.radiusMin);
    add.accept(v.radiusMax);
    
    add.accept(v.userA);
    add.accept(v.userAMin);
    add.accept(v.userAMax);
    
    add.accept(v.userB);
    add.accept(v.userBMin);
    add.accept(v.userBMax);
    
    add.accept(v.userC);
    add.accept(v.userCMin);
    add.accept(v.userCMax);
    
    add.accept(v.timeStep);
    add.accept(v.enabled);
    add.accept(v.running);
    add.accept(Utility.isEmptyOrNull(v.petName) ? null : v.petName);
    
    b.append("END_VALUES");
       
    return b.toString();
  }
  
  @Override
  public PersistentVariables clone()
  {
    System.err.println("[WARN]: This implemention is slow and needs to be implemented property");
    return PersistentVariables.fromLine(PersistentVariables.toLine(this));
  }
  
  /**
   *
   * @return a copy of this  
   * 
   */
  public PersistentVariables copy()
  {
    return clone();
  }
  
  public void setRadius(float r)
  {
    radius = r;
    radiusSquared = r * r;
  }
}
