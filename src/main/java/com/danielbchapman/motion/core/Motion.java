package com.danielbchapman.motion.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.logging.Level;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.event.KeyEvent;
import processing.event.MouseEvent;
import shows.test.TestBrushScene;
import shows.test.TestEllipseBrush;
import shows.test.TestFluidScene;
import shows.test.TestVerletScene;
import toxi.geom.Vec3D;

import com.danielbchapman.code.Pair;
import com.danielbchapman.motion.tools.MaskMakerScene;
import com.danielbchapman.physics.toxiclibs.IGraphicShare;
import com.danielbchapman.physics.toxiclibs.Util;
import com.danielbchapman.text.Safe;
import com.sun.jna.Platform;


/**
 * The Motion class is a complete rewrite of Motion for Processing 3.3. This
 * class will focuses on transitions, scenes, state management, show control,
 * and utility methods.
 */
public class Motion extends PApplet
{
  public static int WIDTH = 400;
  public static int HEIGHT = 400;
  
  public static String KEY_MAP = "KEY_MAP";
  public static String GRAPHICS = "GRAPHICS";
  public static Map<String, String> KEY_MAP_DEFAULTS;
  public static Map<String, String> GRAPHICS_DEFAULTS;
  public static Map<String, String> MOTION_DEFAULTS;
  public static Map<String, Map<String, String>> PROPERTIES;
  
  public static int[] PLAYBACK_COLORS = {
    0xFFFFFF00,
    0xFFFF00FF,
    0xFF00FFFF,
    0xFFFF0000,
    0xFF00FF00,
    0xFF0000FF
  };
  
  public static int PLAYBACK_COLOR_INDEX = -1;
  private static int PLAYBACK_SYSTEM = 0;
  private static int PLAYBACK_SYSTEM_RESET = 1024;
  
