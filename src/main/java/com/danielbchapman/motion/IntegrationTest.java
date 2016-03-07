package com.danielbchapman.motion;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.HBox;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import com.danielbchapman.application.Application;
import com.danielbchapman.groups.Group;
import com.danielbchapman.groups.Groups;
import com.danielbchapman.groups.Groups.GroupFormatException;

public class IntegrationTest extends Application
{
  public static void main(String ...strings)
  {
    launch(IntegrationTest.class);
  }
  
  MenuBar menuBar;
  @Override
  public MenuBar getMenuBar()
  {
    if(menuBar != null)
      return menuBar;
    
    MenuBar menuBar = new MenuBar();
    menuBar.getMenus()
      .addAll(
        getFileMenu(),
        getHelpMenu()
      );
    
    return menuBar;
  }

  @Override
  protected void initialize()
  {
    //Initialize the "Groups" database
    File collections = new File("collections/");
    int total = 0;
    
    if (collections.exists())
      for (File f : collections.listFiles())
      {
        if(total == 0)
        {
          preloadNotification("groups", "groupsDetail",65);
          total = 80 - collections.listFiles().length;
        }
          
        try
        {
          preloadNotification("groupsLoad", "groupsLoadDetail", ++total);
          Group.read(collections, f.getName().replaceAll(".col", ""));
        }
        catch (FileNotFoundException e)
        {
          System.out.println("Unable to read '" + f + "'");
        }
      }
        
    
    for(String s : Groups.getKnownGroups())
      System.out.println("Initializing Collection " + s + " with " + Groups.getGroup(s).all().size() + " records");
    
    preloadNotification("collectionsComplete", "collectionsCompleteDetail", 100);
  }

  public void saveGroups()
  {
    // Save collections on exit or crash.
    File parent = new File("collections/");
    for (String key : Groups.getKnownGroups())
    {
      Group collection = Groups.getGroup(key);
      collection.save(parent);
    }
  }
  
  @Override
  public void shutdown()
  {
    saveGroups();
  }

  @Override
  protected File getPropertiesFile()
  {
    return new File("settings.properties");
  }

  @Override
  protected void logException(Throwable t)
  {
    t.printStackTrace();
  }

  @Override
  protected String getStyleSheets()
  {
    return null;
  }

  Menu help;
  protected Menu getHelpMenu()
  {
    if(help != null)
      return help;
    
    help = new Menu(msg("help-menu"));
    help.getItems().addAll(
        new SeparatorMenuItem(),
        
        createAction("about", "about-description", (x)->
        {
          this.showInformationDialog(msg("help-title"), msg("help-description"));
        }).asMenuItem()
        );
    
    return help;
  }
  Menu file;
  protected Menu getFileMenu()
  {
    if (file != null)
      return file;

    file = new Menu(msg("file"));
    file.getItems().addAll(
        
        createAction("save", "save-desription", (x)->{
          saveGroups();
          showInformationDialog("groups-saved", "groups-saved-description");
        }).asMenuItem(),
        
        createAction("save-as", "save-as-description", (x)->{
          showConfirmationYesNo("Test Confirmation Block", Application.BS_CONTENT, (b)->
          {
           if(b)
           {
             
           }
           else
           {
           }
          });
        }).asMenuItem(),
                  
        new SeparatorMenuItem(),
        
        createAction("importXML", "importXMLDescription", (x)->{

            File toSave = openFile("*.motion.xml");
            if(toSave == null)
              return;
            
            try
            {
              Groups.fromXml(new FileInputStream(toSave));
            }
            catch (ParserConfigurationException | GroupFormatException | SAXException | IOException e)
            {
              e.printStackTrace();
              throw new RuntimeException(e.getMessage(), e);
            }
          
        }).asMenuItem(),
        
        createAction("exportXML", "exportXMLDescription", (x)->
        {
            File toSave = saveFile("motion.xml");
            if(toSave == null)
              return;
            
            try
            {
              Groups.toXml(new FileOutputStream(toSave), "Motion Database Export", "Basic Persistence Export", Groups.getKnownGroups());
            }
            catch (FileNotFoundException | ParserConfigurationException | TransformerException e)
            {
              e.printStackTrace();
              throw new RuntimeException(e.getMessage(), e);
            }
        }).asMenuItem(),
        
        new SeparatorMenuItem(),
        
        exitApplication.asMenuItem()
        );
    return file;
  }

	@Override
	public Scene initializeScene()
	{
		HBox root = new HBox();
		return new Scene(root);
		
	}
}
