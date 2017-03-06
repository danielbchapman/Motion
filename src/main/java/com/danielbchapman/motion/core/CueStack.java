package com.danielbchapman.motion.core;

import java.util.ArrayList;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CueStack implements ISaveable<CueStack>
{
  private String label;
  private ArrayList<Cue> cues = new ArrayList<>();
  
  public CueStack(String label)
  {
    this.label = label;
  }
  
  public void add(Cue q)
  {
    this.cues.add(q);
  }
  
//
//  @Override
//  public String save()
//  {
//    Gson gson = new Gson();
//    String out = gson.toJson(this, CueStack.class);
//    return out;
//  }
//
//  @Override
//  public CueStack load(String data)
//  {
//    Gson in = new Gson();
//    CueStack stack = in.fromJson(data, getClass());
//    return stack;
//  }
}
