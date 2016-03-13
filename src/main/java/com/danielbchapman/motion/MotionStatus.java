package com.danielbchapman.motion;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class MotionStatus extends HBox
{
  Label text = new Label("Status:");
  
  public MotionStatus()
  {
    setMinHeight(25f);
    getChildren().add(text);
    setStyle("-fx-background-color: #aa9");
  }
  
  public void setStatus(String status)
  {
    if(status != null)
      text.setText(status);
  }
}
