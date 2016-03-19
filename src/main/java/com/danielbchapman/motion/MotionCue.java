package com.danielbchapman.motion;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import lombok.ToString;

@ToString
public class MotionCue
{
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
  private SimpleStringProperty delay = new SimpleStringProperty();
  public SimpleStringProperty getDelayProperty()
  {
    return delay;
  }

  public void setDelay(String delay)
  {
    this.delay.set(delay);
  }

  public String getDelay()
  {
    return this.delay.get();
  }
  //AUTO GENERATED VALUES FOR Follow    
  private SimpleStringProperty follow = new SimpleStringProperty();
  public SimpleStringProperty getFollowProperty()
  {
    return follow;
  }

  public void setFollow(String follow)
  {
    this.follow.set(follow);
  }

  public String getFollow()
  {
    return this.follow.get();
  }



}
