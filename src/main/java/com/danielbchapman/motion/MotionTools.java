package com.danielbchapman.motion;

import java.util.ArrayList;

import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;

public class MotionTools extends HBox
{
  ArrayList<Button> buttons;
  
  public MotionTools()
  {
    buttons = new ArrayList<>();
    makeButton(
        new SimpleAction("mediaCue", "mediaCueDetail", 
          (e)->
          {
            System.out.println("Media Cue Fired");
          }
          ));
    
    makeButton(
        new SimpleAction("transformCue", "mediaCueDetail", 
          (e)->
          {
            System.out.println("Transform Cue Fired");
          }
          ));
    
    makeButton(
        new SimpleAction("logicCue", "mediaCueDetail", 
          (e)->
          {
            System.out.println("Logic Cue Fired");
          }
          ));
  }
  
  private void makeButton(SimpleAction action)
  {
    Button button = new Button(action.getLabel());
    button.setTooltip(new Tooltip(action.descriptiveLabel));
    button.setOnAction(
        (e)->
        {
          action.event.accept(e);
        });
    buttons.add(button);
    
    this.getChildren().add(button);
  }
}
