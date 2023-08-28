package com.danielbchapman.physics.toxiclibs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Loader
{
  public static Cue load(int x, int y, int w, int h, String label, String file, String brush, ActionOLD... post)
  {
    MotionInteractiveBehavior brushInstance = MotionInteractiveBehavior.load(new File(brush));
    ArrayList<ActionOLD> acts = ActionsOLD.loadRecordingAsAction(x, y, w, h, new File(file), brushInstance);
    if(post != null)
      for(ActionOLD a : post)
        acts.add(a);
    return cue(label, acts);
  }
  
  public static Cue cue(String label, List<ActionOLD> post, ActionOLD ... pre)
  {
    ArrayList<ActionOLD> list = new ArrayList<ActionOLD>();
    if(pre != null)
      for(ActionOLD a : pre)
        list.add(a);
    
    if(post != null)
      for(ActionOLD a : post)
        list.add(a);
    
    Cue cue = new Cue(label, list);
    return cue;
  }
}
