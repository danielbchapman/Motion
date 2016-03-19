package com.danielbchapman.motion;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class DummyCueItem implements IMotionCue
{
  public DummyCueItem(int id, String label)
  {
    this();
    this.id.set(id);
    this.label.set(label);
  }
  @Getter
  @Setter
  private SimpleIntegerProperty id = new SimpleIntegerProperty();
  @Getter
  @Setter
  private SimpleStringProperty label = new SimpleStringProperty();
  
}
