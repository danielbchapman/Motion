package com.danielbchapman.motion.cues;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TestUiStack extends Application 
{

	public static void main(String ... args)
	{
		launch(TestUiStack.class);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception 
	{
		UiStack ui = new UiStack();
		Scene scene = new Scene(ui);
		primaryStage.setScene(scene);
		System.out.println("start called...");
		primaryStage.setTitle("Test Cue Stack Window");
		primaryStage.setMinHeight(400);
		primaryStage.setMinWidth(300);
		primaryStage.show();
	}

}
