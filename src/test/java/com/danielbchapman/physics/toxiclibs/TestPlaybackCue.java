package com.danielbchapman.physics.toxiclibs;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TestPlaybackCue
{

  @Test
  public void testIO()
  {
    PlaybackCue test = new PlaybackCue("a", "b", "c");
    String data = PlaybackCue.toLine(test);
    PlaybackCue load = PlaybackCue.fromLine(data);
    
    assertTrue("Test == Load", test.equals(load));
  }
}
