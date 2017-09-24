package com.danielbchapman.motion.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

public class MotionSyphonClient extends MotionGraphicsClient
{
  Object syphonClient;
  Class<?> syhponClientClass;
  Method stop;
  Method getGraphics;
  PApplet motion;
  PGraphics _localGraphics;
  
  public MotionSyphonClient(PApplet app, PGraphics g, String appName, String serverName)
  {
    super(app, g, appName, serverName);
  }

  @Override
  public void connect()
  {
    try
    {
      syhponClientClass = Class.forName("codeanticode.syphon.SyphonClient");
      getGraphics = syhponClientClass.getMethod("getGraphics", PGraphics.class);
      stop = syhponClientClass.getMethod("stop");
      
      //Try different connection methods here...
      syphonClient = syhponClientClass.getConstructor(PApplet.class, String.class, String.class).newInstance(motion, appName, serverName);
    }
    catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
    {
      syphonClient = null;
      e.printStackTrace();
      throw new IllegalStateException("Unable to connect to spout" + e.getMessage());
    }
  }

  @Override
  public void update()
  {
    //TODO Auto Generated Sub
    throw new RuntimeException("Not Implemented...");
    
  }

  int count = 0;
  @Override
  public PImage getImage()
  {
    if(syphonClient == null) 
    {
      if(count++ % 60 == 0) { //prevent spam
        Log.warn("Syphon Client is not connected....returning graphics that was passed in.");  
      }
      
      graphics.beginDraw();
      graphics.fill(128,0,0);
      graphics.rect(0, 0, graphics.width, graphics.height);
      graphics.endDraw();
      graphics.fill(255,255,255);
      graphics.text("Syphon Client Not Connceted", 50, 50);
      graphics.endDraw();
      return this.graphics; 
    }
    
    try
    {
      _localGraphics = (PGraphics) getGraphics.invoke(syphonClient, _localGraphics);
      return _localGraphics;
    }
    catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
    {
      e.printStackTrace();
      graphics.beginDraw();
      graphics.fill(128,0,0);
      graphics.rect(0, 0, graphics.width, graphics.height);
      graphics.endDraw();
      graphics.fill(255,255,255);
      graphics.text("Syphon Client Not Connceted", 50, 50);
      graphics.text("Please check the stack trace in the console", 50, 70);
      graphics.text(e.getMessage(), 50, 90);
      graphics.endDraw();
      return this.graphics;
    }
  }

  @Override
  public void shutdown()
  {
    if (syphonClient == null)
      return;
    
    try
    {
      stop.invoke(syphonClient);
    }
    catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
    {
      e.printStackTrace();
    }
    
    syphonClient = null;
  }
}
