package com.danielbchapman.physics.toxiclibs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Loader
{
  public static Cue load(int x, int y, int w, int h, String label, String file, String brush, Action... post)
  {
    MotionInteractiveBehavior brushInstance = MotionInteractiveBehavior.load(new File(brush));
    ArrayList<Action> acts = Actions.loadRecordingAsAction(x, y, w, h, new File(file), brushInstance);
    if(post != null)
      for(Action a : post)
        acts.add(a);
    return cue(label, acts);
  }
  
  public static Cue cue(String label, List<Action> post, Action ... pre)
  {
    ArrayList<Action> list = new ArrayList<Action>();
    if(pre != null)
      for(Action a : pre)
        list.add(a);
    
    if(post != null)
      for(Action a : post)
        list.add(a);
    
    Cue cue = new Cue(label, list);
    return cue;
  }
}
