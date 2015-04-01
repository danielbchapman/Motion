package com.danielbchapman.video.onestream;

/**
 * Getting Started with Capture.
 * 
 * Reading and displaying an image from an attached Capture device. 
 */

import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Window;
import java.util.concurrent.Callable;

import javax.swing.JFrame;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

import com.danielbchapman.video.blending.Vec2;

public class CaptureApp extends PApplet
{
  public class Variables
  {
    //Content
    Vec2 contentStart = new Vec2();
    Vec2 contentDimensions = new Vec2();
    
    //Blending Variables
    Vec2 blendTopLeft = new Vec2();
    Vec2 blendTopRight = new Vec2();
    Vec2 blendBottomLeft = new Vec2();
    Vec2 blendBottomRight = new Vec2();
    
    //Blending Variables    
    Vec2 topLeft = new Vec2();
    Vec2 topRight = new Vec2();
    Vec2 bottomRight = new Vec2();
    Vec2 bottomLeft = new Vec2();
    
    Vec2 home = new Vec2();
    String name = "Default";
    
    PGraphics blend;

    public Variables(int x, int y)
    {
      this(0, 0, x, 0, x, y, 0, y, "Default", 0, 0);
    }

    public Variables(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4,
        String name,
        int homeX, int homeY)
    {
      topLeft = new Vec2(x1, y1);
      topRight = new Vec2(x2, y2);
      bottomRight = new Vec2(x3, y3);
      bottomLeft = new Vec2(x4, y4);
      
      home.x = homeX;
      home.y = homeY;
      this.name = name;
    }
    
    public int tX(int x)
    {
      return home.x + x;
    }
    
    public int tY(int y)
    {
      return home.y + y;
    }
    
    public void updateBlend()
    {
      if(blend == null)
        throw new RuntimeException("Unable to blend uninitialized graphics object");
      
      //DO fancy blending here!
      
    }

  }

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public static void main(String[] args)
  {
    JFrame fs = new JFrame();
    fs.setUndecorated(true);
    // fs.setSize(400, 400);
    fs.setSize(1920 * 2, 720);//1080
    fs.setLocation(-1920, 0);// FIXME need a smart way to deal with this.
    CaptureApp x = new CaptureApp();
    fs.add(x);
    fs.setVisible(true);
    x.init();

    maxOut(fs);
    // GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(fs);
  }

  public static void maxOut(JFrame x)
  {
    int width = 0;
    int height = 0;
    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    GraphicsDevice[] gs = ge.getScreenDevices();
    for (GraphicsDevice curGs : gs)
    {
      DisplayMode mode = curGs.getDisplayMode();
      width += mode.getWidth();
      height = mode.getHeight();
    }

    GraphicsConfiguration xgc = x.getGraphicsConfiguration();
    Window fs = xgc.getDevice().getFullScreenWindow();
    Rectangle r = xgc.getBounds();

    System.out.println("Total Width-> " + width + " Total Height-> " + height + " Current Location ->" + r.x + ", " + r.y);
  }

  Object lock = new Object();

  Vec2 vertex = null;
  String message = "No Node Selected";
  Variables one = new Variables(0, 0, 400, 0, 400, 400, 0, 400, "Mointor 1", 1920, 0);
  Variables two = new Variables(0, 0, 400, 0, 400, 400, 0, 400, "Monitor 2", 0, 0);
  Variables three = null;//new Variables(0, 0, 400, 0, 400, 400, 0, 400);
  Variables four = null; //new Variables(0, 0, 400, 0, 400, 400, 0, 400);
  int monitorIndex = 0;
  Variables selected = one;
  Variables[] monitors = new Variables[]{one, two, three, four};//Hard Coded 4 max

  public boolean editing;
  
  public int monitor = 0;
  PImage image = loadImage("butterfly.jpg");

  public void draw()
  {
    background(123, 0, 0);

    fill(255);
    text("Frame Rate:" + frameRate, 50, 50);
    // The following does the same as the above image() line, but
    // is faster when just drawing the image without any additional
    // resizing, transformations, or tint.
    // set(0, 0, cam);
    
    for(Variables v : monitors)
    {
      if(v != null)
      {
        drawMonitor(v);
        if(editing)
          drawUi(g, v);  
      }
    }
    
    fill(255);
    text("Frame Rate:" + frameRate, 50, 50);
  }

