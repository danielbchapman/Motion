package com.danielbchapman.motion.core;

import java.util.ArrayList;
import java.util.HashMap;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.event.KeyEvent;


/**
 * The Motion class is a complete rewrite of Motion for Processing 3.3. This
 * class will focuses on transitions, scenes, state management, show control,
 * and utility methods.
 */
public class Motion extends PApplet
{
  public static int WIDTH = 400;
  public static int HEIGHT = 400;
  //Graphic Contexts
  private PGraphics main3D;
  private PGraphics main2D;
  private PGraphics core;
  private boolean overlayActive = true;
  private PGraphics overlay;
  private boolean debugActive = true;
  private PGraphics debug;
  
  //Drawing Methods
  ArrayList<MotionBrush> activeBrushes = new ArrayList<>();
  
  //Data Structures
  private HashMap<KeyCombo, MotionEvent> keyMap = new HashMap<>();
  private Scene currentScene = null;
  
  public Motion()
  {
  }
  
  @Override
  public void keyPressed(KeyEvent event)
  {
  }
  
  @Override
  public void settings() 
  {
    size(WIDTH, HEIGHT, P3D);
  } 
  
  @Override
  public void setup()
  {
    overlay = createGraphics(width, height, P3D);
    debug = createGraphics(width, height, P3D);
    core = createGraphics(width, height, P3D);
    main2D = createGraphics(width, height, P3D);
    main3D = createGraphics(width, height, P3D);
  }
  
  @Override
  public void draw()
  {
    core.beginDraw();
    long time = System.currentTimeMillis();
    
    if(currentScene != null)
    {
      currentScene.update(time);
      PGraphics sceneCtx = currentScene.is2D() ? main2D : main3D;
      sceneCtx.pushMatrix();
      
      for(MotionBrush b : activeBrushes)
        currentScene.applyBrushBeforeDraw(b, sceneCtx);  
      
      currentScene.draw(sceneCtx);
      
      for(MotionBrush b : activeBrushes)
        currentScene.applyBrushAfterDraw(b, sceneCtx);
      
      currentScene.afterBrushes(sceneCtx);
      
      sceneCtx.popMatrix();
      core.image(sceneCtx, 0, 0, width, height);
    }
    
    if(overlayActive)
    {
      overlay.beginDraw();
      overlay.clear();
      overlay.stroke(0);
      overlay.line(0, 0, mouseX, mouseY);
      overlay.endDraw();
      core.image(overlay, 0, 0, width, height);
    }
    
    if(debugActive)
    {
      debug.beginDraw();
      debug.clear();
      debug.fill(255);
      debug.stroke(255);
      debug.line(width, height,  mouseX, mouseY);
      debug.endDraw();
      core.image(debug, 0, 0, width, height);
    }
    
    core.endDraw();
    g.pushMatrix();
    g.translate(0, 0, -200); //Offset remove later
    g.image(core, 0, 0, width, height);
    g.popMatrix();
    
    //g.image(overlay.getI, width, height);
  }
}
