package com.danielbchapman.motion.core;

import java.util.HashMap;

import com.danielbchapman.text.Text;
import com.danielbchapman.utility.Utility;

/**
 * A simple map of properties that is unsafe but will not throw exceptions
 * on conversion. It stores the objects and converts them if needed. On conversion 
 * failure the default will be returned.
 */
public class SceneProperties
{
  private static String INT_CLASS = Integer.class.toString();
  private static String FLOAT_CLASS = Float.class.toString();
  private static String STRING_CLASS = String.class.toString();
  
  public HashMap<String, Object> properties = new HashMap<>();
  
  public String getString(String key, String defautValue)
  {
    return getAs(key, String.class, defautValue);
  }
  
  public Integer getInteger(String key, int defaultValue)
  {
    return getAs(key, Integer.class, Integer.valueOf(defaultValue));
  }
  
  public Float getFloat(String key, float defaultValue)
  {
    return getAs(key, Float.class, Float.valueOf(defaultValue));
  }
  
  public void setString(String key, String value)
  {
    properties.put(key, value);
  }
  
  public void setInteger(String key, int value)
  {
    properties.put(key, (Integer)value);
  }
  
  public void setFloat(String key, float value)
  {
    properties.put(key, (Float) value);
  }
  
  public static SceneProperties load(String fileData)
  {
    SceneProperties props = new SceneProperties();
    
    String[] parts = fileData.split("\n");
    
    if(parts.length < 3) //bad formatting
      return props;
    
    for(int i = 0; i < parts.length; i+= 3) 
    {
      if(i + 2 >= parts.length) {
        System.err.println("WARNING: Did not process last property when loading Scene Data");
        break;
      }
      
      String key = parts[i];
      String clazz = parts[i + 1];
      String value = parts[i + 2];
      
      if (INT_CLASS.equals(clazz))
      {
        props.setInteger(key.trim(), Integer.valueOf(value));
      } 
      else if (FLOAT_CLASS.equals(clazz)) 
      {
        props.setFloat(key.trim(), Float.valueOf(value));
      }
      else if (STRING_CLASS.equals(clazz))
      {
        props.setString(key, value);
      }
      else
        System.err.println("did not match " + clazz);
    }
    
    return props;
  }
  
  /**
   * Serialize to a simple newline separated key/class/value pairing
   * @return 
   */
  public String serialize()
  {
    StringBuilder builder = new StringBuilder();
    
    for(String key: properties.keySet())
    {
      Object value = properties.get(key);
      if(value == null)
        continue; //we don't care
      builder.append(key);
      builder.append("\n");
      builder.append(value.getClass());
      builder.append("\n");
      builder.append(value);
      builder.append("\n");
    }
    return builder.toString();
  }
  
  @SuppressWarnings("unchecked")
  private <T> T getAs(String key, Class<T> clazz, T defaultValue)
  {
    Object out = properties.get(key);
    if (out == null)
      return defaultValue;
    
    if (clazz.isInstance(out))
      return (T) out;
    
    return defaultValue;
  }
}
