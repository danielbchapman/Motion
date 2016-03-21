package com.danielbchapman.motion;

import java.util.ArrayList;

import com.danielbchapman.physics.toxiclibs.Action;
import com.danielbchapman.physics.toxiclibs.Cue;
import com.danielbchapman.physics.toxiclibs.PlaybackCue;

public class MotionCueService
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
  public static Cue convertCue(MotionCue cue)
  {
    String label = cue.getCueId() + " | " + cue.getType() + " | " + cue.getLabel();
    ArrayList<Action> actions = new ArrayList<Action>();

    switch(cue.getCueType())
    {
      case CONTENT:
        break;
      case LOGIC:
        break;
      case PLAYBACK:
        String recording = cue.getData("pb-recording", null);
        String brush = cue.getData("pb-brush", null);
        if(recording != null && brush != null)
        {
          PlaybackCue pb = new PlaybackCue(label, brush, recording);
          return pb;
        }
        break;
      case SHOW_CONTROL:
        break;
      case TRANSFORM:
        break;
    }
    
    Cue instance = new Cue(label, actions);
    return instance;
  }
}
