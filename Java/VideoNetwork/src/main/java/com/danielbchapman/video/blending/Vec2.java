package com.danielbchapman.video.blending;

import java.io.Serializable;

import lombok.Data;

@Data
public class Vec2 implements Serializable
{
  private static final long serialVersionUID = 1L;
  public int x;
  public int y;
  
  public Vec2()
  {
  }
  
  public Vec2(int x, int y)
  {
    this.x = x;
    this.y = y;
  }
  
  public Vec2 clone()
  {
    return new Vec2(x, y);
  }
  
  public Vec2 sub(Vec2 other)
  {
    return new Vec2(x - other.x, y - other.y);
  }
  
  public Vec2 add(Vec2 other)
  {
    return new Vec2(x + other.x, y + other.y);
  }
  
  public Vec2 mult(float f)
  {
    return mult(f, f);
  }
  
  public Vec2 mult(float fX, float fY)
  {
    return new Vec2((int)(x * fX), (int)(y * fY));//scales to int
  }
  
  public Vec2 div(float f)
  {
    return div(f, f);
  }
  
  public Vec2 div(float fX, float fY)
  {
    return new Vec2((int)(x / fX), (int)(y / fY));//scales to int
  }
  
  public boolean equals(Object other)
  {
    if(other == null)
      return false;
    
    if(!(other instanceof Vec2))
      return false;
    
    if(x != ((Vec2)other).x)
      return false;
    
    if(y != ((Vec2)other).y)
      return false;
    
    return true;
  }
}
