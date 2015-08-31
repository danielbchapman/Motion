package com.danielbchapman.physics.toxiclibs;

import java.io.Serializable;

import lombok.Data;
import toxi.physics3d.behaviors.ParticleBehavior3D;

@Data
public class MotionBrush implements Serializable
{
  private static final long serialVersionUID = 1L;
  public class Fields
  {
    public final static String BEHAVIOR_CLASS = "behaviorClass";
    public final static String BRUSH_ID = "brush_id";
  }
  public class Type
  {
    public final static String EXPLODE = "explode";
    public final static String PINCH = "pinch";
    public final static String ATTRACTOR = "TMP";
    public final static String FALL_OFF_ATTRACTOR = "TMP";
  }
    
  public String brushName;
  ForceVariables variables = new ForceVariables();
  public Class<? extends ParticleBehavior3D> behaviorClass; 
  
//  public Item save(Item item)
//  {
//    if(item == null)
//      item = new Item();
//    
//    ForceVariables.save(variables, item);
//    item.setValue(Fields.BEHAVIOR_CLASS, behaviorClass.getName());
//    item.setValue(Fields.BRUSH_ID, brushName);
//    
//    return item;
//  }
  
//  @SuppressWarnings("unchecked")
//  public static MotionBrush load(Item i) throws ClassNotFoundException
//  {
//    MotionBrush brush = new MotionBrush();
//    String name = i.string(Fields.BEHAVIOR_CLASS);
//    String id = i.string(Fields.BRUSH_ID);
//    brush.variables = ForceVariables.load(i);
//    Class x = ExplodeBehavior.class.getClassLoader().loadClass(name);
//    brush.setBehaviorClass((Class<? extends ParticleBehavior3D>) x); 
//    
//    return brush;
//  }
}
