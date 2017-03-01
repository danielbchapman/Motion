package com.danielbchapman.motion.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
import com.danielbchapman.physics.toxiclibs.Util;
import com.danielbchapman.text.Safe;


/**
 * The Motion class is a complete rewrite of Motion for Processing 3.3. This
 * class will focuses on transitions, scenes, state management, show control,
 * and utility methods.
 */
public class Motion extends PApplet
{
  public static int WIDTH = 400;
  public static int HEIGHT = 400;
  
  public static Map<String, String> KEY_MAP_DEFAULTS;
  public static Map<String, Map<String, String>> PROPERTIES;
  
  static
  {
    PROPERTIES = new HashMap<>();
    KEY_MAP_DEFAULTS = new HashMap<String, String>();
    KEY_MAP_DEFAULTS.put("go", "spacebar");
    KEY_MAP_DEFAULTS.put("record", "r");
    KEY_MAP_DEFAULTS.put("play", "p");
    KEY_MAP_DEFAULTS.put("debug", "d");
    KEY_MAP_DEFAULTS.put("overlay", "f");
    KEY_MAP_DEFAULTS.put("clearOverlay", "c");
    KEY_MAP_DEFAULTS.put("next_scene", "l");
    KEY_MAP_DEFAULTS.put("brush_1", "1");
    KEY_MAP_DEFAULTS.put("brush_2", "2");
    KEY_MAP_DEFAULTS.put("brush_3", "3");
    KEY_MAP_DEFAULTS.put("brush_4", "4");
    KEY_MAP_DEFAULTS.put("brush_5", "5");
    KEY_MAP_DEFAULTS.put("brush_6", "6");
    KEY_MAP_DEFAULTS.put("brush_7", "7");
    KEY_MAP_DEFAULTS.put("brush_8", "8");
    KEY_MAP_DEFAULTS.put("brush_9", "9");
    KEY_MAP_DEFAULTS.put("brush_10", "0");
    
    PROPERTIES.put("key_map", KEY_MAP_DEFAULTS);
  }
  public static void loadProperties()
  {
    HashMap<String, Map<String, String>> settings = new HashMap<>();
    settings.put("key map", KEY_MAP_DEFAULTS);
    Util.readProps("motion.ini", new HashMap<String, Map<String, String>>());
  }
 
  
  /**
   * @param section
   * @param key
   * @param defaultValue
   * @return the setting value from the PROPERTIES file else return the default value
   * and set it in the properties file.  
   */
  public static String getString(String section, String key, String defaultValue)
  {
    Map<String, String> outer = PROPERTIES.get(section);
    if(outer == null){
      outer = new HashMap<String, String>();
      outer.put(key,  defaultValue);
      PROPERTIES.put(section, outer);
      return defaultValue;
    } 
    else 
    {
      String val = outer.get(key);
      if(val == null)
      {
        outer.put(key, defaultValue);
        return defaultValue;
      }
      else
        return val;
    }
  }
  
  /**
   * @param section
   * @param key
   * @param defaultValue
   * @return the setting value from the PROPERTIES file else return the default value
   * and set it in the properties file.  
   *
   */
  //FIXME Java Doc Needed
  public static float getFloat(String section, String key, float defaultValue)
  {
    String val = getString(section, key, Float.toString(defaultValue));
    return Safe.parseFloat(val, 0f);
  }
  
  /**
   * @param section
   * @param key
   * @param defaultValue
   * @return the setting value from the PROPERTIES file else return the default value
   * and set it in the properties file.  
   */  
  public static int getInt(String section, String key, int defaultValue)
  {
    String val = getString(section, key, Float.toString(defaultValue));
    return Safe.parseInteger(val);
  }
  
  //Graphic Contexts
  private PGraphics main3D;
  private PGraphics main2D;
  
  /**
   * The context displayed in the GUI
   */
  private PGraphics core;
  private boolean overlayActive = true;
  private PGraphics overlay;
  private PGraphics overlayPaths;
  private Vec3D overlayLastPoint = null;
  private boolean clearPaths = false;
  private boolean debugActive = true;
  private PGraphics debug;
  
