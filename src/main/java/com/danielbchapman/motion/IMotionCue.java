package com.danielbchapman.motion;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public interface IMotionCue
{
  public SimpleIntegerProperty getId();
  public void setId(SimpleIntegerProperty id);
  
  public SimpleStringProperty getLabel();
  public void setLabel(SimpleStringProperty label);
}
