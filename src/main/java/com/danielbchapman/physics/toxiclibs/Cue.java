package com.danielbchapman.physics.toxiclibs;

import java.util.ArrayList;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.danielbchapman.groups.Item;

public class Cue
{
  private Layer layer;
  private MotionEngine engine;
  private final Action[] actions;
  private long start;
  private long total;
  private boolean running;
  private String label = "Unknown";
  public static final ScheduledThreadPoolExecutor SCHEDULER = new ScheduledThreadPoolExecutor(8);
  
  public Cue(String label, Layer layer, MotionEngine engine, ArrayList<Action> actions)
  {
    if(label == null)
      label = "Unknown";
    this.label = label;
    this.layer = layer;
    this.engine = engine;
    this.actions = new Action[actions.size()];
    for(int i = 0; i < actions.size(); i++)
      this.actions[i] = actions.get(i);
  }
  
  //FIXME this is a hack design that will work temporarily using the scheduler ~ 20ms in error in raw tests
  public void go(Layer layer, MotionEngine engine)
  {
    if(running)
      return;
    
    running = true;
    long start = System.currentTimeMillis();
    System.out.println("Firing Cue " + label);
    for(Action a : actions)
    {
      a.called = start;
      a.layer = layer;
      a.engine = engine;
      SCHEDULER.schedule(a, a.timeStamp, TimeUnit.MILLISECONDS);
      
    }
    
    long executeLast = 0L;
    if(actions.length > 1)
      executeLast = actions[actions.length - 1].timeStamp; 
    
    SCHEDULER.schedule(new Runnable(){
      
      @Override
      public void run()
      {
        System.out.println("Cue " + label + " Complete | Actions Length " + actions.length);
        Cue.this.running = false;
        
      } }, executeLast, TimeUnit.MILLISECONDS);
  }
  
  public static void save(Item item, Cue cue)
  {
    
  }
  
  public static Cue load(Item item)
  {
    return null;
  }
}
