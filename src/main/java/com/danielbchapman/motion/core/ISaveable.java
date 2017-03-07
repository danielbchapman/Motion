package com.danielbchapman.motion.core;

import com.google.gson.Gson;


public interface ISaveable<T>
{
  /**
   * Save this object to a string.
   */
  default public String save() 
  {
    Gson gson = new Gson();
    String ret = gson.toJson(this, getClass());
    return ret;
  }
  
  /**
   * Load this object from a string
   * @param data the string to process 
   * @return a new object (T). This is basically a static.
   */
  @SuppressWarnings("unchecked")
  default public T load(String data)
  {
    Object obj = new Gson().fromJson(data, getClass());
    return (T) obj;
  }
}
