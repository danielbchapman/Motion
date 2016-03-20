package com.danielbchapman.motion;

import java.util.ArrayList;

import com.danielbchapman.motion.MotionCue.CueType;

import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;

public class MotionTools extends HBox
{
  ArrayList<Button> buttons;
  UI app;
  public MotionTools()
  {
    app = (UI) UI.getCurrentInstance();
    buttons = new ArrayList<>();
    makeButton(
        new SimpleAction("mediaCue", "mediaCueDetail", 
          (e)->
          {
            app.getModule(CueModule.class).addCue(CueType.CONTENT);
          }
          ));
    
    makeButton(
        new SimpleAction("transformCue", "transformCueDetail", 
          (e)->
          {
            app.getModule(CueModule.class).addCue(CueType.TRANSFORM);
          }
          ));
    
    makeButton(
        new SimpleAction("logicCue", "mediaCueDetail", 
          (e)->
          {
            app.getModule(CueModule.class).addCue(CueType.LOGIC);
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
