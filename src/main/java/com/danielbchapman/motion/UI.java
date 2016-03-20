package com.danielbchapman.motion;

import java.io.File;

import com.danielbchapman.application.Application;
import com.danielbchapman.application.Module;
import com.danielbchapman.international.MessageUtility;
import com.danielbchapman.logging.Log;
import com.danielbchapman.physics.toxiclibs.Main;
import com.danielbchapman.physics.toxiclibs.MotionEngine;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.SeparatorMenuItem;

public class UI extends Application
{
	public static void main(String... args)
  {
    launch(UI.class);
  }
	
	protected MenuBar menuBar;
	
	public MotionEngine motion; 
	
  public MotionTools toolBar;

  public UI()
  {
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
            saveBackup();
          }).asMenuItem(),
	      new SeparatorMenuItem(),
	      createAction("exit", "exitFileDescription",
            (x)->
            {
              showConfirmationYesNo(msg("applicationExit"), msg("applicationExitDetail"),
                  exit -> 
                  {
                    if(exit)
                    {
                      saveBackup();
                      exit();
                    }  
                  });
            }).asMenuItem()
	      );
	  
	  return file;
	}
  
	private Menu getHelp()
	{
	  Menu help = new Menu(msg("help"));
	  
	  help.getItems().addAll(
	      createAction(msg("help"), msg("helpDescription"), 
	          x -> 
  	        {
  	          showInformationDialog("HELP TODO", "HELP TODO");  
  	        }).asMenuItem(),
	      createAction(msg("licenses"), msg("licensesDescription"), 
	          x -> 
	          {
	            showInformationDialog("LICENSES TODO", "LICENESES TODO");
	          }).asMenuItem()
	      );
	  return help;
	}

	@Override
	public MenuBar getMenuBar()
	{
	  
	  if(menuBar != null)
	    return menuBar;
	  Log.info("Constructing Menu Bar");
    menuBar = new MenuBar();
    menuBar.getMenus().addAll(
        getFile(),
        getModules(),
        getHelp()
        );
    
    return menuBar;
	}
	private Menu getModules()
	{
	  Menu motion = new Menu(msg("motion"));
	  motion.getItems().addAll(
	      
	      createAction("cueList", "cueListDetail", 
	          (x)->
	          { 
	            loadModule(CueModule.class);
            }).asMenuItem(),
	      
	      createAction("settings", "settingsDetail", 
	          (x)->
    	      {
    	        loadModule(SettingsModule.class);
    	      }).asMenuItem()
	      );
	  
    return motion;
	}

	@Override
	protected File getPropertiesFile()
	{
		return new File("settings.properties");
	}

	@SuppressWarnings("Implement the CSS if we go this route!")
	@Override
	protected String getStyleSheets()
	{
		return null;
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

	@Override
	public Scene initializeScene()
	{
		//TODO Auto Generated Sub
		throw new RuntimeException("Not Implemented...");
		
	}
  
	@SuppressWarnings("ADD EXCEPTION LOGGING HERE IN THE FRAMEWORK")
	@Override
	protected void logException(Throwable t)
	{
		t.printStackTrace();
	}
	
	public void newFile()
  {
    setProperty("lastSave", null);
  }
	@Override
  protected void postStartup()
  {
    //Load Cues
    loadModule(CueModule.class);
    File lastCues = new File("current/cues.motion");
    if(lastCues.exists())
      getModule(CueModule.class).loadFrom(lastCues, false);
  }
  
	public void register(MotionEngine motion)
  {
  	this.motion = motion;
  }
	
  /**
   * Saves a backup file to the "current" directory. This is the 
   * seamless way the applicaiton works to keep the state.  
   * 
   */
  public void saveBackup()
  {
    File root = new File("current/");
    root.mkdirs();
    
    //Save backup
    getModule(CueModule.class).saveTo(new File("current/cues.motion"));

  }

  public void saveLastFile()
	{
	  //Save the cues
    String lastFile = getProperty("lastSave", null);
    File saveTo = null;
    if(lastFile != null && (saveTo = new File(lastFile)).exists())
    {
      if(saveTo != null)
      {
        getModule(CueModule.class).saveTo(saveTo);
      }
    }
    else
    {
      saveTo = saveFile("motion");
      if(saveTo != null)
      {
        getModule(CueModule.class).saveTo(saveTo);
        setProperty("lastSave", saveTo.getAbsolutePath());
      }
    }
	}
  
  @SuppressWarnings("Shutdown is not implemented at all.")
	@Override
	public void shutdown()
	{
		motion = null;
		saveBackup(); //Save state
	}
}

