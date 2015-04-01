package com.danielbchapman.video;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import processing.core.PApplet;

public class TestVideoInput
{
  @Test(timeout = 20000)
  public void TestInput() throws Throwable
  {
    Throwable t = null;
    TestCapApplet app = new TestCapApplet();
    PApplet.runSketch(new String[] { "JUnit Test" }, app);
    while(!app.finished)
      Thread.yield();
    t = app.error;

    assertTrue("Exeception Found!", t != null);
  }
}
