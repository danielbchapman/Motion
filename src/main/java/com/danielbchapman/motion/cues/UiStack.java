package com.danielbchapman.motion.cues;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class UiStack extends VBox 
{
	private VBox cues;
	private CueToolbar controls;
	private CueProperties properties;
	
	private UiCueStackModel model;
	public UiStack()
	{
		model = new UiCueStackModel();
		Label title = new Label("Cue List Demo");
		
		
		this.getChildren().addAll(title);
	}
}
