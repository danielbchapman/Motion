package com.danielbchapman.motion.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.function.Consumer;

import com.danielbchapman.brushes.SaveableBrush;
import com.danielbchapman.physics.toxiclibs.MotionInteractiveBehavior;
import com.danielbchapman.physics.toxiclibs.RecordAction;
import com.danielbchapman.physics.toxiclibs.Recorder;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.event.KeyEvent;
import processing.event.MouseEvent;
import shows.test.TestBrushScene;
import shows.test.TestFluidScene;
import shows.test.TestVerletScene;
import toxi.geom.Vec3D;


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
  ArrayList<RecordableMouse> mouseEvents = new ArrayList<>();
  ArrayList<KeyCombo> keyCombos = new ArrayList<KeyCombo>();
  MotionBrush currentBrush = new MouseBrush();
  
  //Data Structures
  private HashMap<KeyCombo, Consumer<Motion>> keyMap = new HashMap<>();
  private Scene currentScene = null;
  
  private ArrayList<Scene> scenes = new ArrayList<>();
  private int sceneIndex = -1;
  
  //Recording
  Recorder2017 recorder = new Recorder2017();
  static ArrayList<RecordAction2017> CAPTURE;
  
  //Mouse and Keyboard
  boolean mouseLeft = false;
  boolean mouseRight = false;
  boolean mouseCenter = false;
  public Motion()
  {
    //SpaceBar = 32
    KeyCombo go = new KeyCombo(' ');
    KeyCombo nextScene = new KeyCombo('l');
    KeyCombo debug = new KeyCombo('d');
    KeyCombo overlay = new KeyCombo('f');
    KeyCombo record = new KeyCombo('r');
    KeyCombo playback = new KeyCombo('p');
    
    keyMap.put(go, (app)->{
      go();
      println("go");
    });
    
    keyMap.put(nextScene, (app)->{ 
      advanceScene();
      println("advance scene");
    });
    
    keyMap.put(debug, (app)->{
      toggleDebug();
      println("debug");
    });
    
    keyMap.put(overlay, (app)->{
      toggleOverlay();
      println("overlay");
    });
    
    keyMap.put(record, (app)->{
      println("recording");
      toggleRecording();
    });
    
    keyMap.put(playback, (app)->{
      println("playback");
      runPlayback();
    });
  }
  
  @Override
  public void keyPressed(KeyEvent event)
  {
    println(event);
    KeyCombo down = new KeyCombo();
    down.character = event.getKey();
    down.alt = event.isAltDown();
    down.ctrl = event.isControlDown();
    down.shift = event.isShiftDown();

    if(currentScene != null)
    {
      HashMap<KeyCombo, Consumer<Motion>> sceneMap = currentScene.getKeyMap();
      if(sceneMap != null)
      {
        Consumer<Motion> action = sceneMap.get(down);
        if(action != null)
        {
          action.accept(this);
          println("\tConsume by scene");
          return;
        }
      }
    }
    Consumer<Motion> action = keyMap.get(down);
    println(down);
    println("Action------");
    if(action != null)
    {
      println(action);
      action.accept(this);
    }
    else
    {
      println("\tNo action..");
      keyCombos.add(down);
    }

    
  }
  
  public void mouseDragged(MouseEvent event)
  {
    super.mouseDragged(event);
    if(currentBrush != null)
    {
      currentBrush.mouseX = mouseX;
      currentBrush.pmouseX = pmouseX;
      currentBrush.mouseY = mouseY;
      currentBrush.pmouseY = pmouseY;
    }
    
    RecordableMouse m = new RecordableMouse();
    m.brush = currentBrush;
    m.mouseX = mouseX;
    m.pmouseX = pmouseX;
    m.mouseY = mouseY;
    m.pmouseY = pmouseY;
    m.left = mouseLeft;
    m.right = mouseRight;
    m.center = mouseCenter;
    mouseEvents.add(m);
  }
  
  public void mouseMoved(MouseEvent event)
  {
    if(currentBrush != null)
    {
      currentBrush.mouseX = mouseX;
      currentBrush.pmouseX = pmouseX;
      currentBrush.mouseY = mouseY;
      currentBrush.pmouseY = pmouseY;      
    }
    
    RecordableMouse m = new RecordableMouse();
    m.brush = currentBrush;
    m.mouseX = mouseX;
    m.pmouseX = pmouseX;
    m.mouseY = mouseY;
    m.pmouseY = pmouseY;
    m.left = mouseLeft;
    m.right = mouseRight;
    m.center = mouseCenter;
    mouseEvents.add(m);
  }
  
  @Override
  public void mouseReleased(MouseEvent e)
  {
    if(mouseButton == LEFT)
      mouseLeft = false;
    else if (mouseButton == RIGHT)
      mouseRight = false;
    else if (mouseButton == CENTER)
      mouseCenter = false;
 
    
    if(currentBrush != null)
    {
      currentBrush.left = mouseLeft;
      currentBrush.right = mouseRight;
      currentBrush.center = mouseCenter;
    }
      
    
    RecordableMouse m = new RecordableMouse();
    m.brush = currentBrush;
    m.mouseX = mouseX;
    m.pmouseX = pmouseX;
    m.mouseY = mouseY;
    m.pmouseY = pmouseY;
    m.left = mouseLeft;
    m.center = mouseCenter;
    m.right = mouseRight;
    mouseEvents.add(m);    
  }
  
  @Override
  public void mousePressed()
  {    
    if(mouseButton == LEFT)
      mouseLeft = true;
    else if (mouseButton == RIGHT)
      mouseRight = true;
    else if (mouseButton == CENTER)
      mouseCenter = true;
    
    RecordableMouse m = new RecordableMouse();
    m.brush = currentBrush;
    m.mouseX = mouseX;
    m.pmouseX = pmouseX;
    m.mouseY = mouseY;
    m.pmouseY = pmouseY;
    m.left = mouseLeft;
    m.right = mouseRight;
    m.center = mouseCenter;
    mouseEvents.add(m);
    
    if(currentBrush != null)
    {
      currentBrush.left = mouseLeft;
      currentBrush.right = mouseRight;
      currentBrush.center = mouseCenter;
    }
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
    main3D = createGraphics(width, height, P3D);
    main2D = createGraphics(width, height, P2D);
    
    postSetup();
  }
  
  //Add layers etc...
  public void postSetup()
  {
    Consumer<Scene> prep = (scene)-> {
      scenes.add(scene);
      println(scene.getName());
    };
    
    prep.accept(new TestBrushScene());
    prep.accept(new TestFluidScene());
    prep.accept(new TestVerletScene());
  }
  
  public void update(long time)
  {
    //Turn recordables into brushes;
    
    if(mouseEvents.size() > 0)
    {
      //print("MouseEvents");
      for(int i = 0; i < mouseEvents.size(); i++)
      { 
        //We should probably deterimine the "mousedown" situation here"
        RecordableMouse r = mouseEvents.get(i);
        //print(" [" + r.mouseX + ", " + r.mouseY + " @ " + r.mouseDown + " ]");
        if(currentBrush != null && (r.left || r.right || r.center))
        {
          activeBrushes.add(currentBrush.copy());
        }
        if(recorder.isRecording())
        {
          recorder.capture(r);
        }
      }

      mouseEvents.clear();
      //println("");
    }
  }
  
  @Override
  public void draw()
  {
    long time = System.currentTimeMillis();
    update(time);
    
    core.beginDraw();
    core.background(0);
    
    if(currentScene != null)
    {
      currentScene.update(time);
      PGraphics main = currentScene.is2D() ? main2D : main3D;
      if(!currentScene.is2D())
        main.pushMatrix();
      
      for(MotionBrush b : activeBrushes)
        if(b.isActive())
          currentScene.applyBrushBeforeDraw(b, main);  
      if(currentBrush != null)
        if(currentBrush.isActive())
          currentScene.applyBrushBeforeDraw(currentBrush, main);
      
      currentScene.draw(main);
      
      for(MotionBrush b : activeBrushes)
        if(b.isActive())
          currentScene.applyBrushAfterDraw(b, main);
      
      if(currentBrush != null && currentBrush.isActive())
        if(currentBrush.isActive())
          currentScene.applyBrushAfterDraw(currentBrush, main);      
      
      currentScene.afterBrushes(main);
      
      if(!currentScene.is2D())
        main.popMatrix();
      
      core.image(main, 0, 0, width, height);
    }
    
    activeBrushes.clear();
    
    if(overlayActive)
    {
      overlay.beginDraw();
      overlay.clear();
      int red = mousePressed ? 255 : 0;
      
      overlay.pushMatrix();
      overlay.translate(mouseX, mouseY);
      overlay.ellipseMode(CENTER);
      overlay.fill(0,0,0,0);
      overlay.stroke(red, 255, 0, 128);
      overlay.ellipse(0, 0, 16, 16);
      overlay.ellipse(0, 0, 32, 32);
      overlay.ellipse(0, 0, 64, 64);   
      
      overlay.stroke(red, 255, 0, 255);
      overlay.line(0, -64, 0, 64);
      overlay.line(-64, 0, 64, 0);
      
      overlay.fill(red, 255, 0, 255);
      overlay.stroke(0, 0, 0, 0);
      overlay.text("[" +  mouseX + ", " + mouseY + "]", 0, 0);
      overlay.popMatrix();

      overlay.endDraw();
      core.image(overlay, 0, 0, width, height);
    }
    
    if(debugActive)
    {
      debug.beginDraw();
      debug.clear();
      
      if(recorder.isRecording())
      {
        debug.pushMatrix();
        debug.fill(255, 0, 0, 255);
        debug.text("[RECORDING]", 20, 20);
        debug.popMatrix();
      }
      //20 high
      Consumer<String> debugText = (text)->
      {
        debug.fill(0, 0, 0, 255);
        debug.stroke(0, 255, 0);
        debug.rect(10, 5, 140, 25);
        debug.fill(0, 255, 0, 255);
        debug.text(text, 20, 20);        
      };
      
      debug.pushMatrix();
      debug.translate(width - 160, 0);
      debugText.accept("Rate " + frameRate);
      debug.popMatrix();
      
      debug.pushMatrix();
      debug.translate(width - 160, 30);
      debugText.accept(currentScene == null ? "Unknown Scene" : currentScene.getName());
      debug.popMatrix();

      debug.endDraw();
      core.image(debug, 0, 0, width, height);
    }
    
    core.endDraw();
    g.pushMatrix();
    g.translate(0, 0, -200); //Offset remove later
    g.image(core, 0, 0, width, height);
    g.popMatrix(); 
  }
  
  public void advanceScene()
  {
    sceneIndex++;
    if(sceneIndex < 0 || sceneIndex >= scenes.size())
      sceneIndex = 0;
    
    if(scenes.size() > 0)
    {
      if(currentScene != null)
      {
        if(!currentScene.isPersistent())
        {
          currentScene.shutdown();
          currentScene.initialized = false;
        }
      }
      
      currentScene = scenes.get(sceneIndex);
      if(!currentScene.initialized)
      {
        currentScene.initialize(this);
        currentScene.initialized = true;
      }
        
    }
  }
  
  public void rest()
  {
    if(currentScene != null && !currentScene.isPersistent())
    {
      currentScene.shutdown();
      currentScene.initialized = false;
      currentScene = null;
    }
  }
  
  public void go()
  {
    if(currentScene != null)
      currentScene.go();
  }
  
  public void toggleOverlay()
  {
    overlayActive = !overlayActive;
  }
  
  public void toggleDebug()
  {
    debugActive = !debugActive;
  }

  public void toggleRecording()
  {
    if(recorder.isRecording())
      CAPTURE = recorder.stop();
    else
      recorder.start();
  }
  
  public void runPlayback()
  {
    if(CAPTURE == null || CAPTURE.size() < 1)
    {
      println("Nothing to play back");
      return;
    }
    
    Playback2017 pb = recorder.playback(CAPTURE, currentScene, this);
  }
  
  
  /**
   * A hook that allows an action to be "Played" on the screen
   * @param action the action to apply
   * 
   */
  public void robot(RecordableMouse action, MotionBrush brush)
  {
    if(action.left)
    {
      
    }
    else if (action.right)
    {
      
    }
    else if (action.center)
    {
      
    }
    // System.out.println("Recorder Running");
    SaveableBrush paint = null;
    if (behavior instanceof SaveableBrush)
      paint = (SaveableBrush) behavior;

    if (action.leftClick)
    {
      behavior.vars.position = new Vec3D(action.x, action.y, 0);
      if (paint != null)
      {
        if (!paint.isDrawing())
          paint.startDraw();

        behavior.setPosition(behavior.vars.position);
        paintBrushes.add(paint);
      }

      addBehavior(behavior);
    }
    else
    {
      if (paint != null)
        paint.endDraw();
      removeBehavior(behavior);
    }
  }
}
