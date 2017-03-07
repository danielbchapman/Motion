package com.danielbchapman.motion.core;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PathCue extends Cue<PathCue>
{
  private float timeScale = 1f;
  
  private transient Playback2017 playback;

  
  @Override
  public void start()
  {
    if(!loaded)
      load();

    if(playback == null)
    {
      inError = true;
      return;
    }
  }

  @Override
  public void stop()
  { 
  }

  @Override
  public void pause()
  { 
  }

  @Override
  public void load()
  {
  }

  @Override
  public void update(long time)
  {
  }
}
