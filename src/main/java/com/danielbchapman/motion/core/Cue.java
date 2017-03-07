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
  public Vec4D scale = new Vec4D();
  public Vec3D anchor = new Vec3D();
  
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
  
  public abstract void load(Motion motion);
  public abstract void start(Motion motion, long time);
  public abstract void update(Motion motion, long time);
}
