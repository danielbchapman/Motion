package com.danielbchapman.artwork;

import java.util.Random;

public class RandomPlacement
{
  public static void main(String ... args)
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
  
  public static float bind(float f, float low, float high)
  {
    float original = f;
    float spread = high - low;
    f = f * spread + low;
    //System.out.println("Returning -> " + f + " spread " + spread + " | b(" + low + ", " + high +") " + original);
    return f;
  }
}
