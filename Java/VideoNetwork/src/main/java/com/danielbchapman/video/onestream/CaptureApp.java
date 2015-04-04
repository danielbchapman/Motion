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
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.function.BiConsumer;
import java.util.function.Function;

import javax.swing.JFrame;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.video.Capture;

import com.danielbchapman.utility.FileUtil;
import com.danielbchapman.video.blending.Vec2;

import deadpixel.keystone.CornerPinSurface;
import deadpixel.keystone.Keystone;

/**
 * <Class Definitions>
 *
 */
public class CaptureApp extends PApplet
{
  
  

  
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private static JFrame application;

  public static void main(String[] args)
  {
    application = new JFrame();
    application.setUndecorated(true);
    Vec2 size = maxOut(application);
    application.setSize(size.x, (size.y / 4 * 3));
    // fs.setSize(1920 * 2, 720);//1080
    // fs.setLocation(-1920, 0);// FIXME need a smart way to deal with this.
    CaptureApp x = new CaptureApp();
    application.add(x);
    application.setVisible(true);
    x.init();

    maxOut(application);
    // GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(fs);
  }

  public static Vec2 maxOut(JFrame x)
  {
    int width = 0;
    int height = 0;
    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    GraphicsDevice[] gs = ge.getScreenDevices();
    for (GraphicsDevice curGs : gs)
    {
      DisplayMode mode = curGs.getDisplayMode();
      width += mode.getWidth();
      height = Math.max(mode.getHeight(), height);
    }

    GraphicsConfiguration xgc = x.getGraphicsConfiguration();
    Window fs = xgc.getDevice().getFullScreenWindow();
    Rectangle r = xgc.getBounds();

    System.out.println("Total Width-> " + width + " Total Height-> " + height + " Current Location ->" + r.x + ", " + r.y);
    return new Vec2(width, height);
  }
  Object lock = new Object();

  Vec2 vertex = null;

  String message = "No Node Selected";
  Monitor one = new Monitor(this, 0, 0, 400, 0, 400, 400, 0, 400, "Mointor 1", 0, 0); // 1920 for offset dual
  Monitor two =  new Monitor(this, 0, 0, 400, 0, 400, 400, 0, 400, "Monitor 2", 400, 0);
  Monitor three = new Monitor(this, 0, 0, 400, 0, 400, 400, 0, 400, "Monitor 3", 400, 0);
  Monitor four = null; // new Monitor(this, 0, 0, 400, 0, 400, 400, 0, 400, "Monitor 4", 400, 0);
  int monitorIndex = 0;
  Monitor selected = one;
  
  Monitor[] monitors = new Monitor[] { one, two, three, four };// Hard Coded 4 max
  CornerPinSurface[] surfaces = new CornerPinSurface[monitors.length];
  
  
  Keystone keystone;
  public boolean editing;
  public int monitor = 0;

  /**
   * This is the raw "Canvas" that is pulled from. On each capture event
   * this is updated with the content that can be drawn as needed. 
   */
  PImage image;
  
  /**
   * Vector[](x,y,u,v); //texture map 
   */
  float[][] mesh;

  Dimension contentInput = new Dimension(640, 480);

  Capture input;

  /**
   * Two monitors blended by 48 pixels!
   */
  Vec2 surfaceDimensions = new Vec2(2048-48, 1024);

  public void captureEvent(Capture c)
  {
    input.read();
    input.loadPixels();
    // System.out.println("Reading image...");

    // Resize on stream change...
    if (image.width != input.height || image.height != input.height)
    {
      image = new PImage(input.width, input.height);
    }
    //This is rudimentary, this needs to be properly scaled, this is distorted!
    image.loadPixels();
    image.pixels = input.pixels;
    image.updatePixels();

//    for (Variables v : monitors)
//      if (v != null)
//      {
//        // Write the image crop to the cache.
//        v.cache.image(image, 0, 0);// FIXME crop this here...
//        //System.out.println("writing to cache: " + v.cache);
//      }

    // int[] pixels = input.pixels;
    // for(int i = 0; i < buffer.length; i++)
    // buffer[i] = pixels[i];

    // System.out.println("Copied...");
  }

