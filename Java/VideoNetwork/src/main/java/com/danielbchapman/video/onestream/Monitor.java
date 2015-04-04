package com.danielbchapman.video.onestream;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.Function;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;

import com.danielbchapman.utility.FileUtil;
import com.danielbchapman.video.blending.Vec2;

public class Monitor implements Serializable
{
  PApplet parent;
  
  private static final long serialVersionUID = 1L;
  // Content
  Vec2 contentStart = new Vec2();
  Vec2 contentDimensions = new Vec2();

  // Blending Variables
  int thickness = 100;
  int distance = 0;
  Vec2 blendTop = new Vec2(0,0);
  Vec2 blendRight = new Vec2(0,0);
  Vec2 blendBottom = new Vec2(0,0);
  Vec2 blendLeft = new Vec2(0,0);

  // Blending Variables
  Vec2 topLeft = new Vec2();
  Vec2 topRight = new Vec2();
  Vec2 bottomRight = new Vec2();
  Vec2 bottomLeft = new Vec2();

  Vec2 home = new Vec2();
  String name = "Default";

  transient PGraphics blend;
  transient PGraphics cache;

  int loadIndexInt = 0;

  public Monitor(PApplet parent, int x, int y)
  {
    this(parent, 0, 0, x, 0, x, y, 0, y, "Default", 0, 0);
  }

  public Monitor(PApplet parent, int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4, String name, int homeX, int homeY)
  {
    this.parent = parent;
    topLeft = new Vec2(x1, y1);
    topRight = new Vec2(x2, y2);
    bottomRight = new Vec2(x3, y3);
    bottomLeft = new Vec2(x4, y4);

    home.x = homeX;
    home.y = homeY;
    this.name = name;
  }

  public void load(String file)
  {
    //Set variables here:
    loadIndexInt = 0;
    ArrayList<String> lines = FileUtil.readLines(file);
    Function<ArrayList<String>, Vec2> read = (list) -> 
    {
      //Lines 1 == doc
      String[] data = list.get(loadIndexInt + 1).split(",");
      loadIndexInt += 2;
      return new Vec2(Integer.valueOf(data[0]), Integer.valueOf(data[1]));
    };
    
    bottomLeft = read.apply(lines);
    bottomRight = read.apply(lines);
    topLeft = read.apply(lines);
    topRight = read.apply(lines);
    
    blendTop = read.apply(lines);
    blendRight = read.apply(lines);
    blendBottom = read.apply(lines);
    blendLeft = read.apply(lines);
    
    contentStart = read.apply(lines);
    contentDimensions = read.apply(lines);
    
    home = read.apply(lines);
    Vec2 blend = read.apply(lines);
    thickness = blend.x;
    distance = blend.y;
  }

  public void save(String file)
  {
    StringBuilder out = new StringBuilder();
    
    BiConsumer<Vec2, String> writeDoc = (v, l)->
    {
      out.append(l);
      out.append("\r\n");
      out.append(v.x);
      out.append(",");
      out.append(v.y);
      out.append("\r\n");
    };
    
    writeDoc.accept(bottomLeft, "Bottom Left");
    writeDoc.accept(bottomRight, "Bottom Right");
    writeDoc.accept(topLeft, "Top Left");
    writeDoc.accept(topRight, "Top Right");
    
    writeDoc.accept(blendTop, "Blend Top Left");
    writeDoc.accept(blendRight, "Blend Top Right");
    writeDoc.accept(blendRight, "Blend Bottom Right");
    writeDoc.accept(blendBottom, "Blend Bottom Left");
    
    writeDoc.accept(contentStart, "Content Start");
    writeDoc.accept(contentDimensions, "Content Dimensions");
    writeDoc.accept(home, "Home");
    writeDoc.accept(new Vec2(thickness, distance), "TotalBlend");
    
    //Skiping name...
    
    FileUtil.writeFile(file, out.toString().getBytes());
    
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
    if (blend == null)
      throw new RuntimeException("Unable to blend uninitialized graphics object");

    //int thickness = 100;
    int white = parent.color(0,0,0, 255f);
    //int black = parent.color(blendTop.y, blendTop.y, blendTop.y, 255);
    int black = parent.color(0,0,0,0f);
    blend.beginDraw();
    blend.clear();
    blend.noStroke();
    blend.background(255);
    blend.noFill();
    
    boolean __drawCurves = false;
    boolean __left = true;
    boolean __right = true;
    boolean __top = true;
    boolean __bottom = true;
    //TOP
    if(__drawCurves){//TOP if left... if right
      int x = 0;
      int y = 0;
      int w = blend.width;
      int h = blend.height;
      if(__left){
        w-= thickness+1;
        x+=thickness-1;
        blend.fill(0);
        blend.rect(0,0,thickness,thickness);
        blend.noFill();
        DrawingUtil.drawGradientRadial(blend, white, black, thickness, thickness, thickness, PConstants.PI, PConstants.PI+PConstants.HALF_PI);
        
      }
      if(__right){
        w-= thickness-1;
      }
      DrawingUtil.drawGradient(blend, white, black, x, y, w, thickness, DrawingUtil.Y_AXIS);
    }
    //BOTTOM
    if(__drawCurves){//if left... if right
      if(__left){
        
      }
      
      if(__right){
        
      }
      //DrawingUtil.drawGradient(blend, white, black, 0, blend.height - thickness, blend.width, thickness, DrawingUtil.Y_AXIS);  
    }
    
    /*
     * This needs better logic.
     * Basically we need to draw a radial gradient for the corners
     */
    int t = thickness;
    int w = blend.width;
    int h = blend.height;
    DrawingUtil.drawGradient(blend, white, black, 0, 0-distance, w, t+distance, DrawingUtil.Y_AXIS);//TOP
    DrawingUtil.drawGradient(blend, black, white, 0, h - t+distance, w, t+distance, DrawingUtil.Y_AXIS);//BOTTOM
    DrawingUtil.drawGradient(blend, white, black, 0-distance, 0, t+distance, h, DrawingUtil.X_AXIS);//LEFT
    DrawingUtil.drawGradient(blend, black, white, w - t+distance, 0, t+distance, h, DrawingUtil.X_AXIS);//RIGHT
    blend.endDraw();
  }

  
}