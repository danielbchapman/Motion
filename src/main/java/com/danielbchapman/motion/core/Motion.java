package com.danielbchapman.motion.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.event.KeyEvent;
import processing.event.MouseEvent;
import shows.test.*;
import toxi.geom.Vec3D;

import com.danielbchapman.brushes.SaveableBrush;
import com.danielbchapman.code.Pair;


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
  ArrayList<Pair<MotionMouseEvent, MotionBrush>> frameEvents = new ArrayList<>();
  ArrayList<MotionMouseEvent> mouseEvents = new ArrayList<>();
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
    
    keyMap.put(new KeyCombo('1'), (app)->{
      println("Mouse Brush");
      setCurrentBrush(new MouseBrush());
    });
    
    keyMap.put(new KeyCombo('2'), (app)->{
      println("Vector Mouse Brush");
      setCurrentBrush(new VectorMouseBrush());
    });
    
    keyMap.put(new KeyCombo('3'), (app)->{
      println("Ellipse Brush");
      setCurrentBrush(new TestEllipseBrush(false));
    });
    
    keyMap.put(new KeyCombo('4'), (app)->{
      println("Ellipse Brush (Vector)");
      setCurrentBrush(new TestEllipseBrush(true));
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
    
    MotionMouseEvent m = new MotionMouseEvent();
    m.x = mouseX;
    m.pmouseX = pmouseX;
    m.y = mouseY;
    m.pmouseY = pmouseY;
    m.left = mouseLeft;
    m.right = mouseRight;
    m.center = mouseCenter;
    mouseEvents.add(m);
  }
  
  public void mouseMoved(MouseEvent event)
  {    
    MotionMouseEvent m = new MotionMouseEvent();
    m.x = mouseX;
    m.pmouseX = pmouseX;
    m.y = mouseY;
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
    
    MotionMouseEvent m = new MotionMouseEvent();
    m.x = mouseX;
    m.pmouseX = pmouseX;
    m.y = mouseY;
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
    
    MotionMouseEvent m = new MotionMouseEvent();
    m.x = mouseX;
    m.pmouseX = pmouseX;
    m.y = mouseY;
    m.pmouseY = pmouseY;
    m.left = mouseLeft;
    m.right = mouseRight;
    m.center = mouseCenter;
    mouseEvents.add(m);
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
  
  /**
   * Updates the brushes in the scene and any other
   * variables that need to be refreshed before drawing begins
   * in the next frame.
   * 
   * This event consolidates the various mouse and key events
   * into logical actions for this frame.
   * 
   * @param time the current time  
   * 
   */
  public void update(long time)
  {
    currentBrush.update(time);    
    //FIXME Add Playback Events Here for each playback
    //Turn recordables into brushes;
    
    //Active UI (Current Brush)
    if(mouseEvents.size() > 0)
    {
      //print("MouseEvents");
      for(int i = 0; i < mouseEvents.size(); i++)
      { 
        MotionMouseEvent e = mouseEvents.get(i);
        frameEvents.add(Pair.create(e, currentBrush));
        
        if(recorder.isRecording())
          recorder.capture(e);
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
    core.background(0,0,0);
    
    if(currentScene != null)
    {
      currentScene.update(time);
      
      PGraphics main = currentScene.is2D() ? main2D : main3D;
      main.beginDraw();
      if(!currentScene.is2D())
        main.pushMatrix();
      
      if(!currentScene.applyBrushesAfterDraw())
      {
        //FIXME Draw logic here.
      }

      currentScene.draw(main);
      
      if(currentScene.applyBrushesAfterDraw())
      {
        for(Pair<MotionMouseEvent, MotionBrush> pair : frameEvents)
        {
          MotionMouseEvent point = pair.getOne();
          MotionBrush b = pair.getTwo();
          
          boolean shouldBeActive = b.checkActive(point);
          if(shouldBeActive)
          {
            if(!b.isDown())
              b.setDown(true, point);
            
            if(!b.isVectorBrush())
              currentScene.applyBrush(b, main, point);           
            else 
            {
              print("[vector]");
              if(b.last == null)//Start draw
              {
                currentScene.applyBrush(b, main, point);
                println("first");
              }
              else
              {
                Vec3D scalarPoint = new Vec3D(point);
                Vec3D scalar = scalarPoint.sub(b.last);
                float mag = scalar.magnitude();
                int steps = (int) (mag / b.splitSize);
                print(" [steps] " + steps);
                if(steps <= 1)
                { 
                  boolean draw = b.applyWhenIdle() || mag >= 1;//pixel-space, so less than 1 is the same point
                  
                  if(draw)
                  {
                    currentScene.applyBrush(b, main, point);
                    b.last = point;
                  }
                }
                else //Multiple points
                {
                  for(int i = 0; i < steps - 1; i++)
                  {
                    float subMag = ((float)i) * b.splitSize;
                    Vec3D newSub = scalar.getNormalizedTo(subMag);
                    Vec3D newPoint = b.last.add(newSub);
                    MotionMouseEvent copy = point.copy(newPoint);
                    print(" [@] (" + copy.x + ", " + copy.y + ", " + copy.z + ")");
                    currentScene.applyBrush(b,  main, copy);
                  }
                  
                  //Draw at point for the last one
                  currentScene.applyBrush(b, main, point);
                  println(" [@] (" + point.x + ", " + point.y + ", " + point.z + ")");
                  b.last = point;
                }
              }
            }
          }
          else
          {
            if(b.isDown())
              b.setDown(false);
          }
        }        
      }
      
      currentScene.afterBrushes(main);
      
      if(!currentScene.is2D())
        main.popMatrix();
      
      main.endDraw();
      core.image(main, 0, 0, width, height);
    }
    
    frameEvents.clear();
    
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
  
  public void setCurrentBrush(MotionBrush brush)
  {
    if(brush != null)
      this.currentBrush = brush;
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
  public void robot(MotionMouseEvent action, MotionBrush brush)
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
