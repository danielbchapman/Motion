package com.danielbchapman.physics.toxiclibs;

import java.util.ArrayList;
import java.util.Random;
import java.util.function.Consumer;

import lombok.Getter;
import lombok.Setter;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.event.KeyEvent;
import toxi.geom.Vec3D;
import toxi.physics3d.VerletPhysics3D;
import toxi.physics3d.behaviors.ParticleBehavior3D;

import com.danielbchapman.artwork.Word;
import com.danielbchapman.physics.toxiclibs.Recorder.RecordUI;

public class MotionEngine extends PApplet
{
  public enum Mode
  {
    SUCK_FORCE, SLAP_FORCE, EXPLODE_FORCE, BRUSH_PALLET
  }

  private static final long serialVersionUID = 1L;

  // Behavior Checks--map these behaviors
  ArrayList<ParticleBehavior3D> activeBehaviors = new ArrayList<>();
  EnvironmentTools tools;
  BrushEditor brushTools;
  PalletEditor pallets;
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

  public Layer activeLayer;
  
  // Mobilology
  public MobilologyOne mobolologyOne;
  public MobilologyTwo mobolologyTwo;
  public MobilologyThree mobolologyThree;
  
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
    for (Playback p : playbacks)
      p.poll(this);
    physics.update();
    osc.update();

    if (RECORDER.isRecording())
      RECORDER.capture(mouseEvent);

    if (layers != null)
      for (Layer l : layers)
      {
        // Apply forces...
        g.pushMatrix();
        
          /*rotate 90*/
//          translate(0, height);
//          rotateZ(-PConstants.HALF_PI);
        l.render(g);
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
  }

  public void setup()
  {
    Actions.engine = this;
    size(Actions.WIDTH, Actions.HEIGHT, OPENGL);//FIXME Needs a resize listener (though not critical)
    frameRate(60);
    // physics.addBehavior(world);
    physics.setDrag(0.5f);
    postSetup();

    // Add constraints

  }
  
  
  public void advanceScene()
  {
    physics.clear();
    
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
    
    Consumer<Layer> prepare = (layer)->
    {
      layer.applet = this;
      layer.engine = this;
      scenes.add(layer);
//      for (Point p : layer.points)
//        physics.addParticle(p);
    };
    
    prepare.accept(mobolologyOne);
    prepare.accept(mobolologyTwo);
    prepare.accept(mobolologyThree);
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
      words.randomSplits(physics);
    }

    if (event.getKey() == 'S')
    {
      Random rand = new Random();
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
      physics.clear();
      add(gridFly);

      gridFly.offscreen();
      gridFly.lockAll();
    }

    if (event.getKey() == 'd')
    {
      gridFly.debugXAxis();
    }

    if (event.getKey() == 'z')
    {
      gridFly.runFades();
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
      if (!capture.isEmpty())
      {

        Playback p = Recorder.playback(capture, null, this);
        playbacks.clear();
        playbacks.add(p);
        p.start();
      }
    }
  }

  @Override
  public void mouseDragged()
  {
    if (Mode.SUCK_FORCE == mode)
      sucker.setPosition(new Vec3D(mouseX, mouseY, -10f));
    else
      if (Mode.BRUSH_PALLET == mode)
      {
        // Maintain the Z index of the brush

        brush.setPosition(new Vec3D(mouseX, mouseY, brush.vars.position.z));
      }

  }

  /**
   * A hook that allows an action to be "Played" on the screen
   * @param action the action to apply
   * 
   */
  public void robot(RecordAction action, MotionInteractiveBehavior behavior)
  {
    System.out.println("Recorder Running");
    if (action.leftClick)
    {
      behavior.vars.position = new Vec3D(action.x, action.y, 0);
      addBehavior(behavior);
    }
    else
    {
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
            brush.vars.position = new Vec3D(mouseX, mouseY, 0);
            physics.addBehavior(brush);
          }

  }

  public void mouseReleased()
  {
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
            physics.removeBehavior(brush);
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
      ((SaveableParticleBehavior3D) behavior).setRunning(true);
      System.out.println(((SaveableParticleBehavior3D) behavior).vars.running);
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
      ((SaveableParticleBehavior3D) behavior).setRunning(false);
      System.out.println(((SaveableParticleBehavior3D) behavior).vars.running);
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

}
