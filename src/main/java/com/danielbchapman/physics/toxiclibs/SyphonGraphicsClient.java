package com.danielbchapman.physics.toxiclibs;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import processing.core.PApplet;
import processing.core.PGraphics;

public class 
SyphonGraphicsClient implements IGraphicsShareClient
{
  Object syphonClient;
  Method stop;
  Method getGraphics;
  Constructor<?> syhponConstructor;
  
  @Override
  public void initialize(PApplet app, String appName, String serverName, int width, int height)
  {
    Class<?> syphonClientClass = null;
    try
    {
      syphonClientClass = Class.forName("codeanticode.syphon.SyphonClient");
      syhponConstructor = syphonClientClass.getConstructor(PApplet.class, String.class);
      stop = syphonClientClass.getDeclaredMethod("stop");
      getGraphics = syphonClientClass.getDeclaredMethod("getGraphics", PGraphics.class, String.class, String.class);
      syphonClient = syhponConstructor.newInstance(app, appName, serverName);

      System.out.println("SYPHON INITIALZIED");
    }
    catch (NoSuchMethodException | SecurityException | ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
    {
      e.printStackTrace();
    }    
  }

  @Override
  public void cleanup()
  {
    if (syphonClient != null && stop != null) 
    {
      try
      {
        stop.invoke(syphonClient);
      }
      catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
      {
        syhponConstructor = null;
        syphonClient = null;
        getGraphics = null;
        stop = null;
        e.printStackTrace();
      }
    }
    
  }

  @Override
  public void read(PGraphics g)
  {
    if (getGraphics != null && syphonClient != null)
    {
      try
      {
        getGraphics.invoke(syphonClient, g);
      }
      catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
      {
        cleanup();
        e.printStackTrace();
      }
    }
  }
  
  /**
   * Calls the static method returning servers.
   * @throws ClassNotFoundException 
   * @throws SecurityException 
   * @throws NoSuchMethodException 
   * @throws InvocationTargetException 
   * @throws IllegalArgumentException 
   * @throws IllegalAccessException 
   */
  @SuppressWarnings("unchecked")
  public static Map<String, String> listServers() throws ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
  {
    Class<?> syphon = Class.forName("codeanticode.syphon.SyphonClient");
    Method m_listServers = syphon.getDeclaredMethod("listServers");
    return (Map<String, String>) m_listServers.invoke(null);
  }
  
}
