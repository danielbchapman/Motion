package com.danielbchapman.motion;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@ToString
@EqualsAndHashCode(callSuper=false)
public class MotionCue
{
  public enum CueType
  {
    LOGIC,
    CONTENT,
    TRANSFORM,
    SHOW_CONTROL,
    PLAYBACK,
  }
  
  private static Gson getGson()
  {
    return new GsonBuilder()
        .setPrettyPrinting()
        .serializeNulls()
        .create();
  }
  public static String serialize(final List<MotionCue> cues)
  {
    Type type = new TypeToken<List<MotionCueStorage>>(){}.getType();
      
    List<MotionCueStorage> storage = new ArrayList<>();
    for(MotionCue q : cues)
      storage.add(new MotionCueStorage(q));
    return getGson().toJson(storage, type);
  }
  
  public static ArrayList<MotionCue> deserializeList(String data)
  {
    Type type = new TypeToken<List<MotionCueStorage>>(){}.getType();
    
    List<MotionCueStorage> cues = getGson().fromJson(data, type);
    
    ArrayList<MotionCue> ret = new ArrayList<>();
    for(MotionCueStorage store : cues)
      if(store != null)
        ret.add(store.toMotionCue());
    
    return ret;
  }
  
  public static String serialize(MotionCue cue)
  {
    String data = getGson().toJson(new MotionCueStorage(cue));
    return data;
  }
  
  public static MotionCue deserialize(String data)
  {
    MotionCueStorage store = getGson().fromJson(data, MotionCueStorage.class);
    if(store == null)
      return null;
    
    return store.toMotionCue();
  }
  
  @Getter
  private HashMap<String, String> dynamicData = new HashMap<>();
  
  public void setData(String key, String value)
  {
    dynamicData.put(key,  value);
  }
  
  public String getData(String key, String def)
  {
    String data = dynamicData.get(key);
    if(data != null)
      return data;
    
    return def;
  }
  /*
PREPROCESSOR FILES FOR TSVUtil
//AUTO GENERATED VALUES FOR #raw
private SimpleStringProperty #lower = new SimpleStringProperty();
public SimpleStringProperty get#valueProperty()
{
  return #lower;
}

public void set#value(String #lower)
{
  this.#lower.set(#lower);
}

public String get#value()
{
  return this.#lower.get();
}
//PROPERTY LIST 
Id  CueId Label Description Time  Delay Follow    
*/

//AUTO GENERATED CODE BEGINS -------------------------------
  //AUTO GENERATED VALUES FOR Id
  private SimpleIntegerProperty id = new SimpleIntegerProperty();
  public SimpleIntegerProperty getIdProperty()
  {
    return id;
  }

  public void setId(Integer id)
  {
    this.id.set(id);
  }

  public Integer getId()
  {
    return this.id.get();
  }
  //AUTO GENERATED VALUES FOR CueId
  private SimpleStringProperty cueId = new SimpleStringProperty();
  
  public SimpleStringProperty getCueIdProperty()
  {
    return cueId;
  }

  public void setCueId(String cueId)
  {
    this.cueId.set(cueId);
  }

  public String getCueId()
  {
    return this.cueId.get();
  }
  
  //AUTO GENERATED VALUES FOR Label
  public SimpleStringProperty label = new SimpleStringProperty();
  public SimpleStringProperty getLabelProperty()
  {
    return label;
  }

  public void setLabel(String label)
  {
    this.label.set(label);
  }

  public String getLabel()
  {
    return this.label.get();
  }
  
  //AUTO GENERATED VALUES FOR Description
  private SimpleStringProperty description = new SimpleStringProperty();
  public SimpleStringProperty getDescriptionProperty()
  {
    return description;
  }

  public void setDescription(String description)
  {
    this.description.set(description);
  }

  public String getDescription()
  {
    return this.description.get();
  }
  //AUTO GENERATED VALUES FOR Time
  private SimpleFloatProperty time = new SimpleFloatProperty();
  public SimpleFloatProperty getTimeProperty()
  {
    return time;
  }

  public void setTime(Float time)
  {
    this.time.set(time);
  }

  public Float getTime()
  {
    return this.time.get();
  }
  //AUTO GENERATED VALUES FOR Delay
  private SimpleFloatProperty delay = new SimpleFloatProperty();
  public SimpleFloatProperty getDelayProperty()
  {
    return delay;
  }

  public void setDelay(Float delay)
  {
    this.delay.set(delay);
  }

  public Float getDelay()
  {
    return this.delay.get();
  }
  //AUTO GENERATED VALUES FOR Follow    
  private SimpleFloatProperty follow = new SimpleFloatProperty();
  public SimpleFloatProperty getFollowProperty()
  {
    return follow;
  }

  public void setFollow(Float follow)
  {
    this.follow.set(follow);
  }

  public Float getFollow()
  {
    return this.follow.get();
  }
  
  
}
