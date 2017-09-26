package com.danielbchapman.motion.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import codeanticode.syphon.SyphonClient;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

public class MotionSyphonClient extends MotionGraphicsClient
{
  SyphonClient client;
  Object syphonClient;
  Class<?> syhponClientClass;
  Method stop;
  Method getGraphics;
  Method isActive;
  PApplet motion;
  PGraphics _localGraphics;
  
  public MotionSyphonClient(PApplet app, PGraphics g, String appName, String serverName)
  {
    super(app, g, appName, serverName);
  }

  @Override
  public void connect()
  {
    
    System.out.println("connecting to -> " + appName + "::" + serverName);
    client = new SyphonClient(applet, appName, serverName);
//    try
//    {
//      syhponClientClass = Class.forName("codeanticode.syphon.SyphonClient");
//      getGraphics = syhponClientClass.getMethod("getGraphics", PGraphics.class);
//      stop = syhponClientClass.getMethod("stop");
//      isActive = syhponClientClass.getMethod("active");
//      //Try different connection methods here...
//      Constructor<?> constructor = syhponClientClass.getConstructor(PApplet.class, String.class, String.class);
//      syphonClient = constructor.newInstance(applet, appName, serverName);
//    }
//    catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
//    {
//      syphonClient = null;
//      e.printStackTrace();
//      throw new IllegalStateException("Unable to connect to syphon" + e.getMessage());
//    }
  }

  @Override
  public void update()
  {
    //Update code is handled by the new-frame boolean
  }

  int count = 0;
  @Override
  public PImage getImage()
  {
//    if(syphonClient == null) 
//    {
//      if(count++ % 60 == 0) { //prevent spam
//        Log.warn("Syphon Client is not connected....returning graphics that was passed in.");  
//      }
//      
//      graphics.beginDraw();
//      graphics.fill(128,0,0);
//      graphics.rect(0, 0, graphics.width, graphics.height);
//      graphics.endDraw();
//      graphics.fill(255,255,255);
//      graphics.text("Syphon Client Not Connceted", 50, 50);
//      graphics.endDraw();
//      return this.graphics; 
//    }
    
//    try
//    {
      if(client.newFrame()) {
        return client.getImage(this.graphics);
        //return _localGraphics = client.getGraphics(_localGraphics);
      } else {
        if(count++ % 60 == 0) { //prevent spam
          Log.warn("Syphon Client is not connected....returning graphics that was passed in.");
          client.listServers();
        }
        graphics.beginDraw();
        graphics.fill(128,0,0);
        graphics.rect(0, 0, graphics.width, graphics.height);
        graphics.fill(255,255,255);
        graphics.text("Syphon Client Not Connceted", 50, 50);
        graphics.text("Please check the stack trace in the console", 50, 70);
        graphics.text("Syphon Client is not Active", 50, 90);
        graphics.endDraw();
        return this.graphics;
      }
//      if( (boolean) isActive.invoke(syphonClient) ) {
//        _localGraphics = (PGraphics) getGraphics.invoke(syphonClient, this.graphics);
//        return _localGraphics;        
//      } else {
//        
//      }
//      
//    }
//    catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
//    {
//      e.printStackTrace();
//      graphics.beginDraw();
//      graphics.fill(128,0,0);
//      graphics.rect(0, 0, graphics.width, graphics.height);
//      graphics.fill(255,255,255);
//      graphics.text("Syphon Client Not Connceted", 50, 50);
//      graphics.text("Please check the stack trace in the console", 50, 70);
//      String msg = e.getMessage();
//      graphics.text(msg == null ? e.getClass().getSimpleName() : msg, 50, 90);
//      graphics.endDraw();
//      return this.graphics;
//    }
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