  public void drawKeystone(PGraphics g, Vec2 vec, Vec2 home, int color)
  {
    int x = vec.x;
    int y = vec.y;
    g.pushMatrix();
    g.translate(home.x, home.y);
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
  public void drawMonitor(Variables v)
  {
    pushMatrix();
    noFill();
    noStroke();
    translate(v.home.x,v.home.y,0);

    textureMode(IMAGE);
    beginShape(QUADS);
    texture(image);
    vertex(v.topLeft.x, v.topLeft.y, 0, 0);
    vertex(v.topRight.x, v.topRight.y, 800, 0);
    vertex(v.bottomRight.x, v.bottomRight.y, 800, 600);
    vertex(v.bottomLeft.x, v.bottomLeft.y, 0, 600);
    endShape(CLOSE);
    popMatrix();
  }

  public void drawUi(PGraphics g, Variables monitor)
  {
    pushMatrix();
    int red = color(255, 0, 0);//Point
    int orange = color(255, 255, 0);//Selected
    int green = color(0, 255, 0);//MouseOver
    
    int textRestore = getFont().getSize();
    fill(255);
    if(monitor.topLeft.equals(vertex))
      drawKeystone(g, monitor.topLeft, monitor.home, orange);
    else
      drawKeystone(g, monitor.topLeft, monitor.home, red);
    
    if(monitor.topRight.equals(vertex))
      drawKeystone(g, monitor.topRight, monitor.home, orange);
    else
      drawKeystone(g, monitor.topRight, monitor.home, red);
    
    if(monitor.bottomRight.equals(vertex))
      drawKeystone(g, monitor.bottomRight, monitor.home, orange);
    else
      drawKeystone(g, monitor.bottomRight, monitor.home, red);
    
    if(monitor.bottomLeft.equals(vertex))
      drawKeystone(g, monitor.bottomLeft, monitor.home, orange);
    else
      drawKeystone(g, monitor.bottomLeft, monitor.home, red);
    
    translate(monitor.tX(getCenterish()/2), height / 2);
    textSize(72);
    if(monitor.equals(selected))
    {
      fill(255);
      text(monitor.name + " EDITING", 0, 0);  
    }
    else
    {
      fill(128);
      text(monitor.name, 0, 0);
    }
    
    textSize(textRestore);
    popMatrix();
  }

  public int getCenterish()
  {
    int c = 0;
    for(Variables v : monitors)
      if(v != null)
        c++;
    
    return width / c;
  }
  
  @Override
  public void keyPressed(processing.event.KeyEvent e)
  {
    if (e.getKeyCode() == ESC)
    {
      System.exit(1);
    }
    
    if(e.getKey() == 's')
    {
      System.out.println("SAVING?");
      if(e.isAltDown())
      {
        System.out.println("SAVING.");
        save();
      }
    }
    
    if(e.getKey() == 'm')
    {
      Callable<Void> advance = () -> {
      monitorIndex = (monitorIndex + 1 ) % monitors.length;
      int index = monitorIndex % monitors.length;
      System.out.println("index->" + monitorIndex);
      selected = monitors[index];  
      vertex = null;
      return null;
      };
      
      try
      {
        advance.call();
        while(selected == null)
          advance.call();
      }
      catch (Throwable t)
      {
        t.printStackTrace();
      }
      System.out.println("Selecting monitor -> " + selected + "->" + (selected == null ? "" : selected.name));
    }

    int code = e.getKeyCode();
    if (editing && selected != null)
    {
      if (e.getKey() == '1')
      {
        message = "Selecting Top Left";
        vertex = selected.topLeft;
      }
      else
        if (e.getKey() == '2')
        {
          message = "Selecting Top Right";
          vertex = selected.topRight;
        }
        else
          if (e.getKey() == '3')
          {
            message = "Selecting Bottom Right";
            vertex = selected.bottomRight;
          }
          else
            if (e.getKey() == '4')
            {
              message = "Selecting Bottom Left";
              vertex = selected.bottomLeft;
            }
    }

    if (code == ENTER || code == RETURN)
    {
      editing = !editing; // toggle
      if (!editing)
        vertex = null;

      if (editing)
        message = "Entering Edit Mode";
      else
        message = "No Node Selected";
    }

    if (editing && vertex != null)
    {
      boolean modified = e.isShiftDown();
      if (code == UP)
      {
        if (modified)
          vertex.y -= 10;
        else
          vertex.y -= 1;
      }
      else
        if (code == DOWN)
        {
          if (modified)
            vertex.y += 10;
          else
            vertex.y += 1;
        }
        else
          if (code == LEFT)
          {
            if (modified)
              vertex.x -= 10;
            else
              vertex.x -= 1;
          }
          else
            if (code == RIGHT)
            {
              if (modified)
                vertex.x += 10;
              else
                vertex.x += 1;
            }

    }

  }  
  public void save()
  {
    System.out.println("SAVING SETTINGS--NOT IMPLEMENTED");
  }
  
  Dimension contentInput = new Dimension(640, 480);
  public void setup()
  {
    size(1920 * 2, 1080, P3D);
    for(Variables v : monitors)
      if(v != null)
      {
        v.blend = createGraphics(contentInput.width, contentInput.height);
        v.updateBlend();
      }
  }
  
  @Override
  public boolean sketchFullScreen()
  {
    // TODO Auto Generated Sub
    throw new RuntimeException("Not Implemented...");

  }
}
