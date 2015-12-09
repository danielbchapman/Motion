package com.danielbchapman.physics.toxiclibs;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TestActions
{
  @Test
  public void testRecordSerialization()
  {
    int w = 800;
    int h = 600;

    //FIXME this doesn't really have high accuracy, we need a +/- 1 or 2 this will fail on a random test
    RecordAction a = new RecordAction("Test", 2560, 2400, 2000, true, true, true);
    
    String y = RecordAction.toFloatFormat(a, w, h);
    RecordAction b = RecordAction.fromFloatFormat(y, w, h);

    assertTrue("A == B FLOAT\n" + a + "\n" + b, a.equals(b));
  }
}
