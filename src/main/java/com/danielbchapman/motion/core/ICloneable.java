package com.danielbchapman.motion.core;



/**
 * A checked exception free version of clonable that is
 * generically typed
 */
public interface ICloneable<T>
{
  /**
   * @return a deep copy of the object
   */
  public abstract T clone();
}
