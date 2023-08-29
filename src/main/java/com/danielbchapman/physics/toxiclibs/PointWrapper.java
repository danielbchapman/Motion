package com.danielbchapman.physics.toxiclibs;

import lombok.Getter;
import lombok.Setter;

public class PointWrapper<Wrap> extends PointOLD
{
  @Getter
  @Setter
  private Wrap wrap;
  
  public PointWrapper(float x, float y, float z, float w)
  {
    super(x,y,z,w);
  }
  
  public PointWrapper(PointWrapper<Wrap> p)
  {
    super(p);
    this.wrap = p.wrap;
  }
  
  public PointWrapper()
  {
  }
}
