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
  public static int WIDTH = 1024;
  public static int HEIGHT = 768;
  //Forces
  
  public static ActionOLD advanceScene(int delay)
  {
    return new ActionOLD("ADVANCE SCENE", delay, null, e->e.advanceScene());
  }
  public static ActionOLD go()
  {
    return go(0);
  }
  public static ActionOLD go(int delay)
  {
    return new ActionOLD("GO", 0, null, e->e.activeLayerGo());
  }
  
  public static ActionOLD follow(int delay)
  {
    return new ActionOLD("Follow " + delay, delay, null, e->e.activeLayerGo());
  }
  public static ActionOLD dragToVeryLow = new ActionOLD("Drag to 0.01f", 0, null, 
      (x)->{
        x.getPhysics().setDrag(0.01f);
      });
  public static ActionOLD dragToNone = new ActionOLD("Drag to 0.0f", 0, null, 
      (x)->{
        x.getPhysics().setDrag(0.0f);
      });
  public static ActionOLD dragToBasic = new ActionOLD("Drag to 0.4f", 0, null, 
      (x)->{
        x.getPhysics().setDrag(0.4f);
      });
  
  public static AngularGravityBehavior3D gravity = new AngularGravityBehavior3D(new Vec3D(0f, 0.01f, 0f));
  
  public static ActionOLD gravityOff = new ActionOLD("Gravity Off", 0, null, 
      (x)->{
        x.removeBehavior(gravity);
      });
  
  public static ActionOLD gravityOn = new ActionOLD("Gravity On", 0, null, 
      (x)->{
        x.addBehavior(gravity);
      });
  public static HomeBehavior3D home = new HomeBehavior3D(new Vec3D(0, 0, 0));
  public static HomeBehaviorLinear3D homeLinear = new HomeBehaviorLinear3D(0.005f, .1f, 10f);
  
  public static ActionOLD homeOff = new ActionOLD("Home Off", 0, null, 
      (x)->{
        x.removeBehavior(home);
      });
  
  public static ActionOLD homeOn = new ActionOLD("Home On", 0, null, 
      (x)->{
        x.addBehavior(home);
      });
 
  public static ActionOLD homeLinearOff = new ActionOLD("Home Linear Off", 0, null, 
      (x)->{
        x.removeBehavior(homeLinear);
      });
  
  public static ActionOLD homeLinearOn = new ActionOLD("Home Linear On", 0, null, 
      (x)->{
        x.addBehavior(homeLinear);
      });
  
  public static ActionOLD homeTo(float f)
  {
    return new ActionOLD("Home to " + f, 0, null, 
        (x)->{
          home.vars.maxForce = f;
        });
  }
  
  public static ActionOLD gravityTo(float x)
  {
    return new ActionOLD("Gravity Scaling to " + x,
        0,
        null,
        (e)->{Actions.gravity.updateMagnitude(x);}
        );
    
  }
  public static ActionOLD gravityTo(Vec3D dir)
  {
    return new ActionOLD("Gravity to" + dir,
        0,
        null,
        (x)->
        {
          Actions.gravity.setGravity(dir);
        }
        );
  }
  
  public static ActionOLD dragTo(float f)
  {
    return new ActionOLD("Drag to " + f, 0, null, 
        (x)->{
          x.getPhysics().setDrag(f);
        });
  }
  
  public static ActionOLD homeTo0dot2f = new ActionOLD("Home to 0.2f", 0, null, 
      (x)->{
        home.vars.maxForce = 0.2f;
      });
  public static ActionOLD homeTo01f = new ActionOLD("Home to 1f", 0, null, 
      (x)->{
        home.vars.maxForce = 1f;
      });
  
  public static ActionOLD homeTo2f = new ActionOLD("Home to 2f", 0, null, 
      (x)->{
        home.vars.maxForce = 2f;
      });
  
  public static ActionOLD homeTo10f = new ActionOLD("Home to 10f", 0, null, 
      (x)->{
        home.vars.maxForce = 10f;
      });
  
  
  public static ActionOLD lfoOn = new ActionOLD("LFO ON", 0, null, 
	      (x)->{
	    	  engine.startOscillation();
	      });
  
  public static ActionOLD lfoOff = new ActionOLD("Home to 2f", 0, null, 
	      (x)->{
	    	  engine.stopOscillation();
	      });
  
  public static ActionOLD loadBrush(File file){
    return new ActionOLD("Brush: " + file.getName(), 0, null,
      (x)->{
        MotionEngine.brush = MotionInteractiveBehavior.load(
            FileUtil.readFile(file.getAbsolutePath()));
      });
  };
  
  public static ActionOLD stopPlayback()
  {
    return new ActionOLD("Stopping Playbacks", 0, null, (e)->{Actions.engine.clearPlaybacks();});
  }
  
  public static ActionOLD loadEnvironment(File file){
    return new ActionOLD("Environment: " + file.getName(), 0, null,
      (x)->{
        EnvironmentTools.loadVariablesNoUi(file.getAbsolutePath());
      });
  };
  
  public static ArrayList<ActionOLD> loadRecordingAsAction(File recording, File brush)
  {
    MotionInteractiveBehavior instance = MotionInteractiveBehavior.load(brush);
    if(instance == null)
    {
    	System.out.println("[ERROR] unable to load recording:'" + recording + "' brush:'" + brush + "'");
    	return new ArrayList<ActionOLD>();
    }
    return loadRecordingAsAction(recording, instance);
  
  }
  
  public static ArrayList<ActionOLD> loadRecordingAsAction(File recording, MotionInteractiveBehavior brush)
  {
    return loadRecordingAsAction(0, 0, WIDTH, HEIGHT, recording, brush);
  }
  
  public static ArrayList<ActionOLD> loadRecordingAsAction(int x, int y, int w, int h, File recording, MotionInteractiveBehavior brush)
  {
    ArrayList<RecordAction> brushInstance = Recorder.load(recording.getAbsolutePath(), w, h, x, y);
    
    ArrayList<ActionOLD> actions = new ArrayList<>();
  
    Playback p = new Playback("Playback from: " + recording.getName(), brushInstance, brush);
    actions.add(new ActionOLD("Playback", 0, 
        null,
        (e)->
        {
          e.startPlayback(p);
        } 
        ));
   
    return actions;  
  }
  
  public static Cue loadRecording(File recording, File brush){
    ArrayList<ActionOLD> actions = new ArrayList<>();
    MotionInteractiveBehavior instance = MotionInteractiveBehavior.load(brush);
    ArrayList<RecordAction> brushInstance = Recorder.load(recording.getAbsolutePath(), WIDTH, HEIGHT, 0, 0);
    
    Playback p = new Playback("Playback from: " + recording.getName(), brushInstance, instance);
    actions.add(new ActionOLD("Playback", 0, 
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
