package com.danielbchapman.motion.core;

import org.junit.Assert;
import org.junit.Test;

import shows.test.TestEllipseBrush;
import test.Log;

public class TestSerialization
{

  public static void testBrush(MotionBrush brush)
  {
    String out = brush.save();
    MotionBrush copy = brush.load(out);
    Log.debug(brush.getClass().getName());
    Log.debug("\t" + out);
    Assert.assertEquals("Brushes are equal", brush, copy);
  }

  @Test
  public void testMouseBrushes()
  {
    testBrush(new MouseBrush());
    testBrush(new VectorMouseBrush());
  }

  @Test
  public void testTestBrushes()
  {
    testBrush(new TestEllipseBrush(false));
    testBrush(new TestEllipseBrush(true));
  }
}
