package com.danielbchapman.motion;

import java.math.BigDecimal;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class UI extends Application
{

  public static void main(String... args)
  {
    launch(UI.class);
  }

  @Override
  public void start(Stage stage) throws Exception
  {
    HBox workspace = new HBox();
    VBox content = new VBox();
    
    VBox cueList = new VBox();
    cueList.setMinWidth(300);
    cueList.getChildren().add(new Label("Cue List"));
    
    ScrollPane cueScroll = new ScrollPane(cueList);
    cueScroll.setMinWidth(300);
    cueScroll.setContent(cueList);
    
    for(int i = 1; i < 11; i++)
    {
      ContentCue cue = new ContentCue();
      cue.setLabel("Test Cue " + i);
      cue.setCueNumber(new BigDecimal(i));      
      cueList.getChildren().add(cue);
    }
    
    content
      .getChildren()
      .addAll(
          new Label("Controls"),
          new Label("Editor Area"),
          new Label("Properties")
          );
    
    workspace.getChildren()
      .addAll(
          cueScroll,
          new ScrollPane(content)
          );
    
    stage.setMinWidth(800);
    stage.setMinHeight(600);
    stage.setScene(new Scene(workspace));
    stage.setTitle("Motion 0.0.1 - TEST");
    stage.show();
  }
  
  
}
