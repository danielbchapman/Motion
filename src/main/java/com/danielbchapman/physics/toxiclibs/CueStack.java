package com.danielbchapman.physics.toxiclibs;

import java.io.Serializable;
import java.util.ArrayList;

import com.danielbchapman.groups.Item;

public class CueStack implements Serializable
{
  private static final long serialVersionUID = 1L;

  public ArrayList<Cue> cues = new ArrayList<>();
  
  public void add(Cue cue)
  {
    cues.add(cue);
  }
  
  public boolean remove(Cue cue)
  {
    return cues.remove(cue);
  }
  
  public static void save(Item item, CueStack cue)
  {
    
  }
  
  public static CueStack load(Item item)
  {
    return null;
  }
}
