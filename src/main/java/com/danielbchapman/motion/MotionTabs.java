package com.danielbchapman.motion;

import com.danielbchapman.fx.builders.FloatField;
import com.danielbchapman.fx.builders.Fx;
import com.danielbchapman.international.MessageUtility;
import com.danielbchapman.international.MessageUtility.Instance;
import com.danielbchapman.text.Safe;

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
    
    geometryContent = new VBox();
    geometry.setContent(geometryContent);
    
    playbackContent = new VBox();
    playback.setContent(playbackContent);
    
    this.getTabs().addAll(
        flow,
        geometry,
        playback
        );
  }
  
  @SuppressWarnings("SET FOCUS NEEDS TO BE CALLED HERE...")
  public void load(MotionCue cue)
  {
    active = cue;
    String data = cue.getData("dynamic-test", null);
    dynamicTest.setText(data);
    
    if(MotionCue.CueType.PLAYBACK.toString().equals(cue.getType()))
    {
      flow.setDisable(true);
      geometry.setDisable(true);
      playback.setDisable(false);
      
      loadPlayback(cue);
    }
  }
  
  public void loadPlayback(MotionCue cue)
  {
    float posX = Safe.parseFloat(cue.getData("pb-x", "0"));
    float posY = Safe.parseFloat(cue.getData("pb-y", "0"));
    float posZ = Safe.parseFloat(cue.getData("pb-z", "0"));
//    
//    float scaleX = Safe.parseFloat(cue.getData("pb-scaleX", "1"));
//    float scaleY = Safe.parseFloat(cue.getData("pb-scaleY", "1"));
//    float scaleZ = Safe.parseFloat(cue.getData("pb-scaleZ", "1"));
//    
    TextField file = Fx.promptText("File Name");
    FloatField txtPosX = Fx.promptFloat(posX, "X Pos");
    FloatField txtPosY = Fx.promptFloat(posY, "Y Pos");
    FloatField txtPosZ = Fx.promptFloat(posZ, "Z Pos");
    
    file.onActionProperty().set( x -> active.setData("pb-file", file.getText()) );
    
    txtPosX.onActionProperty().set( x -> active.setData("pb-x", txtPosX.getText()) );
    txtPosY.onActionProperty().set( x -> active.setData("pb-y", txtPosY.getText()) );
    txtPosZ.onActionProperty().set( x -> active.setData("pb-z", txtPosZ.getText()) );
    
    playbackContent.getChildren().clear();
    playbackContent.getChildren().addAll(
        new Label("Recording"),
        file,
        new Label("Position X"),
        txtPosX,
        new Label("Position Y"),
        txtPosY,
        new Label("Position Z"),
        txtPosZ
        );
  }
}
