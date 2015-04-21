package com.danielbchapman.physics.toxiclibs;

import java.util.concurrent.Callable;
import java.util.function.Consumer;

public class Action implements Callable<Void>
{
  public int timeStamp;
  public long called;
  public String label = "";
  public Consumer<Layer> layerFunction;
  public Consumer<MotionEngine> motionFunction;

  protected Layer layer;
  protected MotionEngine engine;
  
  public Action(String label, int timeStamp, Consumer<Layer> layerFunction, Consumer<MotionEngine> motionFunction)
  {
    this(label, timeStamp);
    this.layerFunction = layerFunction;
    this.motionFunction = motionFunction;
  }
  
  public Action(String label, int timeStamp)
  {
    this.timeStamp = timeStamp;
    this.label = label;
  }

  @Override
  public Void call() throws Exception
  {
    System.out.println("Executing action: " + label + " at " + timeStamp + " elapsed = " + (System.currentTimeMillis() - called));
    if (layerFunction != null)
      layerFunction.accept(layer);

    if (motionFunction != null)
      motionFunction.accept(engine);

    return null;
  }
}
