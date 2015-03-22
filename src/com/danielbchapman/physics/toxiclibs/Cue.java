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
  public void go()
  {
    if(running)
      return;
    
    running = true;
    long start = System.currentTimeMillis();
    for(Action a : actions)
    {
      a.called = start;
      scheduler.schedule(a, a.timeStamp, TimeUnit.MILLISECONDS);
      
    }
      
    scheduler.schedule(new Runnable(){

      @Override
      public void run()
      {
        Cue.this.running = false;
      } }, actions[actions.length-1].timeStamp, TimeUnit.MILLISECONDS);
  }
}
