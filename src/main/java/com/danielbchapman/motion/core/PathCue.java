package com.danielbchapman.motion.core;

import java.util.ArrayList;

import processing.core.PGraphics;
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
  public void start(Motion motion, long time)
  {
    if(!loaded)
      load(motion);

    if(playback == null)
    {
      inError = true;
      return;
    }
    
    startTime = time;
    playback.start(time);
  }


  @Override
  public void load(Motion motion)
  {
    ArrayList<RecordAction2017> actions = Recorder2017.load(pathFile, motion.width, motion.height, 0, 0);
    MotionBrush brush = motion.currentBrush.clone();
    playback = new Playback2017(pathFile, motion, brush, actions);
  }


  @Override
  public void update(Motion motion, long time)
  { 
  }
}
