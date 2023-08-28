package com.danielbchapman.physics.toxiclibs;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.stream.Collectors;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import javafx.application.Application;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;

import com.danielbchapman.artwork.Word;
import com.danielbchapman.brushes.ImageBrush;
import com.danielbchapman.brushes.ImageBrushRound;
import com.danielbchapman.brushes.SaveableBrush;
import com.danielbchapman.brushes.SmallBrush;
import com.danielbchapman.brushes.TinyBrush;
import com.danielbchapman.layers.BleedingCanvasLayer;
import com.danielbchapman.layers.ClearLayer;
import com.danielbchapman.logging.Log;
import com.danielbchapman.motion.UI;
import com.danielbchapman.motion.livedraw.IRemoteDrawCommand;
import com.danielbchapman.motion.livedraw.RemoteDrawKeyEvent;
import com.danielbchapman.motion.livedraw.RemoteDrawMouseEvent;
import com.danielbchapman.physics.toxiclibs.Recorder.RecordUI;
import com.danielbchapman.physics.ui.SceneController;
import com.illposed.osc.OSCListener;
import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortIn;
import com.illposed.osc.OSCPortOut;
import com.sun.jna.Platform;
import com.sun.org.apache.bcel.internal.generic.CASTORE;

import lombok.Getter;
import lombok.Setter;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.event.KeyEvent;
import processing.opengl.PGraphics3D;
import shows.gravitationalwaves.BleedingGrid;
import shows.gravitationalwaves.BleedingGridOffset;
import shows.gravitationalwaves.BleedingPointGrid;
import shows.gravitationalwaves.GalaxyLayer;
import shows.gravitationalwaves.RandomParticleLinesLayer;
import shows.gravitationalwaves.RandomParticlesLayer;
import shows.gravitationalwaves.RandomParticlesVertical;
import shows.gravitationalwaves.TriangleWavesLayer;
import shows.shekillsmonsters.BeholderPuppet;
import shows.shekillsmonsters.HealingSpellLayer;
import shows.shekillsmonsters.MagicMissleLayer;
import shows.shekillsmonsters.SpinningSquares;
import shows.shekillsmonsters.TilliusWWE;
import shows.shekillsmonsters.TitleSheKillsMonsters;
import shows.troubledwater.Bridge;
import shows.troubledwater.CourtesanLayer;
import shows.troubledwater.FinalLayer;
import shows.troubledwater.HeroLayer;
import shows.troubledwater.OneLeafEnd;
import shows.troubledwater.Prologue;
import shows.troubledwater.RainLayer;
import shows.troubledwater.RecordingLayer;
import shows.troubledwater.RestLayer;
import shows.troubledwater.Scene5Grid;
import shows.troubledwater.Scene7;
import toxi.geom.Vec3D;
import toxi.physics3d.VerletPhysics3D;
import toxi.physics3d.behaviors.ParticleBehavior3D;

@SuppressWarnings("unused")
public class MotionEngine extends PApplet
{
  private static final long serialVersionUID = 1L;
  // Behavior Checks--map these behaviors
  public static boolean showCoordinates = false;
  public static final String VERSION = "0.0.1-ALPHA";

  public enum Mode
  {
    SUCK_FORCE, SLAP_FORCE, EXPLODE_FORCE, BRUSH_PALLET
  }

  // Java FX Interface
  @Getter
  @Setter
  private UI fxInterface;

  ArrayList<ParticleBehavior3D> activeBehaviors = new ArrayList<>();
  EnvironmentTools tools;
  BrushEditor brushTools;
  PalletEditor pallets;
  @Getter
  @Setter
  boolean enableSpout = false;
  boolean highlightMouse = false;
  private Object spout;
  private IGraphicShare syphonOrSpout;
  private Method shareInitialize;
  private Method shareCleanup;
  private Method shareDraw;

  private Class<?> spoutClass;
  private Method spoutSend;
  private final static Recorder RECORDER = new Recorder();
  private RecordUI recordUi;
  @Getter
  @Setter
  private ArrayList<RecordAction> capture = new ArrayList<>();

  public static ActionsOLD ACTIONS;
  private VerletPhysics3D physics = new VerletPhysics3D();
  private int sceneIndex = -1;
  private ArrayList<Layer> scenes = new ArrayList<>();
  private ArrayList<Layer> layers = new ArrayList<>();
  private ArrayList<Playback> playbacks = new ArrayList<>();

  private Mode mode = Mode.SUCK_FORCE;
  private static Cue testCue;
  private static Cue gravityOneSecond;

  private GridLayer grid;
  private GridLayerFlying gridFly;

  private ParticleLayer particles;
  private SceneOneLayer one;
  private ParagraphsLayer paragraph;
  WordLayer words;

