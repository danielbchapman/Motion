package com.danielbchapman.ui.fx;

import java.util.function.Consumer;



/**
 * A simple editor interface that allows methods to be the same across 
 * different editors so that the software does not need to be rewritten 
 * when editors are updated.
 */
public interface IBound<T>
{
  
  /**
   * Returns the listener for this object.
   * 
   * @return the Consumer&lt;Double&gt; for this object 
   * (which is called when the object is updated).
   * 
   */
  public Consumer<T> getOnUpdate();
  
  /**
   * Sets the listener for this object.
   * 
   * @param onUpdate   
   */
  public void setOnUpdate(Consumer<T> onUpdate);
  
  /**
   * Sets the value of this double to the number provided.
   * If null, no value is set.
   * @param t The T to set this slider to.
   */
  public void set(T t);
  /**
   * Returns the current value of the slider.
   * @return 
   * 
   */
  public T get();
}
