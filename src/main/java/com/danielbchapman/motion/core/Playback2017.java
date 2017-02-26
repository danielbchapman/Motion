package com.danielbchapman.motion.core;

import java.util.ArrayList;

import com.danielbchapman.brushes.VectorBrush;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class Playback2017
{
  private String label;
  private long start = -1L;
  @Getter
  private boolean running;
  private RecordAction2017[] actions;
  
  private int last = -1;
  private int size = -1;
  private MotionBrush brush;
  
  public Playback2017(String label, Motion motion, MotionBrush brush, ArrayList<RecordAction2017> actions)
  {
    if(brush == null)
      brush = motion.currentBrush;
    
    this.brush = brush.copy();
    
    this.actions = new RecordAction2017[actions.size()];
    for(int i = 0; i < actions.size(); i++)
      this.actions[i] = actions.get(i);
    
    this.label = label;
  }
  
  public void start()
  {
    size = actions.length;
    if(size > 1)
    {
      running = true;
      start = System.currentTimeMillis();  
    }
  }
  
  public void poll(Motion e)
  {
    if(!running)
      return;
    
    //Set this to use only the last point in the last rather than ALL points
    @SuppressWarnings("FIXME - Vector Brushes are next!")
    boolean objectHandlesDeltaTime = false; //(brush instanceof VectorBrush);
    
    //Create and add a series of points to simulate real-time behaviors
    if(!objectHandlesDeltaTime) 
    {
      long max = System.currentTimeMillis() - start;
      if(last == -1)
        last = 0;
      
      for(; last < size; last++)
      {
        if(actions[last].stamp > max)
          return; // try again next loop
        
        e.robot(actions[last], brush);
      }
      
      RecordAction2017 copy = actions[last -1];
      copy.leftClick = false;
      copy.rightClick = false;
      copy.centerClick = false;
      
      //FIXME use a specific brush here.
      e.robot(copy, brush);
//      System.out.println("Polling complete");
//      ArrayList<RecordAction> cp = new ArrayList<>();
//      for(int i = 0; i < actions.length; i++)
//        cp.add(actions[i]);
//      //Recorder.save(cp, e.getWidth(), e.getHeight());
      running = false;
      last = -1;
      size = -1;
    }
    //Object has internal cacluations so don't add 27 poitns for a frame
    else //We will need to modify this.
    {
      long max = System.currentTimeMillis() - start;
      if(last == -1)
        last = 0;
      
      RecordAction2017 nextAction = null;
      for(; last < size; last++)
      {
        if(actions[last].stamp > max)
        {
          if(nextAction != null)
            e.robot(nextAction, brush);
          return;
        }
        
        RecordAction2017 tmp = actions[last];
        if(tmp != null)
          nextAction = tmp;
      }
      
      //Fire the final action...
      RecordAction2017 copy = actions[last -1];
      copy.leftClick = false;
      copy.rightClick = false;
      copy.keyEvent = false;
      
      //FIXME use a specific brush here.
      e.robot(copy, brush);
//      System.out.println("Polling complete");
//      ArrayList<RecordAction> cp = new ArrayList<>();
//      for(int i = 0; i < actions.length; i++)
//        cp.add(actions[i]);
//      //Recorder.save(cp, e.getWidth(), e.getHeight());
      running = false;
      last = -1;
      size = -1;
    }
    
  }
}
