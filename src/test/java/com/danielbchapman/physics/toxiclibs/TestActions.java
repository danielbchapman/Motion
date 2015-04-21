package com.danielbchapman.physics.toxiclibs;

import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Test;

public class TestActions
{
  @Test
  public void testRecordSerialization()
  {
    Random rand = new Random();
    RecordAction a = new RecordAction("Test", rand.nextInt(), rand.nextInt(), rand.nextInt(), true, true, true);
    
    String x = a.toString();
    RecordAction b = RecordAction.fromString(x);
    assertTrue("A == B", a.equals(b));
  }
}
