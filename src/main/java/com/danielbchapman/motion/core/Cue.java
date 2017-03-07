package com.danielbchapman.motion.core;

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
  private String typeName;
  private String id;
  private String label;
  
  //Paths and files that are commonly needed
  private String pathFile;
  private String brushFile;
  private String environmentFile;
  
  private Vec4D position = new Vec4D();
  private Vec4D scale = new Vec4D();
  private Vec3D anchor = new Vec3D();
  
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
  
  public abstract void load();
  public abstract void start();
  public abstract void stop();
  public abstract void pause();
  public abstract void update(long time);
}
