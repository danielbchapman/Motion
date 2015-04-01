package com.danielbchapman.video;

import processing.core.PApplet;
import processing.video.Capture;

public class TestCapApplet extends PApplet
{
  /**
     * Getting Started with Capture.
     * 
     * Reading and displaying an image from an attached Capture device. 
     */
  Capture cam;
  Throwable error = null;

  public void setup()
  {
    try
    {
      size(640, 480);

      String[] cameras = Capture.list();

      if (cameras == null)
      {
        println("Failed to retrieve the list of available cameras, will try the default...");
        cam = new Capture(this, 640, 480);
      }
      if (cameras.length == 0)
      {
        println("There are no cameras available for capture.");
        exit();
      }
      else
      {
        println("Available cameras:");
        for (int i = 0; i < cameras.length; i++)
        {
          println(cameras[i]);
        }

        // The camera can be initialized directly using an element
        // from the array returned by list():
        cam = new Capture(this, 640, 480);
        // Or, the settings can be defined based on the text in the list
        // cam = new Capture(this, 640, 480, "Built-in iSight", 30);

        // Start capturing the images from the camera
        cam.start();
      }
      
      throw new RuntimeException("Test Exception!");

    }
    catch (Throwable t)
    {
      System.out.println("Exiting!");
      error = t;
      exit();
    }

  }

  public void draw()
  {
    if (cam.available() == true)
    {
      cam.read();
    }
    image(cam, 0, 0);
    // The following does the same as the above image() line, but
    // is faster when just drawing the image without any additional
    // resizing, transformations, or tint.
    // set(0, 0, cam);
  }

}
