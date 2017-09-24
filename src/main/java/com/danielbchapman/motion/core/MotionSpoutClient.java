package com.danielbchapman.motion.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.opengl.PGraphicsOpenGL;

public class MotionSpoutClient extends MotionGraphicsClient
{
  PImage SPOUT;
  Object spout;
  Class<?> spoutClass;
  Method initReceiver;
  Method closeReceiver;
  Method receiveTexture;

  public MotionSpoutClient(PApplet app, PGraphics g, String appName, String serverName)
  {
    super(app, g, appName, serverName);
    SPOUT = new PImage(g.width, g.height);
  }

  @Override
  public void connect() throws IllegalStateException
  {
    try
    {
      spoutClass = Class.forName("SpoutImplementation");
      initReceiver = spoutClass.getMethod("initReceiver", String.class, PImage.class);
      closeReceiver = spoutClass.getMethod("closeReceiver");
      receiveTexture = spoutClass.getMethod("receiveTexture", PImage.class);
      spout = spoutClass.getConstructor(PGraphicsOpenGL.class).newInstance(this.graphics);
      initReceiver.invoke(spout, this.serverName, SPOUT);
    }
    catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
    {
      spout = null;
      e.printStackTrace();
      throw new IllegalStateException("Unable to connect to spout" + e.getMessage());
    }
  }

  @Override
  public void update()
  {
    if (spout == null)
      return;

    try
    {
      receiveTexture.invoke(spout, SPOUT);
    }
    catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
    {
      e.printStackTrace();
    }
  }

  @Override
  public void shutdown()
  {
    if (spout == null)
      return;

    try
    {
      closeReceiver.invoke(spout);
    }
    catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
    {
      e.printStackTrace();
    }

    spout = null;
  }

  @Override
  public PImage getImage()
  {
    return SPOUT;
  }
}
