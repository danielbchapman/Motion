package com.danielbchapman.physics.ui;

import java.io.Serializable;

import lombok.Data;

@Data
public class SelectItem<T> implements Serializable
{
  private static final long serialVersionUID = 1L;
  
  private T value;
  private String name;
  
  public SelectItem(String name, T value)
  {
    this.name = name;
    this.value = value;
  }
  
  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  public String toString()
  {
    return name;
  }
  
  
}
