package com.danielbchapman.physics.toxiclibs;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;

import lombok.Getter;
import lombok.Setter;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.event.KeyEvent;
import shows.troubledwater.CourtesanLayer;
import shows.troubledwater.RainLayer;
import shows.troubledwater.RestLayer;
import shows.troubledwater.Scene5Grid;
import toxi.geom.Vec3D;
import toxi.physics3d.VerletPhysics3D;
import toxi.physics3d.behaviors.ParticleBehavior3D;

import com.danielbchapman.artwork.Word;
import com.danielbchapman.brushes.EllipseBrush;
import com.danielbchapman.brushes.IBrush;
import com.danielbchapman.brushes.SaveableBrush;
import com.danielbchapman.layers.BleedingCanvasLayer;
import com.danielbchapman.logging.Log;
import com.danielbchapman.physics.kinect.KinectTracker;
import com.danielbchapman.physics.toxiclibs.Recorder.RecordUI;
import com.danielbchapman.physics.ui.SceneController;
import com.illposed.osc.OSCListener;
import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPort;
import com.illposed.osc.OSCPortIn;
import com.sun.jna.Platform;

@SuppressWarnings("unused")
public class MotionEngine extends PApplet
{
  public enum Mode
  {
    SUCK_FORCE, SLAP_FORCE, EXPLODE_FORCE, BRUSH_PALLET
  }

  private static final long serialVersionUID = 1L;

  // Behavior Checks--map these behaviors
  public static boolean showCoordinates = false;
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

  public static Actions ACTIONS;
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
  //A list of active brushes for this drawing cycle
  private ArrayList<SaveableBrush> paintBrushes = new ArrayList<>();
  
  //SHOW CONTROL
  public static int OSC_PORT = 44321;
  public OSCPortIn oscReceiver;
  //Layers
  public Layer activeLayer;
  
  // Mobilology
  public MobilologyOne mobolologyOne;
  public MobilologyTwo mobolologyTwo;
  public MobilologyThree mobolologyThree;
  
