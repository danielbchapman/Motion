package com.danielbchapman.ui.fx;

import java.util.function.Consumer;



/**
 * A simple editor interface that allows methods to be the same across 
 * different editors so that the software does not need to be rewritten 
 * when editors are updated.
 */
public interface IBound
{
  
  /**
   * Returns the listener for this object.
   * 
   * @return the Consumer&lt;Double&gt; for this object 
   * (which is called when the object is updated).
   * 
   */
  public Consumer<Double> getOnUpdate();
  
  /**
   * Sets the listener for this object.
   * 
   * @param onUpdate   
   */
  public void setOnUpdate(Consumer<Double> onUpdate);
  
  /**
   * Sets the value of this double to the number provided.
   * If null, no value is set.
   * @param number The number to set this slider to.
   */
  public void set(Number number);
  /**
   * Returns the current value of the slider.
   * @return 
   * 
   */
  public double get();
}
