package com.danielbchapman.physics.toxiclibs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Vec3
{
  float x = 1f;
  float y = 1f;
  float z = 1f;
  
  public Vec3(Vec3 vec)
  {
    this.x = vec.x;
    this.y = vec.y;
    this.z = vec.z;
  }
}