  // Brushes
  public static MotionInteractiveBehavior brush = new ExplodeBehavior(new Vec3D(0, 0, 1f), 100f);
  private static FalloffAttractionBehavior sucker = new FalloffAttractionBehavior(new Vec3D(1f, 1f, 1f), 5f, 100f, 1f);
  private static Slap slap = new Slap(new Vec3D(), new Vec3D(0, 0, -1f), 1000f);
  private static ExplodeBehavior explode = new ExplodeBehavior(new Vec3D(0, 0, 1f), 100f);
  private static FrequencyOscillationBehavior osc = new FrequencyOscillationBehavior();
  // A list of active brushes for this drawing cycle
  private ArrayList<SaveableBrush> paintBrushes = new ArrayList<>();

  //Remote Drawing
  public int virtualMouseX = 0;
  public int virtualMouseY = 0;
  public boolean virtualMouseDown = false;
  private boolean remoteDrawEnabled = false;
  private ArrayList<IRemoteDrawCommand> remoteDrawQueue = new ArrayList<IRemoteDrawCommand>();

  //Live Drawing (sending to Remote)
  public boolean liveDrawEnabled = false;
  public String liveDrawUrl;
  public Integer liveDrawPort;
  public OSCPortOut oscSender;
  
  // SHOW CONTROL
  public static int OSC_PORT = 44321;
  public OSCPortIn oscReceiver;
  
  // Layers
  public Layer activeLayer;

  // Mobilology
  public MobilologyOne mobolologyOne;
  public MobilologyTwo mobolologyTwo;
  public MobilologyThree mobolologyThree;

  // Screen Shot
  private boolean takeScreenshot;
  
  private boolean stopPlayback = false;
  
  static
  {
    ArrayList<ActionOLD> test = new ArrayList<>();

    for (int i = 0; i < 1000; i++)
    {
      ActionOLD a = new ActionOLD("Action " + i, i * 16);
      test.add(a);
    }

    ArrayList<ActionOLD> gravityThirty = new ArrayList<>();
    ActionOLD start = new ActionOLD("Start Gravity", 0);
    // final AngularGravityBehavior3D gravity = new AngularGravityBehavior3D(new Vec3D(0f, 0.01f, 0f));

    ActionOLD stop = new ActionOLD("Stop Gravity", 30000);

    start.motionFunction = (MotionEngine e) -> {
      e.addBehavior(ActionsOLD.gravity);
    };

    stop.motionFunction = (MotionEngine e) -> {
      e.addBehavior(ActionsOLD.gravity);
    };

    gravityThirty.add(start);
    gravityThirty.add(stop);

    gravityOneSecond = new Cue("Gravity Thirty", gravityThirty);
    testCue = new Cue("Test Cue", test);
  }

  /**
   * Start the OSC listeners for this instance
   */
  public void startOSC()
  {
    try
    {
      oscReceiver = new OSCPortIn(OSC_PORT);
      OSCListener listener = new OSCListener()
      {
        public void acceptMessage(java.util.Date time, OSCMessage message)
        {
          List<Object> args = message.getArguments();
          System.out.println("MESSAGE RECEIVED | size: [" + args.size() + "] message:[" + message.getArguments().stream().map(x -> x.toString()).collect(Collectors.joining(" | ")) + "]");
          if (args == null || args.size() < 1)
          {
            System.out.println("Args: " + args);
            if (args != null)
              for (int i = 0; i < args.size(); i++)
                System.out.println(i + " -> " + args.get(i));
          }
          else
          {
            String command = (String) args.get(0);
            if ("go".equalsIgnoreCase(command))
            {
              System.out.println("FIRING GO");
              try
              {
                Integer cue = (Integer) args.get(1);
                if (activeLayer != null)
                  activeLayer.go(MotionEngine.this);
              }
              catch (Throwable t)
              {
                t.printStackTrace();
                System.out.println("Unable to fire cue!");
              }
            }
            else
              if ("advance".equalsIgnoreCase(command))
              {
                if (args.size() < 2)
                {
                  advanceScene();
                }
                else
                {
                  try
                  {
                    String name = (String) args.get(1);
                    advanceSceneTo(name);
                  }
                  catch (Throwable t)
                  {
                    System.out.println("Unable to process scene name: " + args.get(1));
                  }
                }
                return;
              }
              else
                if ("play".equalsIgnoreCase(command))
                {
                  System.out.println("Play...");
                  playCapture();
                }
                else
                  if ("clear".equalsIgnoreCase(command))
                  {
                    advanceSceneTo("clear");
                    activeLayerGo(); // force clear call
                  }
                  else
                    if ("live".equalsIgnoreCase(command))
                    {
                      if (args.size() < 2)
                      {
                        System.out.println("Invalid command: " + args.stream().map(x -> x.toString()).collect(Collectors.joining(", ")));
                        return;
                      }

                      try
                      {
                        String type = (String) args.get(1);
                        if (type.equalsIgnoreCase("mouse") && args.size() > 4)
                        {
                          float x = (float) args.get(2);
                          float y = (float) args.get(3);
                          int down = (int) args.get(4);

                          RemoteDrawMouseEvent e = new RemoteDrawMouseEvent(x, y, down != 0);
//                          System.out.println("Adding event: " + e);
                          remoteDrawQueue.add(e);
                        }
                        else if(type.equalsIgnoreCase("key") && args.size() > 3)
                        {
                          char key = (char) args.get(2);
                          int code = (int) args.get(3);
                          
                          virtualKeyPressed(key, code);
                        }
                      }
                      catch (Throwable t)
                      {
                        System.err.println("INVALID COMMAND");
                        t.printStackTrace(System.err);
                      }
                    }
                    else
                    {
                      System.out.println("UNKNOWN COMMAND " + command);
                      return;
                    }
          }
        }
      };
      oscReceiver.addListener("/motion", listener);
      oscReceiver.startListening();
      System.out.printf("STARTING OSC ON PORT %d%n", OSC_PORT);
      System.out.println(oscReceiver.toString());
    }
    catch (SocketException e)
    {
      e.printStackTrace();
    }
  }

