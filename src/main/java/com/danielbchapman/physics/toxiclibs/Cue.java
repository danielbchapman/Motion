package com.danielbchapman.physics.toxiclibs;

import java.util.ArrayList;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.danielbchapman.groups.Item;

public class Cue
{
  private ArrayList<ActionOLD> actions = new ArrayList<>();
  private boolean running;
  private String label = "Unknown";
  public static final ScheduledThreadPoolExecutor SCHEDULER = new ScheduledThreadPoolExecutor(8);
  
  public Cue(String label, ArrayList<ActionOLD> actions)
  {
    if(label == null)
      label = "Unknown";
    this.label = label;
    
    if(actions != null)
      for(ActionOLD a : actions)
        this.actions.add(a);
  }
  
  public void addAction(ActionOLD a)
  {
    actions.add(a);
  }
  
  public void removeAction(ActionOLD a)
  {
    actions.remove(a);
  }
  
  //FIXME this is a hack design that will work temporarily using the scheduler ~ 20ms in error in raw tests
  public void go(Layer layer, MotionEngine engine)
  {
    if(running)
      return;
    
    running = true;
    long start = System.currentTimeMillis();
    System.out.println(label + " [FIRED]");
    for(ActionOLD a : actions)
    {
      a.called = start;
      a.layer = layer;
      a.engine = engine;
      SCHEDULER.schedule(a, a.timeStamp, TimeUnit.MILLISECONDS);
      
    }
    
    long executeLast = 0L;
    if(actions.size() > 1)
      executeLast = actions.get(actions.size() - 1).timeStamp; 
    
    SCHEDULER.schedule(new Runnable(){
      
      @Override
      public void run()
      {
        System.out.println("Cue " + label + " Complete | Actions Length " + actions.size());
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
  
  public static Cue create(String name, ActionOLD ... actions)
  {
    ArrayList<ActionOLD> act = new ArrayList<>();
    
    if(actions != null)
      for(ActionOLD a : actions)
        if(a != null)
          act.add(a);
    
    return new Cue(name, act);
  }
}