  private boolean stopPlayback = false;
  static
  {
    ArrayList<Action> test = new ArrayList<>();

    for (int i = 0; i < 1000; i++)
    {
      Action a = new Action("Action " + i, i * 16);
      test.add(a);
    }

    ArrayList<Action> gravityThirty = new ArrayList<>();
    Action start = new Action("Start Gravity", 0);
    // final AngularGravityBehavior3D gravity = new AngularGravityBehavior3D(new Vec3D(0f, 0.01f, 0f));

    Action stop = new Action("Stop Gravity", 30000);

    start.motionFunction = (MotionEngine e) -> {
      e.addBehavior(Actions.gravity);
    };

    stop.motionFunction = (MotionEngine e) -> {
      e.addBehavior(Actions.gravity);
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
	  try {
		oscReceiver= new OSCPortIn(OSC_PORT);
		 
		OSCListener listener = new OSCListener() {
			public void acceptMessage(java.util.Date time, OSCMessage message) {
				List<Object> args =message.getArguments();
				
				if(args == null || args.size() < 1)
				{
					System.out.println("Args: " + args);
					if(args != null)
						for(int i = 0; i < args.size(); i++)
							System.out.println(i + " -> " + args.get(i));
				}
				else if (args.size() == 3)//Cue
				{
					String command = (String)args.get(0);
					String layer = (String)args.get(1);
					Integer cue = (Integer)args.get(2);
					System.out.println("Args: " + args);
					if(args != null)
						for(int i = 0; i < args.size(); i++)
							System.out.println(i + " -> " + args.get(i));
					
					if(activeLayer != null)
					 activeLayer.go(MotionEngine.this);
				}
				else
				{
					String command = (String) args.get(0);
					if("advance".equalsIgnoreCase(command))
					{
					  if(args.size() < 2)
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
					    catch(Throwable t)
					    {
					      System.out.println("Unable to process scene name: " + args.get(1));
					    }
					  }
						return;
					}
					else if ("play".equalsIgnoreCase(command))
					{
						System.out.println("Play...");
						playCapture();
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
	} catch (SocketException e) {
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
	  physics.setTimeStep(60f/(float)frameRate);
    if(stopPlayback)
    {
        clearPlaybacksPrivate();
    }
    else
      for (Playback p : playbacks){
          p.poll(this);      
    
    }
    if(stopPlayback)
      stopPlayback = false;
    
    //Do the drag events before the updates and painting.
    mouseDraggedFromDraw(mouseX, mouseY);
    try{
      physics.update();
      osc.update();  
    } catch (Throwable t){
      t.printStackTrace();
    }
    

    if (RECORDER.isRecording())
      RECORDER.capture(mouseEvent);

    SaveableBrush painter = null;
    if(brush instanceof SaveableBrush)
    {
      painter = (SaveableBrush) brush;
    }
      
    if (layers != null)
      for (Layer l : layers)
      {
        // Apply forces...
        g.pushMatrix();
        
          /*rotate 90*/
//          translate(0, height);
//          rotateZ(-PConstants.HALF_PI);
        l.render(g);
        
        //Render all robot brushes 
        for(SaveableBrush b : paintBrushes)
          l.renderBrush(b, g, frameCount);
        
        //Render this brush inside the layer matrix
        if(painter != null && painter.isDrawing())
          l.renderBrush(painter, g, frameCount);
        
        l.renderAfterBrushes(g);
        g.popMatrix();
      }

    // Framerate
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
    
    if(highlightMouse)
    {
      pushMatrix();
      ellipseMode(CENTER);
      stroke(4f);
      fill(255,200,200);
      stroke(255,0,0);
      ellipse(mouseX, mouseY, 50, 50);
      fill(0);
      
      if(showCoordinates)
      {
        translate(mouseX, mouseY + 100);
        fill(255);
        text("[" + mouseX + ", " + mouseY + "]", 0, 0, 0);
      }
      popMatrix();
       
    }
    
    if(enableSpout)
    {
    	if(Platform.isWindows() || Platform.isWindowsCE())
    	{
    		if(spout != null)
        	{
        	  System.err.println("Spout called...");
        	  try
	            {
	              spoutSend.invoke(spout);
	            }
	            catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
	            {
	              // TODO Auto-generated catch block
	              e.printStackTrace();
	            }	
        	}
    	}
    	else if (Platform.isMac())
    	{
    		if(syphonOrSpout != null)
    		{
    			syphonOrSpout.send(g);
    		}
    	}
    	
    }
  }

  public void setup()
  {
    Actions.engine = this;
    size(Actions.WIDTH, Actions.HEIGHT, OPENGL);//FIXME Needs a resize listener (though not critical)
    //QLab will limit the rate to 30 FPS it seems
    //Older Intel graphics seem to limit the rate to an odd count. 30 = 20, 60 = 30;
    frameRate(60);
    // physics.addBehavior(world);
    physics.setDrag(0.5f);
    postSetup();
    startOSC();
    try {
		enableSpoutBroadcast(this.g);
	} catch (IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException
			| IllegalAccessException | ClassNotFoundException | InstantiationException e) {
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
    for(int i = 0; i < playbacks.size(); i++)
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
    playbacks.clear(); //STOP PLAYBACKS
    
    clearPhysics();
    
    int index = -1;
    
    for(int i = 0; i < scenes.size(); i++)
    {
      Layer s = scenes.get(i);
      if(s != null)
      {
        String layerName = s.getName();
        if(layerName != null)
        {
          if(layerName.equalsIgnoreCase(name))
          {
            index = i;
            break;
          }
        }
      }
    }
    
    if(index > -1)
    {
      sceneIndex = index;

      System.out.println("Advancing to scene: " + sceneIndex);
      
      Layer tmp = scenes.get(sceneIndex);
      if(tmp == null)
      {
        throw new RuntimeException("Layer was not found, simulation may crash");
      }
      layers.clear();//Remove all
      add(tmp);
    }
    else
      System.out.println("Unable to load scene '" + name + "' was not found.");
  }
  
  public synchronized void advanceScene()
  {
    layers.clear();
    playbacks.clear(); //STOP PLAYBACKS
    
    clearPhysics();
    
    if(sceneIndex == -1 || sceneIndex+1 >= scenes.size())
      sceneIndex = 0;
    else
      sceneIndex++;
    
    System.out.println("Advancing to scene: " + sceneIndex);
    Layer tmp = scenes.get(sceneIndex);
    if(tmp == null)
    {
      throw new RuntimeException("Layer was not found, simulation may crash");
    }
    layers.clear();//Remove all
    add(tmp);
  }
  
  public void activeLayerGo()
  {
    if(activeLayer != null)
      activeLayer.go(this);
  }

  public void postSetup()
  {
//    grid = new GridLayer();
//    add(grid);
//
//    particles = new ParticleLayer();
//    add(particles);
//
//    gridFly = new GridLayerFlying();
//    add(gridFly);
//
//    words = new WordLayer();
//    add(words);
//
//    paragraph = new ParagraphsLayer();
//    add(paragraph);
//
//    one = new SceneOneLayer();
//    add(one);

//    add(new EmitterLayer());
    /*
     * Mobilology Dance Piece
     */
    mobolologyOne = new MobilologyOne();
//    add(mobolologyOne);
//    activeLayer = mobolologyOne;
    
    mobolologyTwo = new MobilologyTwo();
//    add(mobolologyTwo);
//    activeLayer = mobolologyTwo;
    
    mobolologyThree = new MobilologyThree();
//    add(mobolologyThree);
//    activeLayer = mobolologyThree;
    
    RainLayer rainLayer = new RainLayer(this);
    CourtesanLayer courteseanLayer = new CourtesanLayer(this);
    Consumer<Layer> prepare = (layer)->
    {
      layer.applet = this;
      layer.engine = this;
      scenes.add(layer);
//      for (Point p : layer.points)
//        physics.addParticle(p);
    };
    
    prepare.accept(new BleedingCanvasLayer(this)); //Motion Sketches
    prepare.accept(new BleedingCanvasLayer(this)); //Her Painting
    prepare.accept(rainLayer);
    prepare.accept(courteseanLayer);
    prepare.accept(new SpriteLayer(this));
    //prepare.accept(new KinectTracker(this));
    prepare.accept(mobolologyOne);
    prepare.accept(mobolologyTwo);
    prepare.accept(mobolologyThree);
    prepare.accept(new Scene5Grid());
    prepare.accept(new BlackoutLayer());
    prepare.accept(new RestLayer(this));//Blackout layer...
  }
  

  @Override
  public void keyPressed(KeyEvent event)
  {
    if (event.getKey() == 'q' || event.getKey() == 'Q')
    {
      if (tools == null)
      {
        tools = new EnvironmentTools(this);
      }
      tools.pullData();
      tools.setVisible(true);
    }

    if (event.getKey() == 'a' || event.getKey() == 'A')
    {
      if (brushTools == null)
      {
        brushTools = new BrushEditor(this);
        brushTools.populate(brush);
      }
      brushTools.sync();
      brushTools.setVisible(true);
    }

    if(event.getKey() == 'L' || event.getKey() == 'l')
      advanceScene();
    
    if (event.getKey() == ' ')
    {
      if(activeLayer != null)
        activeLayer.go(this);
    }

    if (event.getKey() == 'g')
    {
      // Gravity for one second.
      gravityOneSecond.go(layers.get(0), this);
    }

    if (event.getKey() == 's')
    {
      System.out.println("Splitting words...");
      if(words != null)
    	  words.randomSplits(physics);
    }

    if (event.getKey() == 'S')
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
    if (event.getKey() == '1')
    {
      mode = Mode.BRUSH_PALLET;
    }

    if (event.getKey() == '2')
    {
      mode = Mode.SUCK_FORCE;
      sucker.vars.magnitude = 100f;
      sucker.setJitter(1f);
    }

    if (event.getKey() == '3')
    {
      mode = Mode.SLAP_FORCE;
    }

    if (event.getKey() == '4')
    {
      mode = Mode.EXPLODE_FORCE;
      explode.vars.force = new Vec3D(0, 0, -1f);
    }
    if (event.getKey() == '5')
    {
      mode = Mode.EXPLODE_FORCE;
      explode.vars.force = new Vec3D(0, 0, 1f);
    }

    if (event.getKey() == '7')
    {
      mode = Mode.SUCK_FORCE;
      sucker.vars.magnitude = 100f;
      sucker.setJitter(0f);
    }

    if (event.getKey() == '8')
    {
      mode = Mode.SUCK_FORCE;
      sucker.vars.magnitude = -100f;
      sucker.setJitter(0f);
    }

    // Animation tests
    if (event.getKey() == '0')
    {
      mode = Mode.BRUSH_PALLET;
      brush = new EllipseBrush();
    }

    if (event.getKey() == 'd')
    {
      gridFly.debugXAxis();
    }

    if (event.getKey() == 'z')
    {
      clearPlaybacks();
    }

    // Force Basics
    if (event.getKeyCode() == LEFT)
    {
      float drag = physics.getDrag();
      drag -= 0.01;
      if (drag < 0)
        drag = 0;

      System.out.println("Setting drag to -> " + drag);
      physics.setDrag(drag);
    }

    if (event.getKeyCode() == RIGHT)
    {
      float drag = physics.getDrag();
      drag += 0.01;
      if (drag > 2)
        drag = 2;

      System.out.println("Setting drag to -> " + drag);
      physics.setDrag(drag);
    }

    if (event.getKeyCode() == UP)
    {
      float x = Actions.home.vars.maxForce;
      x += 0.01;
      if (x > 2f)
        x = 2f;

      System.out.println("Setting max force to -> " + x);
      Actions.home.vars.maxForce = x;
    }

    if (event.getKeyCode() == DOWN)
    {
      float x = Actions.home.vars.maxForce;
      x -= 0.01;
      if (x < 0)
        x = 0;

      System.out.println("Setting max force to -> " + x);
      Actions.home.vars.maxForce = x;
    }

    if (event.getKey() == 'h')
    {
      if (isActive(Actions.home))
      {
        System.out.println("Turning off home force!");
        removeBehavior(Actions.home);
      }
      else
      {
        System.out.println("Turning on home force!");
        addBehavior(Actions.home);
      }
    }

    if (event.getKey() == 'j')
    {
      if (isActive(Actions.homeLinear))
      {
        System.out.println("Turning off home linear force!");
        removeBehavior(Actions.homeLinear);
      }
      else
      {
        System.out.println("Turning on linear home force!");
        addBehavior(Actions.homeLinear);
      }
    }
    if (event.getKey() == ']')
    {
      if(activeLayer != null)
        activeLayer.go(this);
    }

    if (event.getKey() == 'o')
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

    if (event.getKey() == 'r' || event.getKey() == 'R')
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

    if (event.getKey() == 't')
    {
    	playCapture();

    }
    
    if(event.getKey() == 'b')
    {
      System.out.println("lose decoration");
      Main.setUndecorated();
    }
    
    if(event.getKey() == 'x')
    {
      showCoordinates = !showCoordinates;
      highlightMouse = showCoordinates;
    }
    
    if(event.getKey() == 'c')
    {
      showControls();
    }
    
    if (event.getKeyCode() == java.awt.event.KeyEvent.VK_F1)
    {
      System.out.println("F1");
      if(enableSpout)
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
//    System.out.println("Recorder Running");
    SaveableBrush paint = null;
    if(behavior instanceof SaveableBrush)
      paint = (SaveableBrush) behavior;
    
    if (action.leftClick)
    {
      behavior.vars.position = new Vec3D(action.x, action.y, 0);
      if(paint != null)
      {
        if(!paint.isDrawing())
          paint.startDraw();  
        
        behavior.setPosition(behavior.vars.position);
        paintBrushes.add(paint);
      }
        
      addBehavior(behavior);
    }
    else
    {
      if(paint != null)
        paint.endDraw();
      removeBehavior(behavior);
    }
  }

  @Override
  public void mousePressed()
  {
    System.out.println("Mouse Down!");
    if (Mode.SUCK_FORCE == mode)
    {
      physics.addBehavior(sucker);
    }
    else
      if (Mode.SLAP_FORCE == mode)
      {
        slap.location = new Vec3D(mouseX, mouseY, 20);// In the plane
        physics.addBehavior(slap);
      }
      else
        if (Mode.EXPLODE_FORCE == mode)
        {
          explode.vars.position = new Vec3D(mouseX, mouseY, 0);
          physics.addBehavior(explode);
        }
        else
          if (Mode.BRUSH_PALLET == mode)
          {
            if(brush instanceof SaveableBrush)
            {
              SaveableBrush b = (SaveableBrush) brush;
              b.startDraw();
            }
            brush.vars.position = new Vec3D(mouseX, mouseY, 0);
            physics.addBehavior(brush);
          }

  }

  public void mouseReleased()
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
            if(brush instanceof SaveableBrush)
            {
              SaveableBrush b = (SaveableBrush) brush;
              b.endDraw();
            }
            physics.removeBehavior(brush);
            
            for(Object o : physics.behaviors)
              System.out.println("\t->" + o);
          }
            
  };

  // FIXME this needs to be HashMaps This probably isn't an issue for less than 10-20 forces
  public boolean isActive(ParticleBehavior3D behavior)
  {
    return activeBehaviors.contains(behavior);
  }

  public boolean addBehavior(ParticleBehavior3D behavior)
  {
    if (activeBehaviors.contains(behavior))
      return false;
    System.out.println("Adding Behavior: " + behavior);
    physics.addBehavior(behavior);
    if (behavior instanceof SaveableParticleBehavior3D)
    {
      System.out.println("Marking behavior as running: " + behavior);
      ((SaveableParticleBehavior3D<?>) behavior).setRunning(true);
      System.out.println(((SaveableParticleBehavior3D<?>) behavior).vars.running);
    }

    activeBehaviors.add(behavior);
    return true;
  }

  public void removeBehavior(ParticleBehavior3D behavior)
  {
    physics.removeBehavior(behavior);
    if (activeBehaviors.remove(behavior))
      System.out.println("Removing Behavior: " + behavior);

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
    if(enable)
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
	if(Platform.isMac() && syphonOrSpout == null && !enableSpout)
	{
		//We do this to avoid native library initialization 
		Class<?> syphonClass = Class.forName("com.danielbchapman.physics.toxiclibs.SyphonGraphicsShare");
		shareInitialize = syphonClass.getMethod("initialize", PApplet.class);
		shareCleanup = syphonClass.getMethod("cleanup");
		shareDraw = syphonClass.getMethod("send", PGraphics.class);
		
		syphonOrSpout = (IGraphicShare) syphonClass.newInstance();
		shareInitialize.invoke(syphonOrSpout, this);
		enableSpout = true;
		return;
	}
    if(Platform.isWindows() || Platform.isWindowsCE())
      enableSpout = true;
    
    if(spout == null && enableSpout)
    {
      try
      {
        Class<?> spoutProvider = Class.forName("SpoutProvider");
        Method method = spoutProvider.getMethod("getInstance", PGraphics.class);
        
        spout = method.invoke(null, g);
        
      }
      catch (ClassNotFoundException | IllegalAccessException e)
      {
        Log.LOG.log(Level.SEVERE, "Unable to initialize Spout\r\n", e);
      }

      if(spout != null)
      {
        if(spoutClass == null)
        {
          try
          {
            spoutClass = Class.forName("SpoutImplementation");
            spoutSend = spoutClass.getMethod("sendTexture");
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
	  if(Platform.isMac())
	  {
		  if(syphonOrSpout != null)
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
    if(spout != null)
    {
      Method close = spoutClass.getMethod("closeSender");
      close.invoke(spout);
    }
  }
  
  public void setBrush(MotionInteractiveBehavior b)
  {
    mode = Mode.BRUSH_PALLET;
    brush = b;
  }
}
	