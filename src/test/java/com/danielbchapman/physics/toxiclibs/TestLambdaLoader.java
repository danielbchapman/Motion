package com.danielbchapman.physics.toxiclibs;

import org.junit.Test;

public class TestLambdaLoader
{
  @Test
  public void testLoader()
  {
    LambdaBrush brush = new LambdaBrush();
    brush.lambda = "(x)->{System.out.println(\"TEST\");};";
    brush.name = "TestBrush";
    System.out.println(brush.compile());
  }
}
