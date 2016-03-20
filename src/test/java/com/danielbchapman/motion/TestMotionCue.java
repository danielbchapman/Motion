package com.danielbchapman.motion;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

public class TestMotionCue
{
  @Test
  public void TestSerialization()
  {
    MotionCue a = new MotionCue();
    a.setId(99);
    a.setDelay(3f);
    a.setDescription("Test Description");
    a.setFollow(1f);
    a.setLabel("Test Label");
    a.setTime(5000.500f);
    String aData = MotionCue.serialize(a);
    MotionCue b = MotionCue.deserialize(aData);
    Assert.assertEquals("a != b\na:\n" + a + "\nb:\n" + b +"\n", a, b); 
  }
  
  @Test
  public void TestSerializationList()
  {
    ArrayList<MotionCue> cues = new ArrayList<MotionCue>();
    for(int i = 0; i < 10; i++)
    {
      MotionCue a = new MotionCue();
      a.setId(i);
      a.setDelay(.3f + i);
      a.setDescription("Test Description" + i);
      a.setFollow(.4f + i);
      a.setLabel("Test Label" + i);
      a.setTime(.500f + i);
      
      cues.add(a);
    }
    
    String data = MotionCue.serialize(cues);
    System.out.println("------------------------------\n");
    System.out.println(data);
    System.out.println("------------------------------\n");
    ArrayList<MotionCue> copy = MotionCue.deserializeList(data);
    
    for(int i =0 ; i < cues.size(); i++)
    {
      MotionCue a = cues.get(i);
      MotionCue b = copy.get(i);
      
      Assert.assertEquals(a,  b);
      System.out.println("a == b");
      System.out.println(a);
      System.out.println(b);
    }
  }
}
