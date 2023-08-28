package com.danielbchapman.motion;

import java.util.ArrayList;

import com.danielbchapman.application.Resource;
import com.danielbchapman.application.ScopeType;
import com.danielbchapman.logging.Log;
import com.danielbchapman.physics.toxiclibs.ActionOLD;
import com.danielbchapman.physics.toxiclibs.Actions;
import com.danielbchapman.physics.toxiclibs.Cue;
import com.danielbchapman.physics.toxiclibs.Loader;

public class MotionCueService extends Resource
{
  /**
   * Converts the MotionCue passed to this method into an actual
   * Cue instance that is useable by Motion. This cue can then be applied against
   * the active Motion layer (whatever that is).
   * 
   * @param cue the cue to convert
   * @return a Cue that can be played back or NULL if nothing can be played
   * 
   */
  @SuppressWarnings({"REFACTOR TO USE: com.danielbchapman.physics.toxiclibs.Actions.loadRecordingAsAction(int, int, int, int, File, MotionInteractiveBehavior)"})
  public Cue convertCue(final MotionCue cue)
  {
    String label = cue.getCueId() + " | " + cue.getType() + " | " + cue.getLabel();
    ArrayList<ActionOLD> actions = new ArrayList<ActionOLD>();

    switch(cue.getCueType())
    {
      case CONTENT:
        break;
      case LOGIC:
        break;
      case PLAYBACK:
        String recording = cue.getData("pb-recording", null);
        String brush = cue.getData("pb-brush", null);
        
        int w, h, x, y; 
        w = cue.asInt("pb-width", Actions.WIDTH);
        h = cue.asInt("height", Actions.HEIGHT);
        x = cue.asInt("pb-x", 0);
        y = cue.asInt("pb-y", 0);
        
        Cue results = Loader.load(x, y, w, h, cue.getLabel(), recording, brush);
        return results;
      case SHOW_CONTROL:
        break;
      case TRANSFORM:
        break;
    }
    
    Cue instance = new Cue(label, actions);
    return instance;
  }
  
  @SuppressWarnings("This fire cue is inefficient and should be precached, it can result in runtime exceptions")
  public void fireCue(MotionCue cue)
  {
    
    if(cue == null)
    {
      Log.info("The cue was null and will not be fired.");
      return;
    }
    
    UI ui = (UI)app();
    if(ui.motion != null)
    {
      Cue loaded = convertCue(cue);
      if(loaded != null)
      {
        loaded.go(ui.motion.activeLayer, ui.motion);
      }
    }
    else
      Log.severe("MOTION engine is not connected...");
  }

  @Override
  public ScopeType getScopeType()
  {
    return ScopeType.APPLICATION;
  }

  @Override
  protected void initialize()
  {
  }

  @Override
  protected void shutdown()
  { 
  }
}
