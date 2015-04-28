package com.danielbchapman.artwork;

import java.util.ArrayList;
import java.util.Random;

public class RandomPlacement
{
  public static void main(String ... args)
  {
    randomCounts(45000, 20);
  }
  
  public static void randomCounts(int millis, int count)
  {
    Random rand = new Random();
    
    int range = millis / count;
    
    ArrayList<Integer> values = new ArrayList<>();
    
    values.add(0);
    for(int i = 1; i < count; i++)
    {
      int random = (range * i) + (int)bind(0, -200f, 200f);
      values.add(random);
    }
    
    for(Integer i : values)
      System.out.println(i);
    
  }
  public static float bind(float f, float low, float high)
  {
    float original = f;
    float spread = high - low;
    f = f * spread + low;
    //System.out.println("Returning -> " + f + " spread " + spread + " | b(" + low + ", " + high +") " + original);
    return f;
  }
  
  public static void randomTweets()
  {
    //Create Tweets
    Random rand = new Random();
//    System.out.println(bind(0f, -1f, 1f));
//    System.out.println(bind(1f, -1f, 1f));
//    System.out.println(bind(.5f, -1f, 1f));
//    System.out.println(bind(.64f, -1f, 1f));
//    System.out.println(bind(.64f, 0f, 1f));
//    System.out.println(bind(.5f, .5f, 1f));
    
    for(int i = 1; i <= 20; i++)
    {
      float x = bind(rand.nextFloat(), -0.95f, .8f);
      float y = bind(rand.nextFloat(), -0.9f, .3f);
      float w = bind(rand.nextFloat(), .25f, .42f);
      
      System.out.println("tweets.add(create(" + i + ", " + x + "f, " + y +"f, " + w + "f));" );
    }

    for(int i = 0; i < 5; i++)
      System.out.println();
    
    for(int i = 1; i <= 20; i++)
    {
      System.out.println("cue(\"Tweet " + i + "\", action(\"Tweet " + i + "\", null, (x)->{tweet(x.getPhysics());} )),");
    }
  }
}
