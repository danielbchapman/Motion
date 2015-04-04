package com.danielbchapman.video.onestream;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.Function;

import processing.core.PApplet;
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
  Vec2 blendTopLeft = new Vec2();
  Vec2 blendTopRight = new Vec2();
  Vec2 blendBottomLeft = new Vec2();
  Vec2 blendBottomRight = new Vec2();

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
    
    blendTopLeft = read.apply(lines);
    blendTopRight = read.apply(lines);
    blendBottomRight = read.apply(lines);
    blendBottomLeft = read.apply(lines);
    
    contentStart = read.apply(lines);
    contentDimensions = read.apply(lines);
    
    home = read.apply(lines);
    System.out.println("Home is -> " + home);
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
    
    writeDoc.accept(blendTopLeft, "Blend Top Left");
    writeDoc.accept(blendTopRight, "Blend Top Right");
    writeDoc.accept(blendBottomRight, "Blend Bottom Right");
    writeDoc.accept(blendBottomLeft, "Blend Bottom Left");
    
    writeDoc.accept(contentStart, "Content Start");
    writeDoc.accept(contentDimensions, "Content Dimensions");
    writeDoc.accept(home, "Home");
    
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

    int thickness = 100;
    int black = 0x00000000;
    int alpha = 0x000000FF;
    blend.beginDraw();
    blend.clear();
    blend.noStroke();
    blend.background(255);
    blend.noFill();
    DrawingUtil.drawGradient(blend, black, alpha, 0, 0, blend.width, thickness, DrawingUtil.Y_AXIS);
    DrawingUtil.drawGradient(blend, alpha, black, 0, blend.height - thickness, blend.width, thickness, DrawingUtil.Y_AXIS);
    DrawingUtil.drawGradient(blend, black, alpha, 0, 0, thickness, blend.height, DrawingUtil.X_AXIS);
    DrawingUtil.drawGradient(blend, alpha, black, blend.width - thickness, 0, thickness, blend.height, DrawingUtil.X_AXIS);
    blend.endDraw();
  }
  
//  public void updateGradient(Monitor v, int w, int h)
//  {
//    if (blend == null || blend.width != w || blend.height != h)
//    {
//      if (blend != null)
//        blend.dispose();
//
//      blend = parent.createGraphics(w, h);
//    }
//
//    blend.noFill();
//    blend.noStroke();
//    blend.beginDraw();
//    
//    int thick = 100;// alter this later..
//    int black = 0x00000000;
//    int alpha = 0x000000FF;
//    
//    DrawingUtil.drawGradient(blend, black, alpha, 0, 0, w, thick, DrawingUtil.X_AXIS); //Top
//    DrawingUtil.drawGradient(blend, alpha, black, w - thick, 0, thick, h, DrawingUtil.Y_AXIS);//Right
//    DrawingUtil.drawGradient(blend, alpha, black, 0, h - thick, w, thick, DrawingUtil.X_AXIS);//Bottom
//    DrawingUtil.drawGradient(blend, black, alpha, 0, 0, thick, h, DrawingUtil.Y_AXIS);//Left
//    
//    blend.endDraw();
//    blend.loadPixels();
//  }
}