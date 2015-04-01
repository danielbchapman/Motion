package com.danielbchapman.video.blending;

import java.awt.Dimension;
import java.awt.Event;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import com.danielbchapman.groups.Groups;
import com.danielbchapman.groups.Groups.GroupFormatException;
import com.danielbchapman.video.blending.Output.KeystoneApplet;

import processing.core.PApplet;
import processing.core.PImage;
import processing.video.*;

public class Blender
{
  Output[] outputs = new Output[8];
  String file = "settings.xml";

  int[] input800x600 = new int[640 * 480];
  CaptureApplet temp;

  public Blender()
  {
    temp = new CaptureApplet();
    PApplet.runSketch(new String[]{"Video Capture Sink"}, temp);
    try
    {
      System.out.println("Loading settings");
      Groups.fromXml(new FileInputStream("settings.xml"));
    }
    catch (ParserConfigurationException | GroupFormatException | SAXException | IOException e)
    {
      e.printStackTrace();
    }

    Controller main = new Controller();
    main.setSize(400, 400);
    main.setVisible(true);
    main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    for (int i = 0; i < 6; i++)
    {
      Output x = new Output("Output: " + i);
      outputs[i] = x;
      x.setVisible(true);
    }

    // Save on exit
    main.addWindowListener(new java.awt.event.WindowAdapter()
    {
      @Override
      public void windowClosing(java.awt.event.WindowEvent windowEvent)
      {
        if (JOptionPane.showConfirmDialog(main, "Do you want to save on exit?", "Save State", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)
        {
          try
          {
            Groups.toXml(new FileOutputStream(new File(file)), "Blender", "Version 0.0.1", Groups.getKnownGroups());
          }
          catch (FileNotFoundException | ParserConfigurationException | TransformerException e)
          {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
          System.exit(0);
        }
      }
    });
  }

  public static void showOnScreen(int screen, JFrame frame)
  {
    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    GraphicsDevice[] gs = ge.getScreenDevices();
    if (screen > -1 && screen < gs.length)
    {
      gs[screen].setFullScreenWindow(frame);
    }
    else
      if (gs.length > 0)
      {
        gs[0].setFullScreenWindow(frame);
      }
      else
      {
        throw new RuntimeException("No Screens Found");
      }
  }

  public class Controller extends JFrame
  {
    private static final long serialVersionUID = 1L;

    public Controller()
    {
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setTitle("Video Blender 0.0.1");
      add(new JButton("Go Fullscreen")
      {
        {
          addActionListener(new ActionListener()
          {

            @Override
            public void actionPerformed(ActionEvent e)
            {
              System.out.println("Going Full Screen by rebuilding API");
            }

          });
        }
      });
    }

    public void goWindowed()
    {
      setSize(1280, 720);
    }

    public void goFullScreen()
    {
      // System.out.println("Full Screen API");
      // if(full){
      // setVisible(false);
      // invalidate();
      // setSize(Toolkit.getDefaultToolkit().getScreenSize());
      // //setUndecorated(true);
      // revalidate();
      // setVisible(true);
      // }
      // else
      // {
      // //setUndecorated(false);
      //
      // }
    }

  }

  public class CaptureApplet extends PApplet
  {
    Capture input;
    int[] buffer;
    private Object lock = new Object();;
    
    public void setup()
    {
      String[] cameras = Capture.list();

      if (cameras.length == 0)
      {
        System.err.println("There are no cameras available for capture.");
        System.exit(-1);
      }
      else
      {
        System.out.println("Available cameras:");
        for (int i = 0; i < cameras.length; i++)
          System.out.println(cameras[i]);
        
        input = new Capture(temp, 640, 480);
        buffer = new int[input.width * input.height];
        input.start();
      }
      setVisible(false);
    }
    
    public void captureEvent(Capture c)
    {
      input.read();
      input.loadPixels();
//      int[] pixels = input.pixels;
//      for(int i = 0; i < buffer.length; i++)
//        buffer[i] = pixels[i];
      
      synchronized(lock)
      {
        for(int i = 0; i < outputs.length; i++)
        {
          if(outputs[i] != null)
            outputs[i].updateCache(input.pixels);
        }   
        
        for(int i = 0; i < outputs.length; i++)
          if(outputs[i] != null)
          {
            KeystoneApplet applet = outputs[i].processing;
            applet.syncDraw();
          }
      }
     
      //System.out.println("Copied...");
    }
    
    public void draw()
    {
//      PImage copy = new PImage(640, 480);
//      copy.loadPixels();
//      copy.pixels = buffer;
//      copy.updatePixels();
//      image(copy, 0, 0);
    }
  }
}
