package com.danielbchapman.motion.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import lombok.Data;
import processing.core.PGraphics;
import processing.core.PImage;

import com.danielbchapman.code.Pair;
import com.sun.jna.Platform;

/**
 * A cross platform implementation of either Syphon or Spout. 
 */

@Data
public abstract class MotionGraphicsClient
{
  /**
   * A static factory method that provides a client based on the host operating system. This 
   * could easily be extended to provide CITP or NDI support 
   * @param g
   * @param appName
   * @param serverName
   * @return a suitable subclass for the host system or null if unavailable.
   * @throws ClassNotFoundException if there is a compilation error
   * @throws IllegalStateException if there is a problem initializing the connection
   */
  public static MotionGraphicsClient CreateClient(PGraphics g, String appName, String serverName) throws IllegalStateException
  {
    if (Platform.isWindows() || Platform.isWindowsCE())
    {
      try
      {
        Class<? extends MotionGraphicsClient> spoutClass = (Class<? extends MotionGraphicsClient>) Class.forName("com.danielbchapman.motion.core.MotionSpoutClient");
        Constructor<?> initialize = spoutClass.getConstructor(PGraphics.class, String.class, String.class);
        MotionGraphicsClient spout = (MotionGraphicsClient) initialize.newInstance(g, appName, serverName);
        spout.connect();
        return spout;
      }
      catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
      {
        e.printStackTrace();
        return null;
      }
    }
    else if (Platform.isMac())
    {
      try
      {
        Class<? extends MotionGraphicsClient> spoutClass = (Class<? extends MotionGraphicsClient>) Class.forName("com.danielbchapman.motion.core.MotionSyphonClient");
        Constructor<?> initialize = spoutClass.getConstructor(PGraphics.class, String.class, String.class);
        MotionGraphicsClient spout = (MotionGraphicsClient) initialize.newInstance(g, appName, serverName);
        spout.connect();
        return spout;
      }
      catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
      {
        e.printStackTrace();
        return null;
      }
    }
    else
      throw new UnsupportedOperationException("The MotionGraphicsClient can only be used on Windows or Mac as it is dependent on third part solutions for texture sharing.");
  }
  PGraphics graphics;
  String appName;
  String serverName;
  
  public MotionGraphicsClient(PGraphics g, String appName, String serverName)
  {
    this.graphics = g;
    this.appName = appName;
    this.serverName = serverName;
  }
  
  /**
   * Start the connection to the sever
   */
  public abstract void connect();
  
  /**
   * Perform any needed update.
   */
  public abstract void update();
  
  /**
   * Return the graphics context for this Client
   */
  public abstract PImage getImage();
  
  /**
   * Shutdown this texture shared client.
   */
  public abstract void shutdown();
}
