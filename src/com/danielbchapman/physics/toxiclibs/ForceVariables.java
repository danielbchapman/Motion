package com.danielbchapman.physics.toxiclibs;

import lombok.Data;
import toxi.geom.Vec3D;

import com.danielbchapman.groups.Item;

@Data
public class ForceVariables
{
  public class Fields
  {
    public final static String POS_X = "pos_x";
    public final static String POS_Y = "pos_y";
    public final static String POS_Z = "pos_z";
    public final static String DIR_X = "mag_x";
    public final static String DIR_Y = "mag_y";
    public final static String DIR_Z = "mag_z";
    public final static String FORCE = "force";
    public final static String RADIUS = "radius";
    public final static String USER_A = "userA";
    public final static String USER_B = "userB";
    public final static String USER_C = "userC";
    public final static String MAX_FORCE = "maxForce";
    public final static String MIN_FORCE = "minForce";
    public final static String TIME_STEP = "timeStep";
  }

  public Vec3D position = new Vec3D(0f, 0f, 0f);
  public Vec3D direction = new Vec3D(1f, 1f, 1f);
  public float force = 0f;
  public float radius = 0f;
  public float userA = 0f;
  public float userB = 0f;
  public float userC = 0f;
  public float maxForce = 0f;
  public float minForce = 0f;
  public float timeStep = 1f;

  public static Item save(ForceVariables v, Item item)
  {
    if (item == null)
      item = new Item();
    item.setValue(Fields.POS_X, v.position.x);
    item.setValue(Fields.POS_Y, v.position.y);
    item.setValue(Fields.POS_Z, v.position.z);
    item.setValue(Fields.DIR_X, v.direction.x);
    item.setValue(Fields.DIR_Y, v.direction.y);
    item.setValue(Fields.DIR_Z, v.direction.z);
    item.setValue(Fields.FORCE, v.force);
    item.setValue(Fields.MAX_FORCE, v.maxForce);
    item.setValue(Fields.MIN_FORCE, v.minForce);
    item.setValue(Fields.RADIUS, v.radius);
    item.setValue(Fields.USER_A, v.userA);
    item.setValue(Fields.USER_B, v.userB);
    item.setValue(Fields.USER_C, v.userC);
    item.setValue(Fields.TIME_STEP, v.timeStep);
    return item;
  }

  public static ForceVariables load(Item i)
  {
    if(i == null)
      return new ForceVariables();
    
    ForceVariables vars = new ForceVariables();
    vars.direction.x = i.decimal(Fields.DIR_X).floatValue();
    vars.direction.y = i.decimal(Fields.DIR_Y).floatValue();
    vars.direction.z = i.decimal(Fields.DIR_Z).floatValue();
    
    vars.position.x = i.decimal(Fields.POS_X).floatValue();
    vars.position.y = i.decimal(Fields.POS_Y).floatValue();
    vars.position.z = i.decimal(Fields.POS_Z).floatValue();
    
    vars.radius = i.decimal(Fields.RADIUS).floatValue();
    vars.force = i.decimal(Fields.FORCE).floatValue();
    vars.maxForce = i.decimal(Fields.MAX_FORCE).floatValue();
    vars.minForce = i.decimal(Fields.MIN_FORCE).floatValue();
    vars.userA = i.decimal(Fields.USER_A).floatValue();
    vars.userB = i.decimal(Fields.USER_B).floatValue();
    vars.userC = i.decimal(Fields.USER_C).floatValue();
    vars.timeStep = i.decimal(Fields.TIME_STEP).floatValue();
    
    return vars;
  }
}
