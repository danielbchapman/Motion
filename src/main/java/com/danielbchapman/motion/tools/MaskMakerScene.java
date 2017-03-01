package com.danielbchapman.motion.tools;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;

import processing.core.PConstants;
import processing.core.PGraphics;
import toxi.geom.Vec2D;

import com.danielbchapman.motion.core.BaseScene;
import com.danielbchapman.motion.core.KeyCombo;
import com.danielbchapman.motion.core.Motion;
import com.danielbchapman.motion.core.MotionBrush;
import com.danielbchapman.motion.core.MotionMouseEvent;
import com.danielbchapman.motion.core.MouseBrush;
import com.danielbchapman.utility.FileUtil;

public class MaskMakerScene extends BaseScene
{
  HashMap<KeyCombo, Consumer<Motion>> keys = new HashMap<>();
  
  ArrayList<ArrayList<Vec2D>> shapes;
  ArrayList<Vec2D> currentShape;
  Vec2D mousePosition;
  boolean clearFirst;
  int targetSize = 8;
  String[] help;
  @Override
  public void initialize(Motion app)
  {
    shapes = new ArrayList<>();
    currentShape = new ArrayList<>();
    mousePosition = new Vec2D();
    clearFirst = true;
    
    keys.put(new KeyCombo('a'), (motion)->{
      System.out.println("Adding Shape");
      closeShape();
    });
    
    keys.put(new KeyCombo('r'), (motion)->{
      System.out.println("Clearing context");
      clearFirst = true;
    });
    
    keys.put(new KeyCombo('t'), (motion)->{
      System.out.println("Load the most recently saved mask");
      System.err.println("Great feature, not implemented yet...");
    });
    
    keys.put(new KeyCombo('i'), (motion)->{
      System.out.println("Take Screen Shot");
      motion.takeScreenShot("mask-");
    });
    
    keys.put(new KeyCombo((char)KeyEvent.VK_BACK_SPACE), (motion)->{
      System.out.println("BACKSPACE");
    });
    try
    {
      ArrayList<String> data = FileUtil.readLines("help/maskmaker.debug");
      help = new String[data.size()];
      for(int i = 0; i < data.size(); i++)
        help[i] = data.get(i);
    }
    catch(Throwable t)
    {
      help = new String[]{"help/maskmaker.debug is missing"};
    }
    
  }

  @Override
  public void applyBrush(MotionBrush brush, PGraphics g, MotionMouseEvent point)
  {
    mousePosition = new Vec2D(point.x, point.y);
    if(point.left)
    {
      currentShape.add(new Vec2D(point.x, point.y));
    }
  }

  @Override
  public void afterBrushes(PGraphics g)
  { 
  }

  @Override
  public void go(Motion motion)
  { 
    motion.setCurrentBrush(new MouseBrush());
  }

  @Override
  public boolean isPersistent()
  { 
    return false;
  }

  @Override
  public String getName()
  {
    return getClass().getSimpleName();
  }
  
  @Override
  public HashMap<KeyCombo, Consumer<Motion>> getKeyMap()
  {
    return keys;
  }

  @Override
  public void update(long time)
  {
  }

  @Override
  public void draw(PGraphics g)
  { 
    Consumer<ArrayList<Vec2D>> drawShape = (list)->
    {
      g.beginShape();
      for(Vec2D p : list)
      {
        g.vertex(p.x, p.y);          
      }
      g.endShape();        
    };

    g.stroke(255);
    g.fill(255);
    for(ArrayList<Vec2D> shape : shapes)
      drawShape.accept(shape);
    
    drawShape.accept(currentShape);
    
    if(clearFirst)
    {
      g.background(0);
      clearFirst = false;
    }
    
    //Draw in the screenshot if provided...
  }
  
  @Override
  public void overlay(PGraphics clear)
  { 
    clear.strokeWeight(3);
    clear.fill(255, 255, 0, 128);
    clear.stroke(255, 255, 0);
    clear.ellipseMode(PConstants.CENTER);
    clear.ellipse(0, 0, 24, 24);
    
    //TODO Highlight when mouse over...
    Consumer<ArrayList<Vec2D>> drawShape = (list)->
    {
      for(Vec2D p : list)
      {
        clear.stroke(0, 255, 0);
        clear.fill(0,0,0,0);
        clear.ellipse(p.x, p.y, 16, 16);
        clear.line(p.x - targetSize, p.y, p.x+ targetSize, p.y);
        clear.line(p.x, p.y - targetSize, p.x, p.y + targetSize);
      }
    };
    
    shapes.forEach(drawShape);
    drawShape.accept(currentShape);
  }

  @Override
  public void overlayPaths(PGraphics overlayPaths)
  {  
  }
  
  @Override
  public void shutdown()
  { 
  }

  public void closeShape()
  {
    if(currentShape != null && currentShape.size() > 1)
    {
      shapes.add(currentShape);
      currentShape = new ArrayList<>();
    }
  }
  
  @Override
  public void debug(PGraphics g)
  {
    //Show help
    for(int i = 0; i < help.length; i++)
    {
      g.stroke(255, 255, 0);
      g.fill(255, 255, 0);
      g.text(help[i], 50, 20 + 20 * i);
    }
  }
}
