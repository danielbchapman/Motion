package com.danielbchapman.ui.fx;

import java.util.function.Consumer;

import lombok.Getter;
import lombok.Setter;
import toxi.geom.Vec4D;

public class BoundVec4D implements IBound<Vec4D>
{
  @Getter
  @Setter
  private Consumer<Vec4D> onUpdate;
  
  /**
   * A simple reference variable for listeners to block updates. 
   */
  boolean locked = false;
  
  /**
   * A simple variable that blocks the internal updates that would otherwise propogate (overriding values)
   */
  private Vec4D vec;
  
  private BoundFloat x;
  private BoundFloat y;
  private BoundFloat z;
  private BoundFloat w;
  
  /**
   * @param def the default vector
   * @param onUpdate what to do on change
   */
  public BoundVec4D(Vec4D def, Consumer<Vec4D> onUpdate)
  {
    this.onUpdate = onUpdate;
    x = new BoundFloat(def.x, 
        x -> 
        { 
          vec.x = x;
          fire();
        } );
    
    y = new BoundFloat(def.y, 
        y -> 
        { 
          vec.y = y;
          fire();
        } );
    
    z = new BoundFloat(def.z, 
        z -> 
        { 
          vec.z = z;
          fire();
        } );
    
    w = new BoundFloat(def.w, 
        w -> 
        { 
          vec.w = w;
          fire();
        } );
  }
  
  /* (non-Javadoc)
   * @see com.danielbchapman.ui.fx.IBound#get()
   */
  public Vec4D get()
  {
    return vec.copy();
  }
  
  /* (non-Javadoc)
   * @see com.danielbchapman.ui.fx.IBound#set(java.lang.Object)
   */
  public void set(Vec4D vec)
  {
    if(vec != null)
    {
      this.vec = vec.copy();
      locked = true;
      x.set(vec.x);
      y.set(vec.y);
      z.set(vec.z);
      w.set(vec.w);
      locked = false;
      fire();
    }
  }
  
  private void fire()
  {
    if(!locked && onUpdate != null)
      onUpdate.accept(vec.copy());
  }
  
  /* (non-Javadoc)
   * @see javafx.scene.Node#toString()
   */
  public String toString()
  {
    return super.toString() + " [" + vec + "]";
  }
}
