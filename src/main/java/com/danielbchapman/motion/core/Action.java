package com.danielbchapman.motion.core;

import java.util.concurrent.Callable;
import java.util.function.Consumer;

public class Action implements Callable<Void>
{
  public int timeStamp;
  public long called;
  public String label = "";
  public Consumer<Scene> sceneFunction;
  public Consumer<Motion> motionFunction;

  public Scene scene;
  public Motion engine;
  
  public Action(String label, int timeStamp, Consumer<Scene> sceneFunction, Consumer<Motion> motionFunction)
  {
    this(label, timeStamp);
    this.sceneFunction = sceneFunction;
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
    //System.out.println("Executing action: " + label + " at " + timeStamp + " elapsed = " + (System.currentTimeMillis() - called));
    if (sceneFunction != null)
    	sceneFunction.accept(scene);

    if (motionFunction != null)
      motionFunction.accept(engine);

    return null;
  }
}
