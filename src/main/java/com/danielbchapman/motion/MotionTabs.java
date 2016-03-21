package com.danielbchapman.motion;

import com.danielbchapman.fx.builders.Fx;
import com.danielbchapman.international.MessageUtility;
import com.danielbchapman.international.MessageUtility.Instance;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class MotionTabs extends TabPane
{
  MotionCue active;
  CueModule parent;
  Instance msg = MessageUtility.getInstance(MotionTabs.class);
  
  Tab flow;
  Tab geometry;
  Tab playback;
  
  VBox flowContent;
  VBox geometryContent;
  VBox playbackContent;
  
  TextField dynamicTest;
  
  public MotionTabs(CueModule parent)
  {
    this.parent = parent;
    setStyle("-fx-background-color: #cfc");
    flow = new Tab(msg.get("tabFlow"));
    geometry = new Tab(msg.get("geometry"));
    playback = new Tab(msg.get("playback"));
    
    setMinHeight(250f);
    flow.setClosable(false);
    geometry.setClosable(false);
    playback.setClosable(false);
    
    
    
    //Fields
    dynamicTest = Fx.promptText("Dynamic Data");
    dynamicTest.onActionProperty().set(
        e -> 
        {
          if(active != null)
          {
            active.setData("dynamic-test", dynamicTest.getText());
            parent.tabs.setFocused(true);
          }
        });
    
    flowContent = new VBox();
    flowContent.getChildren().addAll(
        new Label("Dynamic Test"),
        dynamicTest
        );
    
    flow.setContent(flowContent);
    geometry.setContent(geometryContent);
    playback.setContent(playbackContent);
    
    this.getTabs().addAll(
        flow,
        geometry,
        playback
        );
  }
  
  public void load(MotionCue cue)
  {
    active = cue;
    String data = cue.getData("dynamic-test", null);
    dynamicTest.setText(data);
  }
  
}
