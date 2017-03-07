package com.danielbchapman.motion.core;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

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
  @SuppressWarnings("rawtypes")
  @Override
  public CueStack load(String data)
  {
    Gson gson = new Gson();
    JsonElement element = gson.fromJson(data, JsonElement.class);
    JsonObject root = element.getAsJsonObject();
    String label = root.get("label").getAsString();
    JsonArray cues = root.get("cues").getAsJsonArray();
    
    CueStack ret = new CueStack();
    ret.label = label;
    
    for(JsonElement el : cues)
    {
      JsonObject obj = el.getAsJsonObject();
      String className = obj.get("typeName").getAsString();
      try
      {
        Class<?> cueClass = Class.forName(className);
        Cue cue = (Cue) gson.fromJson(el, cueClass);
        if(cue != null)
          ret.add(cue);
      }
      catch (ClassNotFoundException e)
      {
        Log.severe("Unable to locate cue class", e);
      }
    }
    
    return ret;
//    in.toJson
//    CueStack stack = in.fromJson(data, getClass());
//    return stack;
  }
}