  // public Force gravity;
  // public Force wind;
  // public Force
  //
  public void add(Layer layer)
  {
    layer.applet = this;
    layer.engine = this;
    layers.add(layer);
    if (layer.points != null)
      for (Point p : layer.points)
        physics.addParticle(p);

    activeLayer = layer;
  }

  public void remove(Layer layer)
  {
    layers.remove(layer);
  }

  @SuppressWarnings("deprecation")
  public void draw()
  {

    // Model updates
    // physics.setTimeStep(frameRate / 60f);
    physics.setTimeStep(60f / (float) frameRate);
    if (stopPlayback)
    {
      clearPlaybacksPrivate();
    }
    else
      for (Playback p : playbacks)
      {
        p.poll(this);

      }
    if (stopPlayback)
      stopPlayback = false;

    // Do the drag events before the updates and painting.
    if (!remoteDrawEnabled)
    {
      virtualMouseX = mouseX;
      virtualMouseY = mouseY;
      mouseDraggedFromDraw(virtualMouseX, virtualMouseY);
    }
    else 
    {
      if(remoteDrawQueue.size() > 0)
      {
        ArrayList<IRemoteDrawCommand> blank = new ArrayList<IRemoteDrawCommand>();
        ArrayList<IRemoteDrawCommand> queue = remoteDrawQueue;
        remoteDrawQueue = blank;
        
        if(queue.size() > 0)
        {
          for(IRemoteDrawCommand command : queue)
          {
            boolean down = false;
            if(command instanceof RemoteDrawMouseEvent)
            {
              System.out.println("Remote Mouse Event: " + command.toString());
              RemoteDrawMouseEvent e = (RemoteDrawMouseEvent)command;
              int[] values = Transform.translate(e.x, e.y, ActionsOLD.WIDTH, ActionsOLD.HEIGHT);
              virtualMouseX = values[0];
              virtualMouseY = values[1];
              down = e.down;
            }
            else if (command instanceof RemoteDrawKeyEvent)
            {
              RemoteDrawKeyEvent e = (RemoteDrawKeyEvent)command;
            }
            
            mouseDraggedFromDraw(virtualMouseX, virtualMouseY);
            
            if(down != virtualMouseDown)
            {
              virtualMouseDown = down;
              if(down)
                virtualMousePressed();
              else
                virtualMouseReleased();
            } 
          }  
        }  
      }
    }

    try
    {
      physics.update();
      osc.update();
    }
    catch (Throwable t)
    {
      t.printStackTrace();
    }

    if (RECORDER.isRecording())
      RECORDER.capture(mouseEvent);
    
    if(liveDrawEnabled)
    {
      float[] transform = Transform.translate(virtualMouseX, virtualMouseY, ActionsOLD.WIDTH, ActionsOLD.HEIGHT);
      ArrayList<Object> args = new ArrayList<>();
      args.add("live");
      args.add("mouse");
      args.add(transform[0]); 
      args.add(transform[1]);
      args.add(virtualMouseDown ? 1 : 0);
      OSCMessage out = new OSCMessage("/motion", args);
      try
      {
        oscSender.send(out);
      }
      catch (IOException e)
      {
        e.printStackTrace();
        liveDrawEnabled = false;
        oscSender = null;
      }
    }

    SaveableBrush painter = null;
    if (brush instanceof SaveableBrush)
    {
      painter = (SaveableBrush) brush;
    }

    if (layers != null)
      for (Layer l : layers)
      {
        // Apply forces...
        g.pushMatrix();

        /* rotate 90 */
        // translate(0, height);
        // rotateZ(-PConstants.HALF_PI);
        l.render(g);

        // Render all robot brushes
        for (SaveableBrush b : paintBrushes)
          l.renderBrush(b, g, frameCount);

        // Render this brush inside the layer matrix
        if (painter != null && painter.isDrawing())
          l.renderBrush(painter, g, frameCount);

        l.renderAfterBrushes(g);
        g.popMatrix();
      }

    // Frame-rate
    pushMatrix();
    translate(0, 0, 50);
    noStroke();
    fill(0);
    rect(50, height - 50, 150, 25, 0);
    fill(255, 0, 0);
    text("Frame Rate: " + frameRate, 50, height - 50 + 15, 1);
    popMatrix();

    if (layers != null)
      for (Layer l : layers)
        l.update();

    if (highlightMouse)
    {
      pushMatrix();
      ellipseMode(CENTER);
      stroke(4f);
      fill(255, 200, 200);
      stroke(255, 0, 0);
      ellipse(virtualMouseX, virtualMouseY, 50, 50);
      fill(0);

      if (showCoordinates)
      {
        translate(virtualMouseX, virtualMouseY + 100);
        fill(255);
        text("[" + virtualMouseX + ", " + virtualMouseY + "]", 0, 0, 0);
      }
      popMatrix();

    }

    if (enableSpout)
    {
      if (Platform.isWindows() || Platform.isWindowsCE())
      {
        if (spout != null)
        {
          try
          {
            spoutSend.invoke(spout);
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
          if (syphonOrSpout != null)
          {
            syphonOrSpout.send(g);
          }
        }

    }
    if(takeScreenshot)
    {
      takeScreenshot = false;
      PImage ss = g.get();
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
      ss.save("screenshots/motion-" + sdf.format(new Date()) + ".tiff");
    }
  }

  public void settings()
  {
    size(ActionsOLD.WIDTH, ActionsOLD.HEIGHT, P3D);// FIXME Needs a resize listener (though not critical)
  }
  public void setup()
  {
   
    ActionsOLD.engine = this;
    // QLab will limit the rate to 30 FPS it seems
    // Older Intel graphics seem to limit the rate to an odd count. 30 = 20, 60 = 30;
    frameRate(60);
    // physics.addBehavior(world);
    physics.setDrag(0.5f);
    postSetup();
    startOSC();
    try
    {
      enableSpoutBroadcast(this.g);
    }
    catch (IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | IllegalAccessException | ClassNotFoundException | InstantiationException e)
    {
      e.printStackTrace();
    }

    // Add constraints

  }

  public synchronized void clearPlaybacks()
  {
    stopPlayback = true;
  }

  private synchronized void clearPlaybacksPrivate()
  {
    for (int i = 0; i < playbacks.size(); i++)
    {
      Playback p = playbacks.get(i);
      removeBehavior(p.getBrush());
    }
    playbacks.clear();
  }

  public synchronized void clearPhysics()
  {
    physics.clear();
    activeBehaviors.clear();
  }

  public synchronized void advanceSceneTo(String name)
  {
    layers.clear();
    playbacks.clear(); // STOP PLAYBACKS

    clearPhysics();

    int index = -1;

    for (int i = 0; i < scenes.size(); i++)
    {
      Layer s = scenes.get(i);
      if (s != null)
      {
        String layerName = s.getName();
        if (layerName != null)
        {
          if (layerName.equalsIgnoreCase(name))
          {
            index = i;
            break;
          }
        }
      }
    }

    if (index > -1)
    {
      sceneIndex = index;

      System.out.println("Advancing to scene: " + sceneIndex);

      Layer tmp = scenes.get(sceneIndex);
      if (tmp == null)
      {
        throw new RuntimeException("Layer was not found, simulation may crash");
      }
      layers.clear();// Remove all
      add(tmp);
    }
    else
      System.out.println("Unable to load scene '" + name + "' was not found.");
  }

  public synchronized void advanceScene()
  {
    layers.clear();
    playbacks.clear(); // STOP PLAYBACKS

    clearPhysics();

    if (sceneIndex == -1 || sceneIndex + 1 >= scenes.size())
      sceneIndex = 0;
    else
      sceneIndex++;

    Layer tmp = scenes.get(sceneIndex);
    if (tmp == null)
    {
      throw new RuntimeException("Layer was not found, simulation may crash");
    }
    System.out.println("Advancing to scene [" + sceneIndex + "] " + tmp.getName());
    layers.clear();// Remove all
    add(tmp);
  }

  public void activeLayerGo()
  {
    if (activeLayer != null)
      activeLayer.go(this);
  }

  public void postSetup()
  {
    // grid = new GridLayer();
    // add(grid);
    //
    // particles = new ParticleLayer();
    // add(particles);
    //
    // gridFly = new GridLayerFlying();
    // add(gridFly);
    //
    // words = new WordLayer();
    // add(words);
    //
    // paragraph = new ParagraphsLayer();
    // add(paragraph);
    //
    // one = new SceneOneLayer();
    // add(one);

    // add(new EmitterLayer());
    /*
     * Mobilology Dance Piece
     */
    // mobolologyOne = new MobilologyOne();
    // add(mobolologyOne);
    // activeLayer = mobolologyOne;

    // mobolologyTwo = new MobilologyTwo();
    // add(mobolologyTwo);
    // activeLayer = mobolologyTwo;

    // mobolologyThree = new MobilologyThree();
    // add(mobolologyThree);
    // activeLayer = mobolologyThree;

    RainLayer rainLayer = new RainLayer();
    CourtesanLayer courteseanLayer = new CourtesanLayer();
    Consumer<Layer> prepare = (layer) -> {
      layer.applet = this;
      layer.engine = this;
      scenes.add(layer);
      // for (Point p : layer.points)
      // physics.addParticle(p);
    };

    // Gravitational Waves Project
    prepare.accept(new TriangleWavesLayer());
    // prepare.accept(new GalaxyLayer(this));
    prepare.accept(new RandomParticlesLayer());
    prepare.accept(new RandomParticlesVertical());
    prepare.accept(new RandomParticleLinesLayer());
    prepare.accept(new BleedingGrid(ActionsOLD.WIDTH, ActionsOLD.HEIGHT, 40));
    prepare.accept(new BleedingGridOffset(ActionsOLD.WIDTH, ActionsOLD.HEIGHT, 40));
    prepare.accept(new BleedingPointGrid(ActionsOLD.WIDTH, ActionsOLD.HEIGHT, 10, "point-grid-10"));
    prepare.accept(new BleedingPointGrid(ActionsOLD.WIDTH, ActionsOLD.HEIGHT, 40, "point-grid-40"));
    prepare.accept(new BleedingPointGrid(ActionsOLD.WIDTH, ActionsOLD.HEIGHT, 80, "point-grid-80"));
    // Demo Leftovers
    // prepare.accept(new HeroLayer(this));
    prepare.accept(new RecordingLayer(this)); // Motion Sketches
    prepare.accept(new TitleSheKillsMonsters());
    prepare.accept(new MagicMissleLayer(this));
    prepare.accept(new HealingSpellLayer(this));
    prepare.accept(new TilliusWWE());
    prepare.accept(new SpinningSquares(ActionsOLD.WIDTH, ActionsOLD.HEIGHT));
    prepare.accept(new BeholderPuppet());
    prepare.accept(rainLayer);
    // prepare.accept(new Prologue(this));
    // prepare.accept(new BleedingCanvasLayer(this)); //Her Painting
    // prepare.accept(new Bridge(this));
    // prepare.accept(new Scene7());
    // prepare.accept(new HeroLayer(this));
    // prepare.accept(rainLayer);
    // prepare.accept(courteseanLayer);
    // prepare.accept(new SpriteLayer(this));
    // prepare.accept(new KinectTracker(this));
    // prepare.accept(mobolologyOne);
    // prepare.accept(mobolologyTwo);
    // prepare.accept(mobolologyThree);
    // prepare.accept(new FinalLayer());
    // prepare.accept(new Scene5Grid());
    // prepare.accept(new OneLeafEnd(this));
    prepare.accept(new ClearLayer()); // A layer that draws black
    prepare.accept(new RestLayer());// Blackout layer...
  }
  
  public void virtualKeyPressed(char key, int code)
  {
    System.out.println("Key Pressed [" + key +"] code:[" + code +"]");
    Predicate<Character> isChar = c -> {
      if (key == c || key == Character.toUpperCase(c))
      {
        return true;
      }

      return false;
    };

    if (isChar.test('w'))
    {
      initializeFXInterface();
    }

    if (key == 'q' || key == 'Q')
    {
      if (tools == null)
      {
        tools = new EnvironmentTools(this);
      }
      tools.pullData();
      tools.setVisible(true);
    }

    if (key == 'a' || key == 'A')
    {
      if (brushTools == null)
      {
        brushTools = new BrushEditor(this);
        brushTools.populate(brush);
      }
      brushTools.sync();
      brushTools.setVisible(true);
    }

    if (key == 'L' || key == 'l')
      advanceScene();

    if (key == ' ')
    {
      if (activeLayer != null)
        activeLayer.go(this);
    }

    if (key == 'g')
    {
      // Gravity for one second.
      gravityOneSecond.go(layers.get(0), this);
    }

    if (key == 's')
    {
      System.out.println("Splitting words...");
      if (words != null)
        words.randomSplits(physics);
    }

    if (key == 'S')
    {
      System.out.println("Splitting paragraph...");
      // paragraph.split(physics, rand.nextFloat() % 3f);
      paragraph.split(physics, 0f);
      if (paragraph.isSplit())
      {
        System.out.println("Splitting words");

        for (Word w : paragraph.paragraph.words)
        {
          // w.split(physics, rand.nextFloat() % 3f);
          w.split(physics, 0f);
        }
      }
    }
    if (key == '1')
    {
      mode = Mode.BRUSH_PALLET;
    }

    if (key == '2')
    {
      mode = Mode.SUCK_FORCE;
      sucker.vars.magnitude = 100f;
      sucker.setJitter(1f);
    }

    if (key == '3')
    {
      mode = Mode.SLAP_FORCE;
    }

    if (key == '4')
    {
      mode = Mode.EXPLODE_FORCE;
      explode.vars.force = new Vec3D(0, 0, -1f);
    }
    if (key == '5')
    {
      mode = Mode.EXPLODE_FORCE;
      explode.vars.force = new Vec3D(0, 0, 1f);
    }

    if (key == '7')
    {
      mode = Mode.BRUSH_PALLET;
      brush = new TinyBrush();
    }

    if (key == '8')
    {
      mode = Mode.BRUSH_PALLET;
      brush = new SmallBrush();
    }

    // Animation tests
    if (key == '9')
    {
      mode = Mode.BRUSH_PALLET;
      brush = new ImageBrushRound();
    }

    // Animation tests
    if (key == '0')
    {
      mode = Mode.BRUSH_PALLET;
      brush = new ImageBrush();
    }

    if (key == 'z')
    {
      clearPlaybacks();
    }

    // Force Basics
    if (key == LEFT)
    {
      float drag = physics.getDrag();
      drag -= 0.01;
      if (drag < 0)
        drag = 0;

      System.out.println("Setting drag to -> " + drag);
      physics.setDrag(drag);
    }

    if (key == RIGHT)
    {
      float drag = physics.getDrag();
      drag += 0.01;
      if (drag > 2)
        drag = 2;

      System.out.println("Setting drag to -> " + drag);
      physics.setDrag(drag);
    }

    if (key == UP)
    {
      float x = ActionsOLD.home.vars.maxForce;
      x += 0.01;
      if (x > 2f)
        x = 2f;

      System.out.println("Setting max force to -> " + x);
      ActionsOLD.home.vars.maxForce = x;
    }

    if (key == DOWN)
    {
      float x = ActionsOLD.home.vars.maxForce;
      x -= 0.01;
      if (x < 0)
        x = 0;

      System.out.println("Setting max force to -> " + x);
      ActionsOLD.home.vars.maxForce = x;
    }

    if (key == 'h')
    {
      if (isActive(ActionsOLD.home))
      {
        System.out.println("Turning off home force!");
        removeBehavior(ActionsOLD.home);
      }
      else
      {
        System.out.println("Turning on home force!");
        addBehavior(ActionsOLD.home);
      }
    }

    if (key == 'j')
    {
      if (isActive(ActionsOLD.homeLinear))
      {
        System.out.println("Turning off home linear force!");
        removeBehavior(ActionsOLD.homeLinear);
      }
      else
      {
        System.out.println("Turning on linear home force!");
        addBehavior(ActionsOLD.homeLinear);
      }
    }
    if (key == ']')
    {
      if (activeLayer != null)
        activeLayer.go(this);
    }

    if (key == 'o')
    {
      if (!osc.enabled)
      {
        System.out.println("Turning on 20hz Wave");
        physics.addBehavior(osc);
        osc.setEnabled(true);
      }
      else
      {
        System.out.println("Turning off 20hz Wave");
        physics.removeBehavior(osc);
        osc.setEnabled(false);
      }
    }

    if (key == 'r' || key == 'R')
    {
      if (RECORDER.isRecording())
      {
        System.out.println("Stopping recording");
        capture = RECORDER.stop();
        if (recordUi == null)
        {
          recordUi = new RecordUI(width, height);
          recordUi.populate("Unknown", getCapture());
          recordUi.setVisible(true);
        }
        else
        {
          recordUi.populate(recordUi.name, getCapture());
          recordUi.setVisible(true);
        }

        System.out.println("List Recorded");
      }
      else
      {
        if (recordUi != null)
        {
          recordUi.setVisible(false);
          recordUi.close();
        }
        recordUi = null;
        System.out.println("Recording Starting...");
        RECORDER.start();
      }

    }

    if (key == 't')
    {
      playCapture();

    }

    if (key == 'b')
    {
      System.out.println("lose decoration");
      Main.setUndecorated();
    }

    if (key == 'x')
    {
      showCoordinates = !showCoordinates;
      highlightMouse = showCoordinates;
    }

    if (key == 'c')
    {
      showControls();
    }
    
    if (code == java.awt.event.KeyEvent.VK_F1)
    {
      System.out.println("F1");
      if (enableSpout)
        try
        {
          disableSpoutBroadcast();
        }
        catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e1)
        {
          enableSpout = false;
          Log.exception(e1);
        }
      else
        try
        {
          try
          {
            enableSpoutBroadcast(g);
          }
          catch (NoSuchMethodException | SecurityException | IllegalAccessException | ClassNotFoundException | InstantiationException e)
          {
            Log.exception(e);
          }
        }
        catch (IllegalArgumentException | InvocationTargetException e)
        {
          Log.exception(e);
        }
    }
    
    if (code == java.awt.event.KeyEvent.VK_F2)
    {
      if( remoteDrawEnabled )
        disableRemoteDraw();
      else
        enableRemoteDraw();
    }
    
    if (code == java.awt.event.KeyEvent.VK_F3)
    {
      if(!liveDrawEnabled)
        dialogLiveDraw();
      else
        disableLiveDraw();
    }
    
    if (code == java.awt.event.KeyEvent.VK_F4)
      takeScreenshot();
    
    if (key == 'm') {
      takeScreenshot();
    }
  }
  
  @Override
  public void keyPressed(KeyEvent event)
  {
    if(liveDrawEnabled && oscSender != null)
    {
      int[] skipped = new int[]
          {
            java.awt.event.KeyEvent.VK_F1,
            java.awt.event.KeyEvent.VK_F2,
            java.awt.event.KeyEvent.VK_F3,
            java.awt.event.KeyEvent.VK_F4,
          };
      
      boolean skipIt = false;
      for(int code : skipped)
      {
        if(event.getKeyCode() == code)
        {
          skipIt = true;
          break;
        }
      }
      if(!skipIt)
      {
        ArrayList<Object> args = new ArrayList<>();
        args.add("live");
        args.add("key");
        args.add(event.getKey());
        args.add(event.getKeyCode());
        OSCMessage out = new OSCMessage("/motion", args);
        try
        {
          oscSender.send(out);
        }
        catch (IOException e)
        {
          disableLiveDraw();
          e.printStackTrace();
        }
      }
    }
    
    virtualKeyPressed(event.getKey(), event.getKeyCode());
    
    

  }

  /**
   * Play back the last captured recording.
   */
  public void playCapture()
  {
    if (!capture.isEmpty())
    {
      Playback p = Recorder.playback(capture, null, this);
      playbacks.clear();
      playbacks.add(p);
      p.start();
    }
  }

  public void mouseDraggedFromDraw(int x, int y)
  {
    if (Mode.SUCK_FORCE == mode)
      sucker.setPosition(new Vec3D(x, y, -10f));
    else
      if (Mode.BRUSH_PALLET == mode)
      {
        // Maintain the Z index of the brush
        brush.setPosition(new Vec3D(x, y, brush.vars.position.z));
      }
  }

  /**
   * A hook that allows an action to be "Played" on the screen
   * @param action the action to apply
   * 
   */
  public void robot(RecordAction action, MotionInteractiveBehavior behavior)
  {
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

  public void virtualMousePressed()
  {
    System.out.println("Mouse Down!");
    if (Mode.SUCK_FORCE == mode)
    {
      physics.addBehavior(sucker);
    }
    else
      if (Mode.SLAP_FORCE == mode)
      {
        slap.location = new Vec3D(virtualMouseX, virtualMouseY, 20);// In the plane
        physics.addBehavior(slap);
      }
      else
        if (Mode.EXPLODE_FORCE == mode)
        {
          explode.vars.position = new Vec3D(virtualMouseX, virtualMouseY, 0);
          physics.addBehavior(explode);
        }
        else
          if (Mode.BRUSH_PALLET == mode)
          {
            if (brush instanceof SaveableBrush)
            {
              SaveableBrush b = (SaveableBrush) brush;
              b.startDraw();
            }
            brush.vars.position = new Vec3D(virtualMouseX, virtualMouseY, 0);
            physics.addBehavior(brush);
          }

  }

  public void virtualMouseReleased()
  {
    System.out.println("Mouse released!");
    if (Mode.SUCK_FORCE == mode)
      physics.removeBehavior(sucker);
    else
      if (Mode.SLAP_FORCE == mode)
        physics.removeBehavior(slap);
      else
        if (Mode.EXPLODE_FORCE == mode)
          physics.removeBehavior(explode);
        else
          if (Mode.BRUSH_PALLET == mode)
          {
            if (brush instanceof SaveableBrush)
            {
              SaveableBrush b = (SaveableBrush) brush;
              b.endDraw();
            }
            physics.removeBehavior(brush);

            for (Object o : physics.behaviors)
              System.out.println("\t->" + o);
          }
  }

  @Override
  public void mousePressed()
  {
    if (!remoteDrawEnabled)
    {
      // override virtual mouse
      virtualMouseDown = true;
      virtualMouseX = mouseX;
      virtualMouseY = mouseY;
      virtualMousePressed();
    }
  }

  public void mouseReleased()
  {
    if (!remoteDrawEnabled)
    {
      virtualMouseDown = false;
      virtualMouseReleased();
    }
  }

  public synchronized void initializeFXInterface()
  {
    if (fxInterface == null)
    {
      Thread fxThread = new Thread(() -> {
        final JFrame frame = new JFrame();
        new JFXPanel(); // start toolkit
          javafx.application.Platform.runLater(() -> {
            System.out.println("Starting FX");
            fxInterface = new UI();
            fxInterface.startSwing(frame);
          });

        });
      fxThread.start();
    }
    else
    {
      fxInterface.reset();
    }

  }

  // FIXME this needs to be HashMaps This probably isn't an issue for less than 10-20 forces
  public boolean isActive(ParticleBehavior3D behavior)
  {
    return activeBehaviors.contains(behavior);
  }

  public boolean addBehavior(ParticleBehavior3D behavior)
  {
    if (activeBehaviors.contains(behavior))
      return false;
    // System.out.println("Adding Behavior: " + behavior);
    physics.addBehavior(behavior);
    if (behavior instanceof SaveableParticleBehavior3D)
    {
      // System.out.println("Marking behavior as running: " + behavior);
      ((SaveableParticleBehavior3D<?>) behavior).setRunning(true);
      // System.out.println(((SaveableParticleBehavior3D<?>) behavior).vars.running);
    }

    activeBehaviors.add(behavior);
    return true;
  }

  public void removeBehavior(ParticleBehavior3D behavior)
  {
    physics.removeBehavior(behavior);
    // if (activeBehaviors.remove(behavior))
    // System.out.println("Removing Behavior: " + behavior);

    if (behavior instanceof SaveableParticleBehavior3D)
    {
      System.out.println("Marking behavior as not running: " + behavior);
      ((SaveableParticleBehavior3D<?>) behavior).setRunning(false);
      System.out.println(((SaveableParticleBehavior3D<?>) behavior).vars.running);
    }
  }

  public VerletPhysics3D getPhysics()
  {
    return physics;
  }

  public void startPlayback(Playback p)
  {
    p.start();
    playbacks.add(p);
  }

  public void osc(boolean enable)
  {
    if (enable)
    {
      System.out.println("Turning on 20hz Wave");
      physics.addBehavior(osc);
    }
    else
    {
      physics.removeBehavior(osc);
      osc.setEnabled(enable);
    }
  }

  public void showControls()
  {
    SceneController controller = new SceneController(this);
    controller.setVisible(true);
  }

  public void enableSpoutBroadcast(PGraphics gl) throws IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, IllegalAccessException, ClassNotFoundException, InstantiationException
  {
    try
    {
      if (Platform.isMac() && syphonOrSpout == null && !enableSpout)
      {
        // We do this to avoid native library initialization
        Class<?> syphonClass = Class.forName("com.danielbchapman.physics.toxiclibs.SyphonGraphicsShare");
        shareInitialize = syphonClass.getMethod("initialize", PApplet.class);
        shareCleanup = syphonClass.getMethod("cleanup");
        shareDraw = syphonClass.getMethod("send", PGraphics.class);

        syphonOrSpout = (IGraphicShare) syphonClass.newInstance();
        shareInitialize.invoke(syphonOrSpout, this);
        enableSpout = true;
        return;
      }

      if (Platform.isWindows() || Platform.isWindowsCE())
        enableSpout = true;
    }
    catch (Throwable t)
    {
      t.printStackTrace();
      enableSpout = false;
      return;
    }

    if (spout == null && enableSpout)
    {
      try
      {
        Class<?> spoutProvider = Class.forName("SpoutProvider");
        Method method = spoutProvider.getMethod("getInstance", PGraphics.class);

        spout = method.invoke(null, gl);

      }
      catch (ClassNotFoundException | IllegalAccessException e)
      {
        Log.LOG.log(Level.SEVERE, "Unable to initialize Spout\r\n", e);
        enableSpout = false;
      }

      if (spout != null)
      {
        if (spoutClass == null)
        {
          try
          {
            spoutClass = Class.forName("SpoutImplementation");
            spoutSend = spoutClass.getMethod("sendTexture");// spoutClass.getMethod("sendTexture2", PGraphics3D.class);
            Method init = spoutClass.getMethod("initSender", String.class, int.class, int.class);
            init.invoke(spout, "Motion", this.displayWidth, this.displayHeight);
          }
          catch (ClassNotFoundException e)
          {
            e.printStackTrace();
          }
        }

      }
    }
  }

  public void disableSpoutBroadcast() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
  {
    if (Platform.isMac())
    {
      if (syphonOrSpout != null)
      {
        shareCleanup.invoke(syphonOrSpout);
        shareCleanup = null;
        shareDraw = null;
        shareInitialize = null;
        syphonOrSpout = null;
      }

      enableSpout = false;
      return;
    }
    enableSpout = false;

    Object spoutRef = spout;
    spout = null; // Clear the reference.
    if (spoutRef != null)
    {
      Method close = spoutClass.getMethod("closeSender");
      close.invoke(spoutRef);
    }
  }

  public void setBrush(MotionInteractiveBehavior b)
  {
    mode = Mode.BRUSH_PALLET;
    brush = b;
  }

  public void stopOscillation()
  {
    osc.setEnabled(false);
    physics.removeBehavior(osc);
    System.out.println("Turning off 20hz Wave");
  }

  public void startOscillation()
  {
    osc.setEnabled(true);
    physics.addBehavior(osc);
    System.out.println("Turning on 20hz Wave");
  }

  public void enableRemoteDraw()
  {
    System.out.println("Enabling Remote Draw");
    remoteDrawEnabled = true;
    remoteDrawQueue.clear();
  }

  public void disableRemoteDraw()
  {
    System.out.println("Disabling Remote Draw");
    remoteDrawEnabled = false;
  }
  
  
  public void dialogLiveDraw()
  {
    OscDialog oscDialog = new OscDialog(this);
    oscDialog.setVisible(true);
  }
  
  public void enableLiveDraw()
  {
    if(liveDrawPort != null && liveDrawUrl != null)
    {
      try
      {
        oscSender = new OSCPortOut(InetAddress.getByName(liveDrawUrl), liveDrawPort);
        liveDrawEnabled = true;
      }
      catch (SocketException | UnknownHostException e)
      {
        e.printStackTrace();
      }  
    }
  }
  
  public void disableLiveDraw()
  {
    liveDrawEnabled = false;
    oscSender = null;
  }
  
  /**
   * Instructs motion to capture the next frame as a .tiff  
   */
  public void takeScreenshot()
  {
    takeScreenshot = true; 
  }
}
