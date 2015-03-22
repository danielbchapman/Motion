package com.danielbchapman.physics.toxiclibs;

import java.util.concurrent.Callable;
import java.util.function.Consumer;;

public class Action implements Callable
{
  public int timeStamp;
  public long called;
  public String label = "";
  public Consumer<Layer> layerFunction;
  public Consumer<Point> pointFunction;
  public Consumer<MotionEngine> motionFunction;
  
  public Action(String label, int timeStamp)
  {
    this.timeStamp = timeStamp;
    this.label = label;
  }

  @Override
  public Object call() throws Exception
  {
    System.out.println("Executing action: " + label + " at " + timeStamp + " elapsed = " + (System.currentTimeMillis() - called)); 
    return null;
  }
}
