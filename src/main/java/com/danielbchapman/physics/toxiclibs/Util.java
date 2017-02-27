package com.danielbchapman.physics.toxiclibs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import processing.core.PApplet;

import com.danielbchapman.logging.Log;
import com.danielbchapman.utility.FileUtil;
import com.danielbchapman.utility.Utility;

public class Util
{
  public static Random rand = new Random();
  
  public static int rand(float low, float high)
  {
    return rand((int)low, (int)high);
  }
  
  public static int rand(int low, int high)
  {
    int delta = high - low;
    if(delta < 0)
      delta = -delta;
    
    int dif = rand.nextInt(delta);
    return dif + low;
  }
  
  /**
   * Fades a float value from one value to another based on the duration
   * passed in and the rate at which it should fade. Currently this is 
   * dependent on {@link processing.core#lerp}.
   * 
   * @param current the current value
   * @param target the target value 
   * @param totalTime the total time for this operation 
   * @param currentTime the current time elapsed
   * @param min minimum value
   * @param max maximum value
   * @return a float value based on the current  
   * 
   */
  public static float fade(float current, float target, float totalTime, float currentTime, float min, float max)
  {
    float percentComplete = 0f;
    if(totalTime < 1) //complete fade
    {
      percentComplete = 1f;
    }
    else if(currentTime == 0) //minimum fade
    {
      percentComplete = 0f; 
    }
    else
    {
      percentComplete = currentTime / totalTime;
      if(percentComplete > 1f)
        percentComplete = 1f;
    }
    if(false && Log.IS_DEBUG)
    {
      Log.debug("\nFADE:\tCurrent value " + current);
      Log.debug("\tTarget Value " + target);
      Log.debug("\tTotal Time " + totalTime);
      Log.debug("\tCurrent Time " + currentTime);
      Log.debug("\tMin " + min);
      Log.debug("\tMax: " + max);
      Log.debug("\tPercent: " + percentComplete);
    }
    
  	float lerp = PApplet.lerp(current, target, percentComplete);  
  	if(lerp < min)
  		return min;
  	if(lerp > max)
  		return max;
  	return lerp;
  }
  
  public static void writeProps(String file, Map<String, Map<String, String>> props)
  {
    ArrayList<String> lines = new ArrayList<>();
    
    for(String s : props.keySet())
    {
      lines.add("[" + s + "]");
      Map<String, String> data = props.get(s);
      if(data != null)
        for(String k : data.keySet())
        {
          String val = data.get(k);
          if(k == null || k.isEmpty())
            continue;
          
          lines.add(k + " = " + val.trim());          
        }

      lines.add("");
    }
    
    StringBuilder text = new StringBuilder();
    
    lines.forEach((s)->{
      text.append(s);
      text.append("\r\n");
    });
    
    
    FileUtil.writeFile(file, text.toString().getBytes());
  }
  
  /**
   * Reads a section based properties file in simple INI format.
   * @param file the file to write
   * @param defaults the defaults to write
   * @return the file with defaults missing.
   * @throws Runtime Exception on bad read
   * 
   */
  public static Map<String, Map<String, String>> readProps(String file, Map<String, Map<String, String>> defaults)
  {
    Map<String, Map<String, String>> out = new HashMap<>();
    ArrayList<String> strings = FileUtil.readLines(file);
    
    String currentSection = "motion";
    for(String s : strings)
    {
      s = s.trim();
      if(s.length() < 1)
        continue;//Ignore Whitespace
      if(s.startsWith("#"))
        continue;//Ignore Comments
      if(s.startsWith(";"))
        continue;
      
      if(s.startsWith("[") && s.endsWith("]"))
      {
        s = s.replaceAll("\\[", "").replaceAll("\\]","");
        currentSection = s.toLowerCase();
        continue;
      }
      String[] parts = s.split("=");
      if(parts.length != 2)
      {
        System.err.println("ignoring setting " + s);
      }
      
      Map<String, String> section = out.get(currentSection);
      if(section == null)
      {
        section = new HashMap<>();
        out.put(currentSection, section);
      }
      section.put(parts[0].trim(), parts[1].trim());
    }
    
    return out;
  }
}