  //Drawing Methods
  ArrayList<Pair<MotionMouseEvent, MotionBrush>> frameEvents = new ArrayList<>();
  ArrayList<MotionMouseEvent> mouseEvents = new ArrayList<>();
  ArrayList<Pair<MotionMouseEvent, MotionBrush>> playBackEvents = new ArrayList<>();
  ArrayList<Playback2017> playbacks = new ArrayList<Playback2017>();
  ArrayList<KeyCombo> keyCombos = new ArrayList<KeyCombo>();
  MotionBrush currentBrush = new MouseBrush();
  
  //Data Structures
  private HashMap<KeyCombo, BiConsumer<Motion, Scene>> keyMap = new HashMap<>();
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
    
    mapKey("go", "spacebar", (app, scene)->{
      go();
      println("go");
    });
    
    mapKey("next_scene", "l", (app, scene)->{ 
      advanceScene();
      println("advance scene");
    });
    
    mapKey("debug", "d", (app, scene)->{
      toggleDebug();
      println("debug");
    });
    
    mapKey("overlay", "f", (app, scene)->{
      toggleOverlay();
      println("overlay");
    });
    
    mapKey("record", "r", (app, scene)->{
      println("recording");
      toggleRecording();
    });
    
    mapKey("playback", "p", (app, scene)->{
      println("playback");
      runPlayback();
    });
    
    mapKey("brush_1", "1", (app, scene)->{
      println("Mouse Brush");
      setCurrentBrush(new MouseBrush());
    });

    mapKey("brush_1", "1", (app, scene)->{
      println("Mouse Brush");
      setCurrentBrush(new MouseBrush());
    });
    mapKey("brush_2", "2", (app, scene)->{
      println("Mouse Brush");
      setCurrentBrush(new VectorMouseBrush());
    });
    mapKey("brush_3", "3", (app, scene)->{
      println("Mouse Brush");
      setCurrentBrush(new TestEllipseBrush(false));
    });
    mapKey("brush_4", "4", (app, scene)->{
      println("Mouse Brush");
      setCurrentBrush(new TestEllipseBrush(true));
    });
    mapKey("brush_5", "5", (app, scene)->{
      println("Mouse Brush");
      setCurrentBrush(new MouseBrush());
    });
    mapKey("brush_6", "6", (app, scene)->{
      println("Mouse Brush");
      setCurrentBrush(new MouseBrush());
    });
    mapKey("brush_7", "7", (app, scene)->{
      println("Mouse Brush");
      setCurrentBrush(new MouseBrush());
    });
    mapKey("brush_8", "8", (app, scene)->{
      println("Mouse Brush");
      setCurrentBrush(new MouseBrush());
    });
    
    mapKey("brush_9", "9", (app, scene)->{
      println("Mouse Brush");
      setCurrentBrush(new MouseBrush());
    });
    
    mapKey("brush_10", "0", (app, scene)->{
      println("Mouse Brush");
      setCurrentBrush(new MouseBrush());
    });
    
