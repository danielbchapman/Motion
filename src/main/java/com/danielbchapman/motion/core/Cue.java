package com.danielbchapman.motion.core;

import toxi.geom.Vec3D;
import toxi.geom.Vec4D;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.google.gson.Gson;

@Data
@NoArgsConstructor
public class Cue implements ISaveable<Cue>
{
  private String id;
  private String label;
  private String file;
  
  private Vec4D position = new Vec4D();
  private Vec4D scale = new Vec4D();
  private Vec3D anchor = new Vec3D();
  
  //private Geometry scale;
  //private Geometry anchor;
  
//  @Override
//  public String save()
//  {
//    Gson gson = new Gson();
//    String ret = gson.toJson(this, Cue.class);
//    return ret;
//  }
//
//  @Override
//  public Cue load(String data)
//  {
//    Cue cue = new Gson().fromJson(data, Cue.class);
//    return cue;
//  }
}
