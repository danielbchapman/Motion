package com.danielbchapman.motion;

import com.danielbchapman.international.MessageUtility;
import com.danielbchapman.international.MessageUtility.Instance;

import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class MotionTabs extends TabPane
{
  Instance msg = MessageUtility.getInstance(MotionTabs.class);
  
  public MotionTabs()
  {
    setStyle("-fx-background-color: #cfc");
    Tab flow = new Tab(msg.get("tabFlow"));
    Tab geometry = new Tab(msg.get("geometry"));
    Tab playback = new Tab(msg.get("playback"));
    
    setMinHeight(250f);
    flow.setClosable(false);
    geometry.setClosable(false);
    playback.setClosable(false);
    
    flow.setContent(new Label("FLOW"));
    geometry.setContent(new Label("GEOMETRY"));
    playback.setContent(new Label("PLAYBACK"));
    
    this.getTabs().addAll(
        flow,
        geometry,
        playback
        );
  }
}