  static
  {
    PROPERTIES = new HashMap<>();
    
    KEY_MAP_DEFAULTS = new HashMap<>();
    KEY_MAP_DEFAULTS.put("go", "spacebar");
    KEY_MAP_DEFAULTS.put("record", "r");
    KEY_MAP_DEFAULTS.put("play", "p");
    KEY_MAP_DEFAULTS.put("debug", "d");
    KEY_MAP_DEFAULTS.put("overlay", "f");
    KEY_MAP_DEFAULTS.put("clearOverlay", "c");
    KEY_MAP_DEFAULTS.put("screenshot", "i");
    KEY_MAP_DEFAULTS.put("next_scene", "l");
    KEY_MAP_DEFAULTS.put("open_tools", "t");
    KEY_MAP_DEFAULTS.put("edit", "e");
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
    
    GRAPHICS_DEFAULTS = new HashMap<>();
    GRAPHICS_DEFAULTS.put("syphon", "Motion");
    
    PROPERTIES.put(KEY_MAP, KEY_MAP_DEFAULTS);
    PROPERTIES.put(GRAPHICS, KEY_MAP_DEFAULTS);
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
  private HashMap<Integer, Vec3D> overlayLastPoint = new HashMap<>();
  private boolean clearPaths = false;
  private boolean debugActive = true;
  private PGraphics debug;
  
  //Editor Preview
  private PGraphics editor;
  private boolean editing = false;
  private Cue editCue;
  private MotionMouseEvent editLastPoint;
  
  //Drawing Methods
  ArrayList<EventPair> frameEvents = new ArrayList<>();
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
  private boolean mouseLeft = false;
  private boolean mouseRight = false;
  private boolean mouseCenter = false;
  
  //Actions
  private String screenShotName = null;
  private boolean takeScreenShot = false;
  
  //Tools and Windows
  private Recorder2017.RecordUI recorderUi;
  
  //Syphon and Spout
  private PGraphics pgrTextureClient;
  private Object spoutReceiver;
  private boolean enableTextureServer;
  private boolean enableTextureClient;
  private Object spout;
  private Object spoutClient;
  private IGraphicShare syphonServer;
  private IGraphicShare syphonClient;
  private Method shareInitialize;
  private Method shareCleanup;
  private Method shareDraw;
  private Class<?> spoutClass;
  private Method spoutSendTexture;
  private Method spoutReceiveTexture;
  
  
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
      Log.info("go");
    });
    
    mapKey("next_scene", "l", (app, scene)->{ 
      advanceScene();
      Log.info("advance scene");
    });
    
    mapKey("debug", "d", (app, scene)->{
      toggleDebug();
      Log.info("debug");
    });
    
    mapKey("overlay", "f", (app, scene)->{
      toggleOverlay();
      Log.info("overlay");
    });
    
    mapKey("record", "r", (app, scene)->{
      Log.info("recording");
      toggleRecording();
    });
    
    mapKey("open_tools", "t", (app, scene)->{
      Log.info("show all tools");
      openRecorderUi();
    });

    
    mapKey("edit", "e", (app, scene)->{
      Log.info("toggle edit");
      toggleEdit();
    });
    
    mapKey("playback", "p", (app, scene)->{
      Log.info("playback");
      runPlayback();
    });
    
    mapKey("brush_1", "1", (app, scene)->{
      Log.info("Mouse Brush");
      setCurrentBrush(new MouseBrush());
    });

    mapKey("brush_1", "1", (app, scene)->{
      Log.info("Mouse Brush");
      setCurrentBrush(new MouseBrush());
    });
    mapKey("brush_2", "2", (app, scene)->{
      Log.info("Mouse Brush");
      setCurrentBrush(new VectorMouseBrush());
    });
    mapKey("brush_3", "3", (app, scene)->{
      Log.info("Mouse Brush");
      setCurrentBrush(new TestEllipseBrush(false));
    });
    mapKey("brush_4", "4", (app, scene)->{
      Log.info("Mouse Brush");
      setCurrentBrush(new TestEllipseBrush(true));
    });
    mapKey("brush_5", "5", (app, scene)->{
      Log.info("Mouse Brush");
      setCurrentBrush(new MouseBrush());
    });
    mapKey("brush_6", "6", (app, scene)->{
      Log.info("Mouse Brush");
      setCurrentBrush(new MouseBrush());
    });
    mapKey("brush_7", "7", (app, scene)->{
      Log.info("Mouse Brush");
      setCurrentBrush(new MouseBrush());
    });
    mapKey("brush_8", "8", (app, scene)->{
      Log.info("Mouse Brush");
      setCurrentBrush(new MouseBrush());
    });
    
    mapKey("brush_9", "9", (app, scene)->{
      Log.info("Mouse Brush");
      setCurrentBrush(new MouseBrush());
    });
    
    mapKey("brush_10", "0", (app, scene)->{
      Log.info("Mouse Brush");
      setCurrentBrush(new MouseBrush());
    });
    
    mapKey("clearOverlay", "c", (app, scene)->{
      Log.info("Clear Overlay");
      clearOverlay();
    });
    
    mapKey("screenshot", "i", (app, scene)->{
      Log.info("ScreenShot");
      takeScreenShot(null);
    });
  }
  
  public void mapKey(String command, String keyDefault, BiConsumer<Motion, Scene> action)
  {
    String keyCommand = Motion.getString(KEY_MAP, command, keyDefault);
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
    Log.debug(event);
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
          Log.info("\tConsume by scene");
          return;
        }
      }
    }
    
    BiConsumer<Motion, Scene> action = keyMap.get(down);
    Log.debug(down);
    Log.debug("Action------");
    if(action != null)
    {
      Log.debug(action);
      action.accept(this, currentScene);
    }
    else
    {
      Log.debug("\tNo action..");
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
    editor = createGraphics(width, height, P3D);

    postSetup();
    
    try
    {
      enableTextureBroadcast(core);
    }
    catch (IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | IllegalAccessException | ClassNotFoundException | InstantiationException e)
    {
      e.printStackTrace();
    }
  }
  
  //Add layers etc...
  public void postSetup()
  {
    Consumer<Scene> prep = (scene)-> {
      scenes.add(scene);
      Log.info(scene.getName());
    };
    
    prep.accept(new TestBrushScene());
    prep.accept(new TestFluidScene());
    prep.accept(new TestVerletScene());
    prep.accept(new MaskMakerScene());
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
    currentBrush.setSystem(-1);
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
        Log.info("Playback complete for " + pb.getLabel());
      }

    
    //Active UI (Current Brush)
    if(mouseEvents.size() > 0 && !editing)
    {
      for(int i = 0; i < mouseEvents.size(); i++)
      { 
        MotionMouseEvent e = mouseEvents.get(i);
        frameEvents.add(new EventPair(e, currentBrush));
        
        if(recorder.isRecording())
          recorder.capture(e);
      }

      mouseEvents.clear();
    }
    
    /* We only support one click at a time priority left, then right, 
     * then center 
     */
    if(editing)
    {
      if(editCue != null)
      {
        for(MotionMouseEvent current : mouseEvents)
        {
          if(!current.anyDown())
          {
            editLastPoint = null;
            continue;
          }
          
          //Left Click
          if(current.left && !current.center && !current.right)
          {
            //if not a left click we're done with that action
            if(editLastPoint == null || !editLastPoint.left) 
              editLastPoint = null;
            
            if(editLastPoint != null)
            {
              float deltaX = current.x - editLastPoint.x;
              float deltaY = current.y - editLastPoint.y;
              if(editCue != null)
              {
                editCue.handleEditLeft(deltaX, deltaY);
              }
            }
            
            editLastPoint = current;
          }
          
          //Right Click
          if(current.right && !current.center && !current.left)
          {
            //if not a left click we're done with that action
            if(editLastPoint == null || !editLastPoint.right) 
              editLastPoint = null;
            
            if(editLastPoint != null)
            {
              float deltaX = current.x - editLastPoint.x;
              float deltaY = current.y - editLastPoint.y;
              if(editCue != null)
              {
                editCue.handleEditRight(deltaX, deltaY);
              }
            }
            
            editLastPoint = current;
          }
          
          //Center Click (not all devices)
          if(current.center && !current.right && !current.left)
          {
            //if not a left click we're done with that action
            if(editLastPoint == null || !editLastPoint.center) 
              editLastPoint = null;
            
            if(editLastPoint != null)
            {
              float deltaX = current.x - editLastPoint.x;
              float deltaY = current.y - editLastPoint.y;
              if(editCue != null)
              {
                editCue.handleEditCenter(deltaX, deltaY);
              }
            }
            
            editLastPoint = current;
          }          
        }
      }
      
      mouseEvents.clear();
    }
  }
  public void readTextureShare(PGraphics pg) {
    if (Platform.isWindows() || Platform.isWindowsCE() && enableTextureClient) 
    {
      //spoutReceiveTexture
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
        for(EventPair pair : frameEvents)
        {
          MotionMouseEvent point = pair.event;
          MotionBrush b = pair.brush;
          
          boolean shouldBeActive = b.checkActive(point);
          if(shouldBeActive)
          {
            if(!b.isDown())
              b.setDown(true, point);
            
            if(!b.isVectorBrush())
              currentScene.applyBrush(b, main, point);           
            else 
            {
              if(b.last == null)//Start draw
              {
                currentScene.applyBrush(b, main, point);
              }
              else
              {
                Vec3D scalarPoint = new Vec3D(point);
                Vec3D scalar = scalarPoint.sub(b.last);
                float mag = scalar.magnitude();
                int steps = (int) (mag / b.splitSize);
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
                    currentScene.applyBrush(b,  main, copy);
                  }
                  
                  //Draw at point for the last one
                  currentScene.applyBrush(b, main, point);
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
      if(takeScreenShot)
      {
        String prefix = null;
        if(screenShotName == null)
          prefix = "/screenshots/motion-";
        else
          prefix = "/screenshots/" + screenShotName;
        
        takeScreenShot = false;
        screenShotName = null;

        PImage ss = main.get();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        ss.save(prefix + sdf.format(new Date()) + ".tiff");
      }
      
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
        overlayLastPoint.clear();
      }
      overlayPaths.strokeWeight(3);
      
      int size = frameEvents.size();
      
      for(EventPair current : frameEvents)
      {
        if(!current.event.anyDown())
        {
          //Clear this index from the system
          overlayLastPoint.remove(current.brush.getSystem());
          continue;
        }
        Vec3D lastSystemPoint = overlayLastPoint.get(current.brush.getSystem());        
        
        overlayPaths.stroke(current.event.debugColor);
        overlayPaths.fill(current.event.debugColor);
        
        if(lastSystemPoint != null)
        {
          overlayPaths.line(
              current.event.x, 
              current.event.y, 
              current.event.z, 
              lastSystemPoint.x,  
              lastSystemPoint.y,  
              lastSystemPoint.z );
        }
        else
        {
          overlayPaths.point(current.event.x, current.event.y, current.event.z); 
        }
        
        overlayLastPoint.put(current.getBrush().getSystem(), new Vec3D(current.event));
      }
      
      overlayPaths.endDraw();
      core.image(overlayPaths, 0, 0, width, height);
      
      //Draw overlay
      overlay.beginDraw();
      overlay.clear();
      
      if(currentScene != null)
        currentScene.overlay(overlay);
      
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
      if(currentScene != null)
        currentScene.debug(debug);
      
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
    
    if(editing)
    {
      editor.beginDraw();
      
      editCue.preview(editor, this, time);
      
      editor.endDraw();
      core.image(editor, 0, 0, width, height);
    }
    
    core.endDraw();
    
    if (enableTextureServer)
    {
      if (Platform.isWindows() || Platform.isWindowsCE())
      {
        if (spout != null)
        {
          try
          {
            Log.debug("invoking spout");
            spoutSendTexture.invoke(spout);
          }
          catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
          {

            try
            {
              disableSpoutBroadcast();
            }
            catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e1)
            {
              e1.printStackTrace();
            }
            e.printStackTrace();
          }
        }
      }
      else
        if (Platform.isMac())
        {
          if (syphonServer != null)
          {
            syphonServer.send(core);
          }
        }

    }    
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
      currentScene.go(this);
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
    {
      CAPTURE = recorder.stop();
      if(recorderUi == null)
        recorderUi = new Recorder2017.RecordUI();

      recorderUi.populate("capture", CAPTURE);
    }
    else
      recorder.start();
  }
  
  public void toggleEdit()
  {
    if(editing)
    {
      editing = false;
      editCue = null;
    }
    else
    {
      editing = true;
      editCue = new PathCue();
      editCue.setPathFile("captures/curly-both-mouse");
    }
  }
  
  public void openRecorderUi()
  {
    if(recorderUi == null)
    {
      recorderUi = new Recorder2017.RecordUI();
      recorderUi.populate(null, null);
    }
  }
  public void takeScreenShot(String name)
  {
    screenShotName = name;
    takeScreenShot = true;
  }
  public void runPlayback()
  {
    if(CAPTURE == null || CAPTURE.size() < 1)
    {
      Log.warn("Nothing to play back");
      return;
    }
    int color = PLAYBACK_COLORS[++PLAYBACK_COLOR_INDEX % PLAYBACK_COLORS.length];
    MotionBrush brush = this.currentBrush.clone();
    brush.setSystem(++PLAYBACK_SYSTEM % PLAYBACK_SYSTEM_RESET); 
    Playback2017 pb = Recorder2017.playback("_live", CAPTURE, this, brush);
    pb.setDebugColor(color);
    playbacks.add(pb);
    Log.info("PLAYBACK ADDED");
    Log.info(pb);
  }
  
  /**
   * A hook that allows an action to be "Played" on the screen
   * @param action the action to apply
   * 
   */
  public void robot(RecordAction2017 action, MotionBrush brush, int color)
  {
    if(brush == null){
      Log.warn("Can not playback without a valid brush");
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
    
    event.debugColor = color;
    frameEvents.add(new EventPair(event, brush));
    
  }
  
  @Override
  public void dispose()
  {
    Log.info("Exiting motion...");
    try
    {
      Util.writeProps("motion.ini", Motion.PROPERTIES);  
    }
    catch(Throwable t)
    {
      Log.severe("Error saving motion.ini");
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

  /**
   * Enable a receiver for Motion. If windows
   * <JavaDoc>
   * @param name the Name of this sender, Spout will default to first if not found.
   * @param gl The PGraphics to use for this texture  
   * @throws ClassNotFoundException 
   * @throws SecurityException 
   * @throws NoSuchMethodException 
   * @throws InvocationTargetException 
   * @throws IllegalArgumentException 
   * @throws IllegalAccessException 
   * 
   */
  public void enableSpoutOrSyphonReceiver(String name, PGraphics gl) throws ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException 
  {
    try 
    {
      if (Platform.isMac() && syphonClient == null && !enableTextureClient) {
        //MAC CODE TO READ SYPHON
      } else if (Platform.isWindows() || Platform.isWindowsCE() && !enableTextureClient) {
        pgrTextureClient = createGraphics(width, height); //Use whatever geometry you want here
        
        //WINDOWS CODE TO 
        Class<?> spoutProvider = Class.forName("SpoutProvider");
        Method method = spoutProvider.getMethod("getInstance", PGraphics.class);

        spoutClient = method.invoke(null, gl);  
        
        //Enabled
        enableTextureClient = true;
      }  
    }
    catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException t) 
    {
      throw t;
    }
  }
  
  private void setupSpoutClassAndMethods() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException 
  {
    if (spoutClass == null)
    {
      try
      {
        spoutClass = Class.forName("SpoutImplementation");
        spoutSendTexture = spoutClass.getMethod("sendTexture");// spoutClass.getMethod("sendTexture2", PGraphics3D.class);
        spoutReceiveTexture = spoutClass.getMethod("receiveTexture", PImage.class);// spoutClass.getMethod("sendTexture2", PGraphics3D.class);
      }
      catch (ClassNotFoundException e)
      {
        e.printStackTrace();
      }
    } 
    else 
    {
      System.out.println("Spout is already initialized...");
    }
  }
  public void enableTextureBroadcast(PGraphics gl) throws IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, IllegalAccessException, ClassNotFoundException, InstantiationException
  {
    try
    {
      if (Platform.isMac() && syphonServer == null && !enableTextureServer)
      {
        // We do this to avoid native library initialization
        Class<?> syphonClass = Class.forName("com.danielbchapman.physics.toxiclibs.SyphonGraphicsShare");
        shareInitialize = syphonClass.getMethod("initialize", PApplet.class);
        shareCleanup = syphonClass.getMethod("cleanup");
        shareDraw = syphonClass.getMethod("send", PGraphics.class);

        syphonServer = (IGraphicShare) syphonClass.newInstance();
        shareInitialize.invoke(syphonServer, this);
        enableTextureServer = true;
        return;
      }

      if (Platform.isWindows() || Platform.isWindowsCE())
        System.out.println("Spout ouput here...");
        enableTextureServer = true;
        setupSpoutClassAndMethods();
        
        if (spout != null)
        {
          if (spoutClass == null)
          {
            Method init = spoutClass.getMethod("initSender", String.class, int.class, int.class);
            init.invoke(spout, "Motion", this.displayWidth, this.displayHeight);              
          }
        }
    }
    catch (Throwable t)
    {
      t.printStackTrace();
      enableTextureServer = false;
      return;
    }

    if (spout == null && enableTextureServer)
    {
      try
      {
        Class<?> spoutProvider = Class.forName("SpoutProvider");
        Method method = spoutProvider.getMethod("getInstance", PGraphics.class);

        spout = method.invoke(null, gl);
        
        spoutSendTexture = spoutClass.getMethod("sendTexture");// spoutClass.getMethod("sendTexture2", PGraphics3D.class);
        Method init = spoutClass.getMethod("initSender", String.class, int.class, int.class);
        init.invoke(spout, "Motion", this.displayWidth, this.displayHeight);        
        Log.severe("Initializing Spout " + spout);
      }
      catch (ClassNotFoundException | IllegalAccessException e)
      {
        e.printStackTrace();
        Log.severe(e.toString());
        Log.severe("Unable to initialize Spout\r\n", e);
        enableTextureServer = false;
      }
    }
  }

  public void disableSpoutBroadcast() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
  {
    if (Platform.isMac())
    {
      if (syphonServer != null)
      {
        shareCleanup.invoke(syphonServer);
        shareCleanup = null;
        shareDraw = null;
        shareInitialize = null;
        syphonServer = null;
      }

      enableTextureServer = false;
      return;
    }
    enableTextureServer = false;

    Object spoutRef = spout;
    spout = null; // Clear the reference.
    if (spoutRef != null)
    {
      Method close = spoutClass.getMethod("closeSender");
      close.invoke(spoutRef);
    }
  }
}