    mapKey("clearOverlay", "c", (app, scene)->{
      println("Clear Overlay");
      clearOverlay();
    });
  }
  
  public void mapKey(String command, String keyDefault, BiConsumer<Motion, Scene> action)
  {
    String keyCommand = Motion.getString("key_map", command, keyDefault);
    //FIXME Split this out to the commands like alt+ctrl+ etc...
    //FIXME add special notes here...
    Character key = null;
    if(keyCommand.equals("spacebar")){
      key = ' ';
    } else 
    {
      key = keyCommand.charAt(0); //first char  
    }
    
    KeyCombo keyCombo = new KeyCombo(key);
    keyMap.put(keyCombo, action);
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
    
    BiConsumer<Motion, Scene> action = keyMap.get(down);
    println(down);
    println("Action------");
    if(action != null)
    {
      println(action);
      action.accept(this, currentScene);
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
    overlayPaths = createGraphics(width, height, P3D);
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
    
    ArrayList<Playback2017> remove = new ArrayList<Playback2017>();
    if(playbacks.size() > 0)
      for(int i = 0; i < playbacks.size(); i++)
      {
        Playback2017 pb = playbacks.get(i);
        
        if(pb.isRunning())
        {
          pb.poll(this);
        }
        else if (pb.isCompleted())
        {
          remove.add(pb);
        } 
        else 
        {
          pb.start(time);
          pb.poll(this);
        }
      }
    
    if(remove.size() > 0)
      for(Playback2017 pb : remove)
      {
        playbacks.remove(pb);
        println("Playback complete for " + pb.getLabel());
      }

    
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
              //print("[vector]");
              if(b.last == null)//Start draw
              {
                currentScene.applyBrush(b, main, point);
                //println("first");
              }
              else
              {
                Vec3D scalarPoint = new Vec3D(point);
                Vec3D scalar = scalarPoint.sub(b.last);
                float mag = scalar.magnitude();
                int steps = (int) (mag / b.splitSize);
                //print(" [steps] " + steps);
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
                  //  print(" [@] (" + copy.x + ", " + copy.y + ", " + copy.z + ")");
                    currentScene.applyBrush(b,  main, copy);
                  }
                  
                  //Draw at point for the last one
                  currentScene.applyBrush(b, main, point);
                  //println(" [@] (" + point.x + ", " + point.y + ", " + point.z + ")");
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
    
    
    if(overlayActive)
    {
      //Draw paths
      overlayPaths.beginDraw();
      if(clearPaths)
      {
        overlayPaths.clear();
        clearPaths = false;
        overlayLastPoint = null;
      }
      overlayPaths.strokeWeight(3);
      
      int size = frameEvents.size();
      MotionMouseEvent e = null;
      for(Pair<MotionMouseEvent, MotionBrush> frame : frameEvents)
      {
        MotionMouseEvent current = frame.getOne();
        if(!current.anyDown())
          continue;
        
        overlayPaths.stroke(current.debugColor);
        overlayPaths.fill(current.debugColor);
        if(overlayLastPoint != null)
        {
          overlayPaths.line(
              current.x, 
              current.y, 
              current.z, 
              overlayLastPoint.x,  
              overlayLastPoint.y,  
              overlayLastPoint.z );
        }
        else
        {
          overlayPaths.point(current.x, current.y, current.z); 
        }
        
        overlayLastPoint = new Vec3D(current);
      }
      
      if(size == 1)
      {

        MotionMouseEvent one = frameEvents.get(0).getOne();
        if(one.left || one.right || one.center)
        {

        }
      } 
      else if (size > 1)
      {
        MotionMouseEvent current;
        MotionMouseEvent next;
        for(int i = 1; i < size; i++)
        {
          current = frameEvents.get(i-1).getOne();
          next = frameEvents.get(i-1).getOne();
          
          if(current.anyDown() && next.anyDown())
          {
            overlayPaths.line(current.x,  current.y, current.z, next.x, next.y,  next.z);
          }
          else if(current.anyDown())
          {
            overlayPaths.point(current.x,  current.y, current.z);
          }
          else if (next.anyDown())
          {
            overlayPaths.point(next.x,  next.y, next.z);
          }
        }
      }
      
      overlayPaths.endDraw();
      core.image(overlayPaths, 0, 0, width, height);
      
      //Draw overlay
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
    
    //Clear data
    frameEvents.clear();
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
    clearOverlay();
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
    Playback2017 pb = recorder.playback("_live", CAPTURE, this, this.currentBrush.copy());
    playbacks.add(pb);
    println("PLAYBACK ADDED");
    println(pb);
  }
  
  /**
   * A hook that allows an action to be "Played" on the screen
   * @param action the action to apply
   * 
   */
  public void robot(RecordAction2017 action, MotionBrush brush)
  {
    if(brush == null){
      System.out.println("Can not playback without a valid brush");
      return;
    }
    
    MotionMouseEvent event = new MotionMouseEvent();
    event.left = action.leftClick;
    event.right = action.rightClick;
    event.center = action.centerClick;
    
    event.x = action.x;
    event.y = action.y;
    event.z = action.z;
    
    event.pmouseX = action.px;
    event.pmouseY = action.py;
    event.pmouseZ = action.pz;
    
    frameEvents.add(Pair.create(event, brush));
    
  }
  
  @Override
  public void dispose()
  {
    System.out.println("Exiting motion...");
    try
    {
      Util.writeProps("motion.ini", Motion.PROPERTIES);  
    }
    catch(Throwable t)
    {
      System.err.println("Error saving motion.ini");
      t.printStackTrace();
    }
    
    super.dispose();
  }
  
  /**
   * Clears the paths on the overlay.  
   */
  public void clearOverlay()
  {
    clearPaths = true;
  }
}
