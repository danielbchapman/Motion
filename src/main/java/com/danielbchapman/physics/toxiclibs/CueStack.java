package com.danielbchapman.physics.toxiclibs;

import java.util.ArrayList;

import com.danielbchapman.groups.Item;

public class CueStack
{

  public ArrayList<Cue> cues = new ArrayList<>();
  
  public int index = -1;
  
  public void go(MotionEngine engine, Layer layer)
  {
    if(index == -1)
    {
      if(cues.size() < 1)
      {
        System.out.println("Can not fire empty cue");
        return;
      }
      index = 0;
    }
    
    if(index < cues.size())
    {
      Cue toFire = cues.get(index);
      index++;
      toFire.go(layer, engine);
    }
  }
  public void add(Cue ... cues)
  {
    for(Cue c : cues)
    {
      System.out.println("Adding cue ->" + c);
      add(c);
    }
      
  }
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
  
  public static CueStack loadFromGroup(Item item)
  {
    return null;
  }
}
