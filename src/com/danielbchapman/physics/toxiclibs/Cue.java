package com.danielbchapman.physics.toxiclibs;

import java.util.ArrayList;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Cue
{
  private Layer layer;
  private MotionEngine engine;
  private final Action[] actions;
  private long start;
  private long total;
  private boolean running;
  private ScheduledThreadPoolExecutor scheduler = new ScheduledThreadPoolExecutor(8);
  
  public Cue(Layer layer, MotionEngine engine, ArrayList<Action> actions)
  {
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
    for(Action a : actions)
    {
      a.called = start;
      a.layer = layer;
      a.engine = engine;
      scheduler.schedule(a, a.timeStamp, TimeUnit.MILLISECONDS);
      
    }
    
    long executeLast = 0L;
    if(actions.length > 1)
      executeLast = actions[actions.length - 1].timeStamp; 
    
    scheduler.schedule(new Runnable(){
      
      @Override
      public void run()
      {
        System.out.println("Actions Length " + actions.length);
        Cue.this.running = false;
        
      } }, executeLast, TimeUnit.MILLISECONDS);
  }
}
