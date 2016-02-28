package com.danielbchapman.motion.cues;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class CueToolbar extends HBox 
{
	UiCueStackModel model;
	public CueToolbar(UiStack stack)
	{
		Button add = new Button("Add Cue");
		add.setOnMouseClicked((x) -> { model.createCue();});
		
		getChildren().addAll(
				add
				);
	}
	
}
