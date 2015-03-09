package com.danielbchapman.video.blending;

import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;
import processing.event.KeyEvent;

public class Output extends JFrame
{
  private static final long serialVersionUID = 1L;
  
  KeystoneApplet processing;
  int[] pixelCache;
  
  Object lock = new Object();
  boolean editing = false;
  Vec2 vertex = null;
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
    processing = new KeystoneApplet(true, getWidth(), getHeight());
    processing.setSize(400, 400);
    processing.init();
    add(processing);
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
    Vec2 topLeft = new Vec2();
    Vec2 topRight = new Vec2();
    Vec2 bottomRight = new Vec2();
    Vec2 bottomLeft = new Vec2();
    
    String message = "No Node Selected";
    public KeystoneApplet(boolean full, int x1, int y1,int x2, int y2, int x3, int y3, int x4, int y4)
    {
      this.fullScreen = full;
      topLeft = new Vec2(x1, y1);
      topRight = new Vec2(x2, y2);
      bottomRight = new Vec2(x3, y3);
      bottomLeft = new Vec2(x4, y4);
    }
    
    public KeystoneApplet(boolean full, int x, int y)
    {
      this(full, 0, 0, x, 0, x, y, 0, y);
    }

    @Override
    public void keyPressed(KeyEvent e)
    {

      int code = e.getKeyCode();
      if(editing)
      {
        if(e.getKey() == '1')
        {      
          message = "Selecting Top Left";
          vertex = topLeft;
        }
        else if (e.getKey() == '2')
        {
          message = "Selecting Top Right";
          vertex = topRight;
        }
        else if (e.getKey() == '3')
        {
          message = "Selecting Bottom Right";
          vertex = bottomRight;
        }
        else if (e.getKey() == '4')
        {
          message = "Selecting Bottom Left";
          vertex = bottomLeft;
        }
      }
      
      if(code == ENTER || code == RETURN)
      {
        editing = !editing; //toggle
        if(!editing)
          vertex = null;
        
        if(editing)
          message = "Entering Edit Mode";
        else
          message = "No Node Selected";
      }
      
      if(editing && vertex != null)
      {
        boolean modified = e.isShiftDown();
        if(code == UP)
        {
          if(modified)
            vertex.y -= 10;
          else
            vertex.y -= 1;
        }
        else if (code == DOWN)
        {
          if(modified)
            vertex.y += 10;
          else
            vertex.y += 1;
        }
        else if (code == LEFT)
        { 
          if(modified)
            vertex.x -= 10;
          else
            vertex.x -= 1;
        }
        else if (code == RIGHT)
        {
          if(modified)
            vertex.x += 10;
          else
            vertex.x += 1;
        }
        
      }
      
    }
    public void drawKeystone(PGraphics g, Vec2 vec, int color)
    {
      int x = vec.x;
      int y = vec.y;
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
      size(width, height, P3D);
    }
    
    public void draw()
    {
      background(0);
//      if(pixelCache != null){
//        synchronized(lock){
//          //Draw
//        }
//      }
      
      int red = color(255, 0, 0);//Point
      int orange = color(255, 255, 0);//Selected
      int green = color(0, 255, 0);//MouseOver
      
      //Process Selection here--keyboard only for now...
      if(topLeft.equals(vertex))
        drawKeystone(g, topLeft, orange);
      else
        drawKeystone(g, topLeft, red);
      
      if(topRight.equals(vertex))
        drawKeystone(g, topRight, orange);
      else
        drawKeystone(g, topRight, red);
      
      if(bottomRight.equals(vertex))
        drawKeystone(g, bottomRight, orange);
      else
        drawKeystone(g, bottomRight, red);
      
      if(bottomLeft.equals(vertex))
        drawKeystone(g, bottomLeft, orange);
      else
        drawKeystone(g, bottomLeft, red);
      
      //Draw Data...
      
      PImage image = loadImage("butterfly.jpg");
      textureMode(IMAGE);
      beginShape(QUADS);
      texture(image);
      vertex(topLeft.x, topLeft.y, 0, 0);
      vertex(topRight.x, topRight.y, 800, 0);
      vertex(bottomRight.x, bottomRight.y, 800, 600);
      vertex(bottomLeft.x, bottomLeft.y, 0, 600);
      endShape(CLOSE);
      
      translate(0, 0);
      text("Client: " + frameCount, 30, 90);      
    }
  }
}
