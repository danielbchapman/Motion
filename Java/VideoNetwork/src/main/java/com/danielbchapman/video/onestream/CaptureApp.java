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
import processing.video.Capture;

import com.danielbchapman.video.blending.Vec2;

import deadpixel.keystone.CornerPinSurface;
import deadpixel.keystone.Keystone;
import deadpixel.keystone.MeshPoint;


/**
 * <Class Definitions>
 *
 ***************************************************************************
 * @author Daniel B. Chapman 
 * <br /><i><b>Light Assistant</b></i> copyright Daniel B. Chapman
 * @since Mar 31, 2015
 * @version 2 Development
 * @link http://www.lightassistant.com
 ***************************************************************************
 */
//FIXME Java Doc Needed

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
    PGraphics cache;

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
      
      int thickness = 100;
      int colorA = 0x00000000;
      int colorB = 0x000000FF;
      blend.beginDraw();
      blend.clear();
      blend.noStroke();
      blend.noFill();
      drawGradient(blend, 0, 0, blend.width, thickness, Y_AXIS, false);
      drawGradient(blend, 0, blend.height - thickness, blend.width, thickness, Y_AXIS, true);
      drawGradient(blend, 0, 0, thickness, blend.height, X_AXIS, false);
      drawGradient(blend, blend.width-thickness, 0, thickness, blend.height, X_AXIS, true);
      blend.endDraw();
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
    fs.setSize(1024, 768);
    //fs.setSize(1920 * 2, 720);//1080
    //fs.setLocation(-1920, 0);// FIXME need a smart way to deal with this.
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

  public static final int X_AXIS = 0;
  public static final int Y_AXIS = 1;
  
  Object lock = new Object();

  Vec2 vertex = null;
  String message = "No Node Selected";
  Variables one = new Variables(0, 0, 400, 0, 400, 400, 0, 400, "Mointor 1", 0, 0); //1920 for offset dual
  Variables two = new Variables(0, 0, 400, 0, 400, 400, 0, 400, "Monitor 2", 400, 0);
  Variables three = null;//new Variables(0, 0, 400, 0, 400, 400, 0, 400);
  Variables four = null; //new Variables(0, 0, 400, 0, 400, 400, 0, 400);
  int monitorIndex = 0;
  Variables selected = one;
  Variables[] monitors = new Variables[]{one, two, three, four};//Hard Coded 4 max
  CornerPinSurface[] surfaces = new CornerPinSurface[monitors.length];
  Keystone keystone;
  public boolean editing;
  
  public int monitor = 0;
  PImage image;

  
  public void draw()
  {
    background(123, 0, 0);

    fill(255);
    text("Frame Rate:" + frameRate, 50, 50);
    // The following does the same as the above image() line, but
    // is faster when just drawing the image without any additional
    // resizing, transformations, or tint.
    // set(0, 0, cam);
    
    for(int i = 0; i < monitors.length; i++)
    {
      if(monitors[i] != null)
      {
        //System.out.println("Drawing monitor [" + i + "]->" + monitors[i]);
        drawMonitor(monitors[i], surfaces[i]);
        if(editing)
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
    g.line(x-30, y, x+30, y);
    g.line(x, y-30, x, y+30);
    g.text("(" + x + ", " + y + ")", x+10, y+20);
    g.text("(" + x + ", " + y + ")", x-60, y-15);
    g.text("(" + x + ", " + y + ")", x+10, y-15);
    g.text("(" + x + ", " + y + ")", x-60, y+20);
    
    g.text(message == null ? "" : message, g.width / 2 - 150, g.height /2);
    
    g.popMatrix();
  }
  
  public void drawMonitor(Variables v, CornerPinSurface s)
  {
    boolean __useKeystone = true;
    if(__useKeystone){
      //
      //v.cache.clear();
      v.cache.image(image, 0, 0);
      v.cache.image(v.blend, 0, 0);
      s.render(v.cache, 0, 0, image.width, image.height);
//          v.contentStart.x, 
//          v.contentStart.y, 
//          v.contentDimensions.x, 
//          v.contentDimensions.y);
      return;
    }
    int tX = 0;
    int tY = 0;
    int tW = image.width;
    int tH = image.height;
    
//    Vec2 top = v.topRight.add(v.topLeft).div(2f);//v.topRight.sub(v.topLeft).div(2f, 1f);
//    Vec2 right = v.topRight.add(v.bottomRight).div(2f);
//    Vec2 bottom = v.bottomRight.add(v.bottomLeft).div(2f);
//    Vec2 left = v.bottomLeft.add(v.topLeft).div(2f);
    
//    System.out.println("Top Left -> " + v.topLeft);
//    System.out.println("Top -> " + top);
//    System.out.println("Top Right -> " + v.topRight);
//    System.out.println("Right -> " + right);
//    System.out.println("Bottom Right -> " + v.bottomRight);
//    System.out.println("Bottom -> " + bottom);
//    System.out.println("Bottom Left -> " + v.bottomRight);
//    System.out.println("Left -> " + left);
//    System.out.println();
    
    //Create Mesh Surface-> Keystone5 base
    
    
    pushMatrix();
    noFill();
    noStroke();
    translate(v.home.x,v.home.y,0);

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
  
  /**
   * Vector[](x,y,u,v); //texture map 
   */
  float[][] mesh;
  public void drawSurface(PGraphics g, Variables v)
  {
    /*
     Finish this at some point to understand it, what he does
      is makes a map of quads and maps the texture to it using 
      "WarpPerspective" to determine points and just applying the 
      texture to the 3D surface. This isn't a bad method, I'm not
      sure it is needed. 
     */
    
//    int tX = 0;
//    int tY = 0;
//    int tW = 800;
//    int tH = 600;
//    
//    int w = v.contentDimensions.x
//    g.pushMatrix();
//    g.translate(v.home.x, v.home.y);
//    pushMatrix();
//    noFill();
//    noStroke();
//    textureMode(IMAGE);
//    beginShape(PApplet.QUADS);
//    texture(image);
//    
//    
//    
//    vertex(v.topLeft.x, v.topLeft.y, tX, tY);
//    vertex(v.topRight.x, v.topRight.y, tW, tY);
//    vertex(v.bottomRight.x, v.bottomRight.y, tW, tH);
//    vertex(v.bottomLeft.x, v.bottomLeft.y, tX, tH);
//    endShape(PApplet.CLOSE);
//    popMatrix();    
//    g.popMatrix();
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
      if(e.isAltDown() || e.getKeyCode() == ALT)
      {
        System.out.println("SAVING.");
        save();
        
      }
    }
    
    if(e.getKey() == 'e')
    {
      System.out.println("Entering Keystone Calibration");
      keystone.toggleCalibration();
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
  Capture input;
  
  public void setup()
  {
    //size(1920 * 2, 1080, P3D);
    size(1400, 800, P3D);
    smooth();
    noLights();
    keystone = new Keystone(this);
    {//Draw Grid
    image = loadImage("butterfly.jpg");
    PGraphics grid = createGraphics(image.width, image.height);
    //draw a grid
    int spacing = 10;
    int x = image.width / spacing;
    int y = image.height / spacing;

    grid.beginDraw();
    grid.background(255,0,0);
    grid.strokeWeight(2f);
    grid.stroke(255);
    grid.image(image, 0, 0);
    for(int i = 0; i < x; i++)
      grid.line(i * spacing, 0, i * spacing, image.height);
    
    for(int i = 0; i < y; i++)
      grid.line(0, i * spacing, image.width, i * spacing);
    grid.endDraw();
    
//    image.loadPixels();
//    image.pixels = grid.pixels;
//    image.updatePixels();
    
    }
    for(Variables v : monitors)
      if(v != null)
      {
        v.blend = createGraphics(contentInput.width, contentInput.height);
        v.cache = createGraphics(contentInput.width, contentInput.height);
        v.updateBlend();
      }
    
    int resolution = 4;
    int w = 800;
    int h = 600;
    //Update Mesh (from Keystone 5)
    mesh = new float[resolution * resolution][]; //vector(x,y,u,v)
    for (int i = 0; i < mesh.length; i++) {
      float x = (i % resolution) / (float) (resolution - 1);
      float y = (i / resolution) / (float) (resolution - 1);
      mesh[i] = new float[]{x * w, y * h, x * w, y * h};
    }    
    
    //Create Keystone Surfaces
    for(int i = 0; i < monitors.length; i++)
    {
      if(monitors[i] != null)
      {
        surfaces[i] = keystone.createCornerPinSurface(800, 600, 12);
        surfaces[i].x = monitors[i].home.x;
        surfaces[i].y = monitors[i].home.y;
      }
    }
    //Disable for no input devices and use the butterfly
    boolean __enableCamera = false;
    
    if(__enableCamera)
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
  }
  
  @Override
  public boolean sketchFullScreen()
  {
    return false;
  }
  
  public void captureEvent(Capture c)
  {
    input.read();
    input.loadPixels();
    System.out.println("Reading image...");
    
    //Resize on stream change...
    if(image.width != input.height || image.height != input.height)
    {
      image = new PImage(input.width, input.height);
    }
    
    image.loadPixels();
    image.pixels = input.pixels;
    image.updatePixels();
    
    for(Variables v : monitors)
      if(v != null)
      {
        //Write the image crop to the cache.
        v.cache.image(image, 0, 0);//FIXME crop this here...
      }
    
    
//    int[] pixels = input.pixels;
//    for(int i = 0; i < buffer.length; i++)
//      buffer[i] = pixels[i];
   
    //System.out.println("Copied...");
  }
  
  public void drawGradient(PGraphics g, int x, int y, float w, float h, int axis, boolean reverse)
  {
    int black = 0;
    int clear = 0;
    if (reverse)
    {
      clear = this.color(0, 0, 0);
      black = this.color(0, 0, 0, 0.0f);

    }
    else
    {
      black = this.color(0, 0, 0);
      clear = this.color(0, 0, 0, 0.0f);
    }

    if (axis == Y_AXIS)
    { // Top to bottom gradient
      for (int i = y+1; i < y + h; i++)
      {
        float inter = map(i, y, y + h, 0, 1);
        int c = lerpColor(black, clear, inter);
        g.stroke(c);
        g.line(x, i-1, x + w, i-1);
      }
    }
    else
      if (axis == X_AXIS)
      { // Left to right gradient
        for (int i = x+1; i < x + w; i++)
        {
          float inter = map(i, x, x + w, 0, 1);
          int c = lerpColor(black, clear, inter);
          g.stroke(c);
          g.line(i-1, y, i-1, y + h);
        }
      }
  }  
}
