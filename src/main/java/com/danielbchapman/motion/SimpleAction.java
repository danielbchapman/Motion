package com.danielbchapman.motion;

import java.util.function.Consumer;

import com.danielbchapman.international.MessageUtility;
import com.danielbchapman.international.MessageUtility.Instance;

import javafx.event.ActionEvent;
import lombok.Getter;

public class SimpleAction
{
  @Getter
  String label;
  @Getter
  String descriptiveLabel;
  @Getter
  Consumer<ActionEvent> event;
  
  public SimpleAction(String textKey, String descriptionKey, final Consumer<ActionEvent> onClick)
  {
    Instance msg = MessageUtility.getInstance(getClass());
    label = msg.get(textKey);
    descriptiveLabel = msg.get(descriptionKey);
    this.event = onClick;
  }
}
