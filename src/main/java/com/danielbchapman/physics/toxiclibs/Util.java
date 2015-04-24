package com.danielbchapman.physics.toxiclibs;

import java.util.Random;

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
}
