package com.danielbchapman.motion.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import lombok.Data;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

import com.sun.jna.Platform;

/**
 * A cross platform implementation of either Syphon or Spout. 
 */

@Data
public abstract class MotionGraphicsClient
{
  /**
   * A static factory method that provides a client based on the host operating system. This 
   * could easily be extended to provide CITP or NDI support.
   * @param applet, the instance of Motion (needed for Syphon) 
   * @param g, the graphics context (needed for Spout)
   * @param appName
   * @param serverName
   * @return a suitable subclass for the host system or null if unavailable.
   * @throws ClassNotFoundException if there is a compilation error
   * @throws IllegalStateException if there is a problem initializing the connection
   */
  public static MotionGraphicsClient CreateClient(PApplet applet, PGraphics g, String appName, String serverName) throws IllegalStateException
  {
    if (Platform.isWindows() || Platform.isWindowsCE())
    {
      try
      {
        Class<? extends MotionGraphicsClient> spoutClass = (Class<? extends MotionGraphicsClient>) Class.forName("com.danielbchapman.motion.core.MotionSpoutClient");
        Constructor<?> initialize = spoutClass.getConstructor(PApplet.class, PGraphics.class, String.class, String.class);
        MotionGraphicsClient spout = (MotionGraphicsClient) initialize.newInstance(applet, g, appName, serverName);
        spout.applet = applet;
        spout.graphics = g;
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
  
  String appName;
  String serverName;
  PGraphics graphics;
  PApplet applet;
  
  public MotionGraphicsClient(PApplet app, PGraphics g, String appName, String serverName)
  {
    this.applet = app;
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
