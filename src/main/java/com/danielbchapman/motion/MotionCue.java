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
@EqualsAndHashCode(callSuper = false)
public class MotionCue
{

  /*
   * THIS MIGHT BE USELESS NOW PREPROCESSOR FILES FOR TSVUtil //AUTO GENERATED VALUES FOR #raw private
   * SimpleStringProperty #lower = new SimpleStringProperty(); public SimpleStringProperty get#valueProperty() { return
   * #lower; } public void set#value(String #lower) { this.#lower.set(#lower); } public String get#value() { return
   * this.#lower.get(); } //PROPERTY LIST Id CueId Label Description Time Delay Follow
   */

  // AUTO GENERATED CODE BEGINS -------------------------------
  // AUTO GENERATED VALUES FOR Id
  public enum CueType
  {
    CONTENT("content"), LOGIC("logic"), PLAYBACK("playback"), SHOW_CONTROL("showcontrol"), TRANSFORM("transform");

    public static CueType fromString(String from)
    {
      switch (from)
      {
      case "content":
        return CONTENT;
      case "transform":
        return TRANSFORM;
      case "showcontrol":
        return SHOW_CONTROL;
      case "playback":
        return PLAYBACK;
      default:
        return LOGIC;
      }

    }

    String value;

    CueType(String x)
    {
      this.value = x;
    }

    public String toString()
    {
      return value;
    }
  }

  public static MotionCue deserialize(String data)
  {
    MotionCueStorage store = getGson().fromJson(data, MotionCueStorage.class);
    if (store == null)
      return null;

    return store.toMotionCue();
  }

  public static ArrayList<MotionCue> deserializeList(String data)
  {
    Type type = new TypeToken<List<MotionCueStorage>>()
    {
    }.getType();

    List<MotionCueStorage> cues = getGson().fromJson(data, type);

    ArrayList<MotionCue> ret = new ArrayList<>();
    for (MotionCueStorage store : cues)
      if (store != null)
        ret.add(store.toMotionCue());

    return ret;
  }

  private static Gson getGson()
  {
    return new GsonBuilder().setPrettyPrinting().serializeNulls().create();
  }

  public static String serialize(final List<MotionCue> cues)
  {
    Type type = new TypeToken<List<MotionCueStorage>>()
    {
    }.getType();

    List<MotionCueStorage> storage = new ArrayList<>();
    for (MotionCue q : cues)
      storage.add(new MotionCueStorage(q));
    return getGson().toJson(storage, type);
  }

  public static String serialize(MotionCue cue)
  {
    String data = getGson().toJson(new MotionCueStorage(cue));
    return data;
  }

  // AUTO GENERATED VALUES FOR CueId
  private SimpleStringProperty cueId = new SimpleStringProperty();

  // AUTO GENERATED VALUES FOR Delay
  private SimpleFloatProperty delay = new SimpleFloatProperty();

  // AUTO GENERATED VALUES FOR Description
  private SimpleStringProperty description = new SimpleStringProperty();

  @Getter
  private HashMap<String, String> dynamicData = new HashMap<>();
  // AUTO GENERATED VALUES FOR Follow
  private SimpleFloatProperty follow = new SimpleFloatProperty();

  private SimpleIntegerProperty id = new SimpleIntegerProperty();

  // AUTO GENERATED VALUES FOR Label
  public SimpleStringProperty label = new SimpleStringProperty();
  // AUTO GENERATED VALUES FOR Time
  private SimpleFloatProperty time = new SimpleFloatProperty();

  private SimpleStringProperty type = new SimpleStringProperty();

  public String getCueId()
  {
    return this.cueId.get();
  }

  public SimpleStringProperty getCueIdProperty()
  {
    return cueId;
  }

  public String getData(String key, String def)
  {
    String data = dynamicData.get(key);
    if (data != null)
      return data;

    return def;
  }

  public Float getDelay()
  {
    return this.delay.get();
  }

  public SimpleFloatProperty getDelayProperty()
  {
    return delay;
  }

  public String getDescription()
  {
    return this.description.get();
  }

  public SimpleStringProperty getDescriptionProperty()
  {
    return description;
  }

  public Float getFollow()
  {
    return this.follow.get();
  }

  public SimpleFloatProperty getFollowProperty()
  {
    return follow;
  }

  public Integer getId()
  {
    return this.id.get();
  }

  public SimpleIntegerProperty getIdProperty()
  {
    return id;
  }

  public String getLabel()
  {
    return this.label.get();
  }

  public SimpleStringProperty getLabelProperty()
  {
    return label;
  }

  public Float getTime()
  {
    return this.time.get();
  }

  public SimpleFloatProperty getTimeProperty()
  {
    return time;
  }

  public String getType()
  {
    return this.type.get();
  }

  public SimpleStringProperty getTypeProperty()
  {
    return type;
  }

  public void setCueId(String cueId)
  {
    this.cueId.set(cueId);
  }

  public void setData(String key, String value)
  {
    dynamicData.put(key, value);
  }

  public void setDelay(Float delay)
  {
    this.delay.set(delay);
  }

  public void setDescription(String description)
  {
    this.description.set(description);
  }

  public void setFollow(Float follow)
  {
    this.follow.set(follow);
  }

  public void setId(Integer id)
  {
    this.id.set(id);
  }

  public void setLabel(String label)
  {
    this.label.set(label);
  }

  public void setTime(Float time)
  {
    this.time.set(time);
  }

  public void setType(String type)
  {
    this.type.set(type);
  }
  
  public CueType getCueType()
  {
    return CueType.fromString(getType());  
  }
}
