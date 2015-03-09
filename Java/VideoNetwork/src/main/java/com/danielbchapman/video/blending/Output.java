package com.danielbchapman.video.blending;

import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;

import com.danielbchapman.video.blending.Blender.Variables;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;

public class Output extends JFrame
{
  private static final long serialVersionUID = 1L;
  
  KeystoneApplet processing;
  int[] pixelCache;
  
  Object lock = new Object();
  public Output()
  {
    this((String) null);
  }
  
  public Variables getVariables()
  {
    return null;
//    Variables var = new Variables();
//    setVisible(false);
//    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
//    GraphicsDevice[] screens = ge.getScreenDevices();
//    int n = screens.length;
//    
//    for (int i = 0; i < n; i++) {
//        if (screens[i].getIDstring().contentEquals(settings.getScreen())) {
//            JFrame dummy = new JFrame(screens[i].getDefaultConfiguration());
//            setLocationRelativeTo(dummy);
//            dummy.dispose();
//        }
//    }
//    setVisible(true);    
  }
  public Output(String title)
  {
    setTitle("Window X " + title);
    setSize(400, 400);
    processing = new KeystoneApplet(true);
    processing.setSize(400, 400);
    processing.init();
    add(processing);
  }
  
  boolean full = false;
  public void goFullScreen()
  {
    System.out.println("Full Screen API");

    full = !full;
    if(full){
      invalidate();
      setSize(Toolkit.getDefaultToolkit().getScreenSize());
      setUndecorated(true);
      revalidate();
    }
    else 
    {
      setUndecorated(false);
      setSize(1280, 720);
    }
  }
  
  
  public void setupCache(int width, int height)
  {
    
  }
  
  public void updateCache(int[] data)
  {
    synchronized(lock){
      pixelCache = data;
    }
  }
  public class KeystoneApplet extends PApplet
  {
    private static final long serialVersionUID = 1L;
    boolean fullScreen = false;
    PVector topLeft;
    PVector topRight;
    PVector bottomRight;
    PVector bottomLeft;
    
    String message = "No Node Selected";
    public KeystoneApplet(boolean full)
    {
      this.fullScreen = full;
    }
    
//    public boolean sketchFullScreen()
//    {
//      return fullScreen;
//    }
    
    public void drawKeystone(PGraphics g, int x, int y, int color)
    {
      g.pushMatrix();
      g.noFill();
      g.strokeWeight(1);
      g.stroke(color);
      g.ellipseMode(CENTER);
      g.ellipse(x, y, 8, 8);
      g.ellipse(x, y, 16, 16);
      g.ellipse(x, y, 2, 2);
      g.line(x-30, y, x+30, y);
      g.line(x, y-30, x, y+30);
      g.text("(" + x + ", " + y + ")", x+10, y+20);
      g.text("(" + x + ", " + y + ")", x-60, y-15);
      g.text("(" + x + ", " + y + ")", x+10, y-15);
      g.text("(" + x + ", " + y + ")", x-60, y+20);
      
      g.text(message == null ? "" : message, g.width / 2 - 150, g.height /2);
      
      g.popMatrix();
    }
    
    public void setup()
    {
      
    }
    
    @Override
    public void mouseClicked()
    {
      goFullScreen();
    }
    public void draw()
    {
      background(0);
//      if(pixelCache != null){
//        synchronized(lock){
//          //Draw
//        }
//      }
      
      drawKeystone(g, 50, 50, color(255,0,0));
      drawKeystone(g, 200, 50, color(255,0,0));
      drawKeystone(g, 200, 200, color(255,0,0));
      drawKeystone(g, 50, 200, color(255,0,0));
    }
  }
}