  public void draw()
  {
    background(123, 0, 0);

    fill(255);
    text("Frame Rate:" + frameRate, 50, 50);
    // The following does the same as the above image() line, but
    // is faster when just drawing the image without any additional
    // resizing, transformations, or tint.
    // set(0, 0, cam);

    for (int i = 0; i < monitors.length; i++)
    {
      if (monitors[i] != null)
      {
        // System.out.println("Drawing monitor [" + i + "]->" + monitors[i]);
        drawMonitor(monitors[i], surfaces[i]);
        if (editing)
          drawUi(g, monitors[i]);
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
    g.line(x - 30, y, x + 30, y);
    g.line(x, y - 30, x, y + 30);
    g.text("(" + x + ", " + y + ")", x + 10, y + 20);
    g.text("(" + x + ", " + y + ")", x - 60, y - 15);
    g.text("(" + x + ", " + y + ")", x + 10, y - 15);
    g.text("(" + x + ", " + y + ")", x - 60, y + 20);

    g.text(message == null ? "" : message, g.width / 2 - 150, g.height / 2);

    g.popMatrix();
  }

  public void drawMonitor(Monitor v, CornerPinSurface s)
  {
    boolean __useKeystone = true;
    if (__useKeystone)
    {

      //
      // v.cache.clear();
      if(v.cache == null || v.blend == null || image == null)
      {
        System.out.println("Cache or image null\n\t" + v + " | " + image);
        return;
      }
        
      v.cache.beginDraw();
      v.cache.image(image, v.contentStart.x, v.contentStart.y, v.contentDimensions.x, v.contentDimensions.y); //crop here
      //v.cache.image(image, 0, 0, image.width, image.height);
    
      v.cache.mask(v.blend);
      //v.cache.image(v.blend,0,0);
      v.cache.endDraw();
      s.render(g, v.cache, 0, 0, v.cache.width, v.cache.height);
      return;
    }
    int tX = 0;
    int tY = 0;
    int tW = image.width;
    int tH = image.height;

    // Vec2 top = v.topRight.add(v.topLeft).div(2f);//v.topRight.sub(v.topLeft).div(2f, 1f);
    // Vec2 right = v.topRight.add(v.bottomRight).div(2f);
    // Vec2 bottom = v.bottomRight.add(v.bottomLeft).div(2f);
    // Vec2 left = v.bottomLeft.add(v.topLeft).div(2f);

    // System.out.println("Top Left -> " + v.topLeft);
    // System.out.println("Top -> " + top);
    // System.out.println("Top Right -> " + v.topRight);
    // System.out.println("Right -> " + right);
    // System.out.println("Bottom Right -> " + v.bottomRight);
    // System.out.println("Bottom -> " + bottom);
    // System.out.println("Bottom Left -> " + v.bottomRight);
    // System.out.println("Left -> " + left);
    // System.out.println();

    // Create Mesh Surface-> Keystone5 base

    pushMatrix();
    noFill();
    noStroke();
    translate(v.home.x, v.home.y, 0);

    textureMode(IMAGE);
    beginShape();
    texture(image);
    vertex(v.topLeft.x, v.topLeft.y, tX, tY);
    vertex(v.topRight.x, v.topRight.y, tW, tY);
    vertex(v.bottomRight.x, v.bottomRight.y, tW, tH);
    vertex(v.bottomLeft.x, v.bottomLeft.y, tX, tH);
    endShape(CLOSE);
    popMatrix();
  }

  public void drawSurface(PGraphics g, Monitor v)
  {
    /*
     * Finish this at some point to understand it, what he does is makes a map of quads and maps the texture to it using
     * "WarpPerspective" to determine points and just applying the texture to the 3D surface. This isn't a bad method,
     * I'm not sure it is needed.
     */

    // int tX = 0;
    // int tY = 0;
    // int tW = 800;
    // int tH = 600;
    //
    // int w = v.contentDimensions.x
    // g.pushMatrix();
    // g.translate(v.home.x, v.home.y);
    // pushMatrix();
    // noFill();
    // noStroke();
    // textureMode(IMAGE);
    // beginShape(PApplet.QUADS);
    // texture(image);
    //
    //
    //
    // vertex(v.topLeft.x, v.topLeft.y, tX, tY);
    // vertex(v.topRight.x, v.topRight.y, tW, tY);
    // vertex(v.bottomRight.x, v.bottomRight.y, tW, tH);
    // vertex(v.bottomLeft.x, v.bottomLeft.y, tX, tH);
    // endShape(PApplet.CLOSE);
    // popMatrix();
    // g.popMatrix();
  }
  
  public void drawUi(PGraphics g, Monitor monitor)
  {
    pushMatrix();
    int red = color(255, 0, 0);// Point
    int orange = color(255, 255, 0);// Selected
    int green = color(0, 255, 0);// MouseOver

    int textRestore = getFont().getSize();
    fill(255);
    if (monitor.topLeft.equals(vertex))
      drawKeystone(g, monitor.topLeft, monitor.home, orange);
    else
      drawKeystone(g, monitor.topLeft, monitor.home, red);

    if (monitor.topRight.equals(vertex))
      drawKeystone(g, monitor.topRight, monitor.home, orange);
    else
      drawKeystone(g, monitor.topRight, monitor.home, red);

    if (monitor.bottomRight.equals(vertex))
      drawKeystone(g, monitor.bottomRight, monitor.home, orange);
    else
      drawKeystone(g, monitor.bottomRight, monitor.home, red);

    if (monitor.bottomLeft.equals(vertex))
      drawKeystone(g, monitor.bottomLeft, monitor.home, orange);
    else
      drawKeystone(g, monitor.bottomLeft, monitor.home, red);

    translate(monitor.tX(getCenterish() / 2), height / 2);
    textSize(72);
    if (monitor.equals(selected))
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
    for (Monitor v : monitors)
      if (v != null)
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

    if (e.getKey() == 's')
    {
      System.out.println("SAVING?");
      if (e.isAltDown() || e.getKeyCode() == ALT)
      {
        System.out.println("SAVING.");
        save();

      }
    }
    
    if (e.getKey() == 'l')
    {
      if(e.isAltDown() || e.getKeyCode() == ALT)
      {
        System.out.println("LOADING POSITIONS");
        load();
      }
    }

    if (e.getKey() == 'e')
    {
      System.out.println("Entering Keystone Calibration");
      keystone.toggleCalibration();
    }

    if (e.getKey() == 'm')
    {
      Callable<Void> advance = () -> {
        monitorIndex = (monitorIndex + 1) % monitors.length;
        int index = monitorIndex % monitors.length;
        System.out.println("index->" + monitorIndex);
        selected = monitors[index];
        vertex = null;
        return null;
      };

      try
      {
        advance.call();
        while (selected == null)
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
    
    if(e.getKey() == '-')
      blend(-1);
    if(e.getKey() == '=')
      blend(1);
    if(e.getKey() == '_')
      blend(-5);
    if(e.getKey() == '+')
      blend(+5);
   

  }

  public void load()
  {
    BiConsumer<Monitor, String> l = (v, f) ->
    {
      if(v != null)
        v.load(f);
    };
    
    l.accept(one,  "config/1.cfg");
    l.accept(two,  "config/2.cfg");
    l.accept(three,  "config/3.cfg");
    l.accept(four,  "config/4.cfg");
    
    updateMesh();
  }

  public void save()
  {
    System.out.println("SAVING SETTINGS via serialization");
    
    BiConsumer<Monitor, String> s = (v, f) -> 
    {
      if(v != null)
        v.save(f);
    };
    
    BiConsumer<Monitor, CornerPinSurface> update = (v, c)->
    {
     if(v == null || c == null)
       return;
     
     float[] tl  = c.getMeshPoint(CornerPinSurface.TL);
     float[] tr  =c.getMeshPoint(CornerPinSurface.TR);
     float[] bl  =c.getMeshPoint(CornerPinSurface.BL);
     float[] br  =c.getMeshPoint(CornerPinSurface.BR);
     
     v.topLeft.x = (int)tl[0];
     v.topLeft.y = (int)tl[1];
     
     v.topRight.x = (int)tr[0];
     v.topRight.y = (int)tr[1];
     
     v.bottomRight.x = (int)br[0];
     v.bottomRight.y = (int)br[1];
     
     v.bottomLeft.x = (int)bl[0];
     v.bottomLeft.y = (int)bl[1];
     
     v.home.x = (int) c.x;
     v.home.y = (int) c.y;
     
     System.out.println("Saving surface to ->\n" + v);
     
    };
    
    for(int i = 0; i < monitors.length; i++)
      update.accept(monitors[i], surfaces[i]);
    
    s.accept(one,  "config/1.cfg");
    s.accept(two,  "config/2.cfg");
    s.accept(three,  "config/3.cfg");
    s.accept(four,  "config/4.cfg");
  }

  public void setup()
  {
    
    Vec2 allMonitors = maxOut(application);
    size(allMonitors.x, allMonitors.y / 4 * 3, P3D); // Debug make this smaller
    // size(1920 * 2, 1080, P3D);
    // size(1400, 800, P3D);
    smooth();
    noLights();
    keystone = new Keystone(this);
    
    {// Draw Grid
      image = loadImage("butterfly.jpg");
      PGraphics grid = createGraphics(image.width, image.height);
      // draw a grid
      int spacing = 10;
      int x = image.width / spacing;
      int y = image.height / spacing;

      grid.beginDraw();
      grid.background(255, 0, 0);
      grid.strokeWeight(2f);
      grid.stroke(255);
      grid.image(image, 0, 0);
      for (int i = 0; i < x; i++)
        grid.line(i * spacing, 0, i * spacing, image.height);

      for (int i = 0; i < y; i++)
        grid.line(0, i * spacing, image.width, i * spacing);
      grid.endDraw();

      // image.loadPixels();
      // image.pixels = grid.pixels;
      // image.updatePixels();

    }
    for (Monitor v : monitors)
      if (v != null)
      {
        v.blend = createGraphics(contentInput.width, contentInput.height, P3D);
        v.cache = createGraphics(contentInput.width, contentInput.height, P3D);
        v.updateBlend();
      }

    int resolution = 4;
    int w = 800;
    int h = 600;
    // Update Mesh (from Keystone 5)
    mesh = new float[resolution * resolution][]; // vector(x,y,u,v)
    for (int i = 0; i < mesh.length; i++)
    {
      float x = (i % resolution) / (float) (resolution - 1);
      float y = (i / resolution) / (float) (resolution - 1);
      mesh[i] = new float[] { x * w, y * h, x * w, y * h };
    }

    // Create Keystone Surfaces
    for (int i = 0; i < monitors.length; i++)
    {
      if (monitors[i] != null)
      {
        surfaces[i] = keystone.createCornerPinSurface(800, 600, 12);
        surfaces[i].x = monitors[i].home.x;
        surfaces[i].y = monitors[i].home.y;
      }
    }
    // Disable for no input devices and use the butterfly
    boolean __enableCamera = false;

    if (__enableCamera)
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

        input = new Capture(this, 640, 480);
        input.start();
      }
    }

    System.out.println("Camera intialized...");
    
    //Set up the surfaces
    //one = real
    
    int blend = 100/2;
    one.contentDimensions = new Vec2(640, 480);
    one.contentStart = new Vec2(0,0);
    
    two.contentDimensions = new Vec2(1280, 960); 
    two.contentStart = new Vec2(-320-blend, -480); //middle of image
    
    three.contentDimensions = new Vec2(1280, 960);
    three.contentStart = new Vec2(-640-320+blend, -480); //middle of image
    
  }
  @Override
  public boolean sketchFullScreen()
  {
    return false;
  }

  public void updateMesh()
  {
    for(int i = 0; i < monitors.length; i++)
    {
      Monitor v = monitors[i];
      CornerPinSurface c = surfaces[i];
      if(v != null && c != null)
      {
        c.load(v.home.x, v.home.y, 
            v.topLeft.x, v.topLeft.y,
            v.topRight.x, v.topRight.y,
            v.bottomRight.x, v.bottomRight.y,
            v.bottomLeft.x, v.bottomLeft.y);
        c.x = v.home.x;
        c.y = v.home.y;
        System.out.println("Home move -> " + c);
//        c.w = v.home.x;
//        c.h = v.home.y;
//        c.moveMeshPointTo(CornerPinSurface.TL, v.topLeft.x, v.topLeft.y);
//        c.moveMeshPointTo(CornerPinSurface.TR, v.topRight.x, v.topRight.y);
//        c.moveMeshPointTo(CornerPinSurface.BR, v.bottomRight.x, v.bottomRight.y);
//        c.moveMeshPointTo(CornerPinSurface.BL, v.bottomLeft.x, v.bottomLeft.y);
       
      }
    }
  }
  
  public void blend(int amount)
  {
    for(Monitor m : monitors)
      if(m != null)
      {
        m.blendTop.y = m.blendTop.y+ amount;
        
        if(m.blendTop.y > 255)
          m.blendTop.y = 255;
        if(m.blendTop.y < 0)
          m.blendTop.y = 0;
        
        m.updateBlend();
        System.out.println("Blend is -> " + m.blendTop);
      }
  }
}
