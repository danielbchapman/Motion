package com.danielbchapman.motion;

import com.danielbchapman.fx.builders.FileField;
import com.danielbchapman.fx.builders.FloatField;
import com.danielbchapman.fx.builders.Fx;
import com.danielbchapman.fx.builders.IntegerField;
import com.danielbchapman.international.MessageUtility;
import com.danielbchapman.international.MessageUtility.Instance;
import com.danielbchapman.physics.toxiclibs.Actions;
import com.danielbchapman.text.Safe;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
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
    int posX = cue.asInt("pb-x", 0);
    int posY = cue.asInt("pb-y", 0);
    int posZ = cue.asInt("pb-z", 0);
    
    int sizeX = cue.asInt("pb-width", Actions.WIDTH);
    int sizeY = cue.asInt("pb-height", Actions.HEIGHT);
    
    FileField motionFile = new FileField(cue.getData("pb-recording", null));
    FileField brushFile =  new FileField(cue.getData("pb-brush", null));
    
    IntegerField txtPosX = Fx.promptInt(posX, "X Pos");
    IntegerField txtPosY = Fx.promptInt(posY, "Y Pos");
    IntegerField txtPosZ = Fx.promptInt(posZ, "Z Pos");
    
    IntegerField txtWidth = Fx.promptInt(sizeX, "Width");
    IntegerField txtHeight = Fx.promptInt(sizeY, "Height");
    
    motionFile.setFileChanged(f -> active.setData("pb-recording", motionFile.getFile().getAbsolutePath()));
    brushFile.setFileChanged(f -> active.setData("pb-brush", brushFile.getFile().getAbsolutePath()));
    
    txtPosX.onActionProperty().set( x -> active.setData("pb-x", txtPosX.getText()) );
    txtPosY.onActionProperty().set( x -> active.setData("pb-y", txtPosY.getText()) );
    txtPosZ.onActionProperty().set( x -> active.setData("pb-z", txtPosZ.getText()) );
    
    txtWidth.onActionProperty().set( x -> active.setData("pb-width", txtWidth.getText()) );
    txtHeight.onActionProperty().set( x -> active.setData("pb-height", txtHeight.getText()) );
    
    playbackContent.getChildren().clear();
    playbackContent.getChildren().addAll(
        
        Fx.label("Motion File"),
        motionFile,
        Fx.label("Brush File"),
        brushFile,
        new HBox(
            new VBox(
                Fx.label("Width"),
                txtWidth
                ),
            
            new VBox(
                Fx.label("Height"),
                txtHeight
                )),
        new HBox(
            new VBox(
                Fx.label("Position X"),
                txtPosX
                ),
            new VBox(
                Fx.label("Position Y"),
                txtPosY
                ),
            new VBox(
                Fx.label("Position Z"),
                txtPosZ
                )
            )
        );
  }
}
