package com.danielbchapman.motion.core;

import java.util.logging.Level;
import java.util.logging.Logger;

import lombok.Data;

import org.junit.Assert;
import org.junit.Test;

import test.Log;

import com.google.gson.Gson;

public class TestCueStack
{  
  @Test
  public void testSerializatoin()
  {
    PathCue a = new PathCue();
    
    a.setId("1");
    a.setPathFile("Path File");
    a.setEnvironmentFile("Environment File");
    a.setBrushFile("Brush File");
    a.setPathFile("Brush File");
    a.setLabel("Label here");
    a.getPosition().x = 12;
    a.getScale().y = 12;
    a.getAnchor().z = 12;    
    a.setTimeScale(1.5f);
    Log.debug(a.toString());
    String aa = a.save();
    PathCue b = a.load(aa);
    Log.debug(aa);
    Log.debug(b);
    Assert.assertEquals("Cue a == Cue b", a, b);
  }
  
  @Test
  public void testCueList()
  {
    CueStack stack = new CueStack("Test-Stack");
    for(int i = 0; i < 50; i++)
    {
      Cue q = null;
      if(i % 2 == 0)
        q = new PathCue();
      else
        q = new ContentCue();
      
      q.getAnchor().x = i;
      q.getAnchor().y = i + 1;
      q.getAnchor().z = i + 2;
      
      q.getPosition().x = i;
      
      q.getScale().z = i * 2;
      q.setPathFile("Some File Name " + i);
      q.setId(Integer.toString(i));
      q.setLabel("Cue " + i);
      stack.add(q);
    }
    
    String out = stack.save();
    CueStack in = stack.load(out);
    Log.debug(out);
    Assert.assertEquals("Stack are equal? ", in, stack);
  }
  
  @Test
  public void testBasics()
  {
    TestClass x = new TestClass();
    x.outer = "OUTER!";
    x.inner = new SubClass();
    x.inner.setInner("INNER");
    String str = new Gson().toJson(x);
    Log.debug(str);
    TestClass y = new Gson().fromJson(str, TestClass.class);
    Assert.assertEquals("x == y? ", x, y);
  }
  
  @Data
  public class TestClass
  {
    String outer;
    SubClass inner;
  }
  
  @Data
  public class SubClass 
  {
    String inner;
  }
}
