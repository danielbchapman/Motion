package com.danielbchapman.physics.toxiclibs;

import java.util.Random;

import com.danielbchapman.logging.Log;

import processing.core.PApplet;
import processing.core.PGraphics;

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
    if(Log.IS_DEBUG)
    {
      
    }
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
  if(Log.IS_DEBUG)
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
  System.out.println("Returing value " + lerp + " at percent " + percentComplete);
	if(lerp < min)
		return min;
	if(lerp > max)
		return max;
	return lerp;
  }
}
