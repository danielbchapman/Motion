package com.danielbchapman.motion.core;

import java.io.File;
import java.util.ArrayList;

import toxi.geom.Vec3D;

import com.danielbchapman.physics.toxiclibs.EnvironmentTools;
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
  public static Motion engine;
  public static int WIDTH = 1024;
  public static int HEIGHT = 768;
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
    return new Action("GO", 0, s->s.go(Motion.MOTION), null);
  }
  
  public static Action follow(int delay)
  {
    return new Action("Follow " + delay, delay, s->s.go(Motion.MOTION), null);
  }
  
  public static Action dragToVeryLow = new Action("Drag to 0.01f", 0, (s)->{
  	if(s.isVerletPhysic3D()) 
  	{
  		s.get3DPhysics().setTimeStep(0.01f);
  	}
  },null); 
      
  public static Action dragToNone = new Action("Drag to 0.0f", 0, (s)->{
  	if(s.isVerletPhysic3D()) 
  	{
  		s.get3DPhysics().setTimeStep(0.0f);
  	}
  },null);
  
  public static Action dragToBasic = new Action("Drag to 0.4f", 0, (s)->{
  	if(s.isVerletPhysic3D()) 
  	{
  		s.get3DPhysics().setTimeStep(0.4f);
  	}
  },null); 
  
  public static AngularGravityBehavior3D gravity = new AngularGravityBehavior3D(new Vec3D(0f, 0.01f, 0f));
  
  public static Action gravityOff = new Action("Gravity Off", 0, (s)->{
  	if(s.isVerletPhysic3D()) 
  	{
  		if(s.isVerletPhysic3D()) {
  			s.get3DPhysics().removeBehavior(gravity);	
  		}
  	}
  },null);
  
  public static Action gravityOn = new Action("Gravity On", 0, (s)->{
  	if(s.isVerletPhysic3D()) 
  	{
  		if(s.isVerletPhysic3D()) {
  			Log.info("Adding gravity " + gravity);
  			s.get3DPhysics().addBehavior(gravity)	;
  		}
  	}
  },null);
  
  public static HomeBehavior3D home = new HomeBehavior3D(new Vec3D(0, 0, 0));
  public static HomeBehaviorLinear3D homeLinear = new HomeBehaviorLinear3D(0.005f, .1f, 10f);
  
  public static Action homeOff = new Action("Home Off", 0, s->{
  	if(s.isVerletPhysic3D())
  	{
  		s.get3DPhysics().removeBehavior(home);
  	}
  }, null);
  
  public static Action homeOn = new Action("Home On", 0, s->{
  	if(s.isVerletPhysic3D())
  	{
  		s.get3DPhysics().addBehavior(home);
  	}
  }, null);
 
  public static Action homeLinearOff = new Action("Home Linear Off", 0, s->{
  	if(s.isVerletPhysic3D())
  	{
  		s.get3DPhysics().removeBehavior(homeLinear);
  	}
  }, null);
  
  public static Action homeLinearOn = new Action("Home Linear On", 0, s->{
  	if(s.isVerletPhysic3D())
  	{
  		s.get3DPhysics().removeBehavior(homeLinear);
  	}
  }, null);
  
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
    return new Action("Drag to " + f, 0, s->{
    	if(s.isVerletPhysic3D()) {
    		s.get3DPhysics().setDrag(f);
    	}
    		
    },null);
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
  
  public static Action loadBrush(File file){
    return new Action("Brush: " + file.getName(), 0, null,
      (x)->{
      	try
      	{
        	MotionBrush brush = MotionBrush.loadFromFile(
              FileUtil.readFile(file.getAbsolutePath()));
        	x.setCurrentBrush(brush);
      	}
      	catch(RuntimeException e)
      	{
      		Log.severe("Unable to load brush " + file.getName(), e);
      	}
      });
  };
  
  public static Action stopPlayback()
  {
    return new Action("Stopping Playbacks", 0, null, 
    		(e)->{
    			e.stopPlaybacks();
    		});
  }
  
  public static Action loadEnvironment(File file){
    return new Action("Environment: " + file.getName(), 0, null,
      (x)->{
        EnvironmentTools.loadVariablesNoUi(file.getAbsolutePath());
      });
  };
  
  public static ArrayList<Action> loadRecordingAsAction(String recording, String brush)
  {
    	ArrayList<RecordAction2017> actions = Recorder2017.load(recording, Motion.WIDTH, Motion.HEIGHT, 0, 0);
    	MotionBrush loadedBrush = MotionBrush.loadFromFile(brush);  

    	return loadRecordingAsAction(recording, 0, 0, Motion.WIDTH, Motion.HEIGHT, actions, loadedBrush);
  }
  
  public static ArrayList<Action> loadRecordingAsAction(String recording, MotionBrush brush)
  {
  	ArrayList<RecordAction2017> actions = Recorder2017.load(recording, Motion.WIDTH, Motion.HEIGHT, 0, 0);  	
    return loadRecordingAsAction(recording, 0, 0, Motion.WIDTH, Motion.HEIGHT, actions, brush);
  }
  
  /**
   * An overload that takes strings instead of constructed objects
   * @see Actions.loadRecordingAsAction
   * 
   * @param id the label for this
   * @param x offset.x
   * @param y offset.y
   * @param w width
   * @param h height
   * @param recording String file path
   * @param brush String file path
   * @return a list of actions
   */
  public static ArrayList<Action> loadRecordingAsAction(String id, int x, int y, int w, int h, String recording, String brush)
  {
  	ArrayList<RecordAction2017> actions = Recorder2017.load(recording, Motion.WIDTH, Motion.HEIGHT, 0, 0);
  	MotionBrush loadedBrush = MotionBrush.loadFromFile(brush);  
  	
  	return loadRecordingAsAction(id, x, y, w, h, actions, loadedBrush);  
  }
  
  /**
   * The core method for displaying a playback, this takes the record, scales it if needed, 
   * and plays it back using the following brush. This returns a list of Actions that can
   * be called by the interface. In practice this is a single item, but it could be many.
   * @param id The name of this playback for logging and the UI
   * @param x position x, default zero
   * @param y position y, default zero
   * @param w the width of the recording, default to Motion.WIDTH
   * @param h the height of the recording, defauls to Motion.HEIGHT
   * @param recording the list of recording steps
   * @param brush the MotionBrush to paint with
   * @return A list of Actions
   */
  public static ArrayList<Action> loadRecordingAsAction(String id, int x, int y, int w, int h, ArrayList<RecordAction2017> recording, MotionBrush brush)
  {
    
    ArrayList<Action> actions = new ArrayList<>();
  
    actions.add(new Action("Playback", 0, 
        null,
        m->
        {
        	m.runPlayback(id, recording, brush);
        } 
        ));
   
    return actions;  
  }
  
  public static PlaybackCue loadRecording(File recording, File brush){
  	ArrayList<Action> actions = loadRecordingAsAction(recording.getAbsolutePath(), brush.getAbsolutePath());
    
  	PlaybackCue pb = new PlaybackCue();
  	pb.name = recording.getName();
  	pb.brushFile = brush.getAbsolutePath();
  	pb.recordFilePath = recording.getAbsolutePath();
  	return pb;
  };
}
