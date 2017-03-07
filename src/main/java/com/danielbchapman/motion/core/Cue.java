package com.danielbchapman.motion.core;

import processing.core.PGraphics;
import toxi.geom.Vec3D;
import toxi.geom.Vec4D;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.google.gson.Gson;


/**
 * All child classes need to store variables in the Cue class or they
 * must override the save/load defaults in @see {ISaveable} as GSON will
 * not know how to load them. 
 * 
 * The type is stored as the class on construction.
 */
@Data
public abstract class Cue<T> implements ISaveable<T>
{
  public String typeName;
  public String id;
  public String label;
  
  //Paths and files that are commonly needed
  public String pathFile;
  public String brushFile;
  public String environmentFile;
  
  public Vec4D position = new Vec4D();
  public Vec4D scale = new Vec4D(1f, 1f, 1f, 1f);
  public Vec3D anchor = new Vec3D();
  public Vec3D size = new Vec3D();
  
  //Transients (do not serialize)
  public transient boolean editing;
  public transient boolean loaded = false;
  public transient boolean inError = false;
  public transient boolean running = false;  
  public transient long startTime;
  public transient long length;
  
  public Cue()
  {
    this.typeName = getClass().getName();
  }
  
  /**
   * <p>Left click and drag changes the position of the drawing.</p>
   * <em>Override for custom functionality.</em> 
   * @param deltaX the change in x
   * @param deltaY the change in y  
   */
  public void handleEditLeft(float deltaX, float deltaY)
  {
    this.position.x += deltaX;
    this.position.y += deltaY;
    Log.severe("[handleEditLeft] @(" + deltaX + ", " + deltaY + ")");
  }
  
  /**
   * <p>Left click and drag changes the position of the drawing.</p>
   * <em>Override for custom functionality.</em> 
   * @param deltaX the change in x
   * @param deltaY the change in y   
   */
  public void handleEditRight(float deltaX, float deltaY)
  {
    Log.severe("[handleEditRight] @(" + deltaX + ", " + deltaY + ")");
    
    //We're only using deltaY;
    if(deltaY < 1 && deltaY > -1)
      return;
    
    boolean shrink = deltaY > 0; //Coordinates are backwards...
    float abs = Math.abs(deltaY);
    float sizeY = size.y < 1 ? 1 : size.y;
    float percent = abs / sizeY;
    
    if(percent >= 1 && !shrink){
      scale.x = scale.x + percent;
      scale.y = size.y + percent;    
    }
    else if (percent > 1 && shrink)
    {
      return; //Nope...      
    }
    else if(percent <= 0)
    {
      return; //Nope
    } 
    else 
    {
      float scalar = 1f;
      if(shrink)
      {
        scalar = -percent;
      }
      else
      {//expand
        scalar = percent;
      }
      
      scale.x = scale.x + scalar;
      scale.y = scale.y + scalar;
      
      if(scale.x < 0)
        scale.x = 0;
      if(scale.y < 0)
        scale.y = 0;
      Log.severe("[handleEditRight] SCALAR( + " + scalar + ")");
    }
  }
  
  /**
   * <p>Center click and drag changes the scale of the drawing 
   * and is unbound meaning it will use both values. Right click
   * uses only the greater value and scales symmetrically. 
   * </p>
   * 
   * <em>Override for custom functionality.</em>
   *  
   * @param deltaX the change in x
   * @param deltaY the change in y   
   */
  public void handleEditCenter(float deltaX, float deltaY)
  {
    Log.severe("[handleEditCenter] @(" + deltaX + ", " + deltaY + ")");
  }
  
  public abstract void load(Motion motion);
  public abstract void start(Motion motion, long time);
  public abstract void update(Motion motion, long time);
  public abstract void preview(PGraphics g, Motion motion, long time);
}
