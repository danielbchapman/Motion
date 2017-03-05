package com.danielbchapman.motion.core;

import java.util.ArrayList;

import lombok.Data;
import lombok.Getter;

@Data
public class Playback2017
{
  private String label;
  private long start = -1L;
  @Getter
  private boolean running;
  @Getter
  private boolean completed;
  private RecordAction2017[] actions;
  
  private int last = -1;
  private int size = -1;
  private MotionBrush brush;
  
  public Playback2017(String label, Motion motion, MotionBrush brush, ArrayList<RecordAction2017> actions)
  {
    if(brush == null)
      brush = motion.currentBrush;
    
    this.brush = brush.clone();
    
    this.actions = new RecordAction2017[actions.size()];
    for(int i = 0; i < actions.size(); i++)
      this.actions[i] = actions.get(i);
    
    this.label = label;
  }
  
  public void start(long time)
  {
    size = actions.length;
    if(size > 1)
    {
      running = true;
      start = time;  
    }
  }
  
  public void poll(Motion motion)
  {
    if(!running)
      return;
    
    long max = System.currentTimeMillis() - start;
    if(last == -1)
      last = 0;
    
    for(; last < size; last++)
    {
      if(actions[last].stamp > max)
        return; // try again next loop
      
      motion.robot(actions[last], brush);
    }
    
    //Fire a final event to "release" the mousedown
    RecordAction2017 copy = actions[last -1];
    copy.leftClick = false;
    copy.rightClick = false;
    copy.centerClick = false;
    
    motion.robot(copy, brush);
    running = false;
    completed = true;
    last = -1;
    size = -1;
  }
}
