package com.danielbchapman.physics.toxiclibs;

import java.io.File;
import java.util.ArrayList;

import toxi.geom.Vec3D;

import com.danielbchapman.utility.FileUtil;


/**
 * A collection of actions and forces that can be 
 * used by other layers and scenes. These are all "defaults"
 * and can be adjusted as needed. Ideally this allows scenes
 * to share fundamental environmental variables without doubling 
 * up accidentally.
 */
public class Actions
{
  public static MotionEngine engine;
  public static int WIDTH = 1280;
  public static int HEIGHT = 626;
  //Forces
  
  public static Action advanceScene(int delay)
  {
    return new Action("ADVANCE SCENE", delay, null, e->e.advanceScene());
  }
  public static Action go()
  {
    return go(0);
  }
  public static Action go(int delay)
  {
    return new Action("GO", 0, null, e->e.activeLayerGo());
  }
  
  public static Action follow(int delay)
  {
    return new Action("Follow " + delay, delay, null, e->e.activeLayerGo());
  }
  public static Action dragToVeryLow = new Action("Drag to 0.01f", 0, null, 
      (x)->{
        x.getPhysics().setDrag(0.01f);
      });
  public static Action dragToNone = new Action("Drag to 0.0f", 0, null, 
      (x)->{
        x.getPhysics().setDrag(0.0f);
      });
  public static Action dragToBasic = new Action("Drag to 0.4f", 0, null, 
      (x)->{
        x.getPhysics().setDrag(0.4f);
      });
  
  public static AngularGravityBehavior3D gravity = new AngularGravityBehavior3D(new Vec3D(0f, 0.01f, 0f));
  
  public static Action gravityOff = new Action("Gravity Off", 0, null, 
      (x)->{
        x.removeBehavior(gravity);
      });
  
  public static Action gravityOn = new Action("Gravity On", 0, null, 
      (x)->{
        x.addBehavior(gravity);
      });
  public static HomeBehavior3D home = new HomeBehavior3D(new Vec3D(0, 0, 0));
  public static HomeBehaviorLinear3D homeLinear = new HomeBehaviorLinear3D(0.005f, .1f, 10f);
  
  public static Action homeOff = new Action("Home Off", 0, null, 
      (x)->{
        x.removeBehavior(home);
      });
  
  public static Action homeOn = new Action("Home On", 0, null, 
      (x)->{
        x.addBehavior(home);
      });
 
  public static Action homeLinearOff = new Action("Home Linear Off", 0, null, 
      (x)->{
        x.removeBehavior(homeLinear);
      });
  
  public static Action homeLinearOn = new Action("Home Linear On", 0, null, 
      (x)->{
        x.addBehavior(homeLinear);
      });
  
  public static Action homeTo(float f)
  {
    return new Action("Home to " + f, 0, null, 
        (x)->{
          home.vars.maxForce = f;
        });
  }
  
  public static Action gravityTo(float x)
  {
    return new Action("Gravity Scaling to " + x,
        0,
        null,
        (e)->{Actions.gravity.updateMagnitude(x);}
        );
    
  }
  public static Action gravityTo(Vec3D dir)
  {
    return new Action("Gravity to" + dir,
        0,
        null,
        (x)->
        {
          Actions.gravity.setGravity(dir);
        }
        );
  }
  
  public static Action dragTo(float f)
  {
    return new Action("Drag to " + f, 0, null, 
        (x)->{
          x.getPhysics().setDrag(f);
        });
  }
  
  public static Action homeTo0dot2f = new Action("Home to 0.2f", 0, null, 
      (x)->{
        home.vars.maxForce = 0.2f;
      });
  public static Action homeTo01f = new Action("Home to 1f", 0, null, 
      (x)->{
        home.vars.maxForce = 1f;
      });
  
  public static Action homeTo2f = new Action("Home to 2f", 0, null, 
      (x)->{
        home.vars.maxForce = 2f;
      });
  
  public static Action homeTo10f = new Action("Home to 10f", 0, null, 
      (x)->{
        home.vars.maxForce = 10f;
      });
  
  
  public static Action lfoOn = new Action("LFO ON", 0, null, 
	      (x)->{
	    	  engine.startOscillation();
	      });
  
  public static Action lfoOff = new Action("Home to 2f", 0, null, 
	      (x)->{
	    	  engine.stopOscillation();
	      });
  
  public static Action loadBrush(File file){
    return new Action("Brush: " + file.getName(), 0, null,
      (x)->{
        MotionEngine.brush = MotionInteractiveBehavior.load(
            FileUtil.readFile(file.getAbsolutePath()));
      });
  };
  
  public static Action stopPlayback()
  {
    return new Action("Stopping Playbacks", 0, null, (e)->{Actions.engine.clearPlaybacks();});
  }
  
  public static Action loadEnvironment(File file){
    return new Action("Environment: " + file.getName(), 0, null,
      (x)->{
        EnvironmentTools.loadVariablesNoUi(file.getAbsolutePath());
      });
  };
  
  public static ArrayList<Action> loadRecordingAsAction(File recording, File brush)
  {
    MotionInteractiveBehavior instance = MotionInteractiveBehavior.load(brush);
    if(instance == null)
    {
    	System.out.println("[ERROR] unable to load recording:'" + recording + "' brush:'" + brush + "'");
    	return new ArrayList<Action>();
    }
    return loadRecordingAsAction(recording, instance);
  
  }
  
  public static ArrayList<Action> loadRecordingAsAction(File recording, MotionInteractiveBehavior brush)
  {
    return loadRecordingAsAction(0, 0, WIDTH, HEIGHT, recording, brush);
  }
  
  public static ArrayList<Action> loadRecordingAsAction(int x, int y, int w, int h, File recording, MotionInteractiveBehavior brush)
  {
    ArrayList<RecordAction> brushInstance = Recorder.load(recording.getAbsolutePath(), w, h, x, y);
    
    ArrayList<Action> actions = new ArrayList<>();
  
    Playback p = new Playback("Playback from: " + recording.getName(), brushInstance, brush);
    actions.add(new Action("Playback", 0, 
        null,
        (e)->
        {
          e.startPlayback(p);
        } 
        ));
   
    return actions;  
  }
  
  public static Cue loadRecording(File recording, File brush){
    ArrayList<Action> actions = new ArrayList<>();
    MotionInteractiveBehavior instance = MotionInteractiveBehavior.load(brush);
    ArrayList<RecordAction> brushInstance = Recorder.load(recording.getAbsolutePath(), WIDTH, HEIGHT, 0, 0);
    
    Playback p = new Playback("Playback from: " + recording.getName(), brushInstance, instance);
    actions.add(new Action("Playback", 0, 
        null,
        (e)->
        {
          e.startPlayback(p);
        } 
        ));
    Cue ret = new Cue("Cue for " + recording.getName(), actions);
    return ret;
  };
}
