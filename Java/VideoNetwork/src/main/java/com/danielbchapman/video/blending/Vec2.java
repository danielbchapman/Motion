package com.danielbchapman.video.blending;

public class Vec2
{
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
