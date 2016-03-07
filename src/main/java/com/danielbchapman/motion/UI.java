package com.danielbchapman.motion;

import java.io.File;
import java.math.BigDecimal;

import com.danielbchapman.application.Application;
import com.danielbchapman.physics.toxiclibs.Main;
import com.danielbchapman.physics.toxiclibs.MotionEngine;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class UI extends Application
{
	public MotionEngine motion;
	
  public static void main(String... args)
  {
    launch(UI.class);
  }

  public void register(MotionEngine motion)
  {
  	this.motion = motion;
  }
  
  @Override
  public void start(Stage stage) throws Exception
  {
  	Platform.setImplicitExit(false);
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

	@Override
	public MenuBar getMenuBar()
	{
		return new MenuBar();
	}

	@SuppressWarnings("Engine is only partially initialized")
	@Override
	protected void initialize()
	{
		motion = Main.ENGINE; //static pointer
	}

	@SuppressWarnings("Shutdown is not implemented at all.")
	@Override
	public void shutdown()
	{
		motion = null;
		System.out.println("[NOT IMPLEMENTED] shutdown NOT CALLED ");
	}

	@Override
	protected File getPropertiesFile()
	{
		return new File("settings.properties");
	}

	@SuppressWarnings("ADD EXCEPTION LOGGING HERE IN THE FRAMEWORK")
	@Override
	protected void logException(Throwable t)
	{
		t.printStackTrace();
	}

	@SuppressWarnings("Implement the CSS if we go this route!")
	@Override
	protected String getStyleSheets()
	{
		return null;
	}

	@Override
	public Scene initializeScene()
	{
		//TODO Auto Generated Sub
		throw new RuntimeException("Not Implemented...");
		
	}
  
  
}
