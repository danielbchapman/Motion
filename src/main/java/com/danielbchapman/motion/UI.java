package com.danielbchapman.motion;

import java.io.File;

import com.danielbchapman.application.Application;
import com.danielbchapman.application.Module;
import com.danielbchapman.international.MessageUtility;
import com.danielbchapman.logging.Log;
import com.danielbchapman.physics.toxiclibs.Main;
import com.danielbchapman.physics.toxiclibs.MotionEngine;

import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.SeparatorMenuItem;

public class UI extends Application
{
	public MotionEngine motion;
	
	public MotionTools toolBar;
	
	protected MenuBar menuBar; 
	
  public static void main(String... args)
  {
    launch(UI.class);
  }

  public UI()
  {
  }
  
  public void register(MotionEngine motion)
  {
  	this.motion = motion;
  }
  
	@Override
	public MenuBar getMenuBar()
	{
	  
	  if(menuBar != null)
	    return menuBar;
	  Log.info("Constructing Menu Bar");
    menuBar = new MenuBar();
    menuBar.getMenus().addAll(
        getFile()
        );
    
    return menuBar;
	}

	@Override
	protected String getTitle()
	{
	  return MessageUtility.GLOBAL.get("motion-version");
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
		save(); //Save state
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
  
	private Menu getFile()
	{
	  Menu file = new Menu(msg("file"));
	  
	  file.getItems().addAll(
	      createAction("openFile", "openFileDescription",
          (x)->
          {
            getModule(CueModule.class).openCueList();
          }).asMenuItem(),
	      createAction("saveFile", "saveFileDescription",
          (x)->
          {
            getModule(CueModule.class).saveCueList();
            save();
          }).asMenuItem(),
	      new SeparatorMenuItem(),
	      createAction("exit", "exitFileDescription",
            (x)->
            {
              showConfirmationYesNo(msg("saveBeforeExit"), msg("saveBeforeExitdetail"), y-> saveFile());
              exit();
            }).asMenuItem()
	      );
	  
	  return file;
	}
  
  public void save()
  {
    File root = new File("current/");
    root.mkdirs();
    
    //Save the following modules
    getModule(CueModule.class).saveTo(new File("current/cues.motion"));
  }

  @Override
  protected void postStartup()
  {
    //Load Cues
    loadModule(CueModule.class);
    File lastCues = new File("current/cues.motion");
    if(lastCues.exists())
      getModule(CueModule.class).loadFrom(lastCues);
  }
}

