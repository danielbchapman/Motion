package com.danielbchapman.motion.core;

import org.junit.Assert;
import org.junit.Test;

public class TestSceneProperties
{
  @Test
  public void testSaveLoad()
  {
    SceneProperties test = new SceneProperties();
    Float a = 12f;
    Integer b = 13;
    String c = "14";
    
    test.setFloat("a", a);
    test.setInteger("b", b);
    test.setString("c", c);
    
    String save = test.serialize();
    System.out.println(save);
    SceneProperties test2 = SceneProperties.load(save);
    
    Assert.assertEquals("Getter Works", test.getFloat("a", 5), a);
    
    Assert.assertEquals("A is good", test2.getFloat("a", 5), a);
    Assert.assertEquals("B is goos", test2.getInteger("b", 5), b);
    Assert.assertEquals("C is good", test2.getString("c", "5"), c);
  }
}
