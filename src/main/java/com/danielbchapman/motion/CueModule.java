package com.danielbchapman.motion;

import java.io.File;
import java.util.ArrayList;

import com.danielbchapman.application.Module;
import com.danielbchapman.application.ScopeType;
import com.danielbchapman.groups.Group;
import com.danielbchapman.groups.Groups;
import com.danielbchapman.logging.Log;
import com.danielbchapman.motion.MotionCue.CueType;
import com.danielbchapman.utility.Utility;

import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


/**
 * The Cue Module is the central User Interface for editing
 * cues and firing cues. It uses the underlying data structure to 
 * monitor the list of cues.
 *
 */
@SuppressWarnings("CUES SHOULD USE A RESOURCE, NOT AN OBSERVED LIST FOR THE SOURCE")
public class CueModule extends Module
{
  private Node parent;
  public Group test = Groups.getGroup("TestCues");
  
  public MotionTools tools;
  public MotionStatus status;
  public MotionTabs tabs;
  public MotionCueList cueList;
  
  @Override
  public ScopeType getScopeType()
  {
    return ScopeType.APPLICATION;
  }

  @Override
  protected Node getNode()
  {
    if(parent != null)
      return parent;
    
    //Main container
    VBox workspace = new VBox();
    
    tools = new MotionTools();
    status = new MotionStatus();
    tabs = new MotionTabs(this);
    cueList = new MotionCueList(this);
    HBox controls = new HBox();
    
    controls.getChildren().add(tabs);
    
    ArrayList<MotionCue> cues = new ArrayList<>();   
    
    for(int i = 1; i < 11; i++)
    {
      final int ii = i;
      cues.add(
          new MotionCue()
          {{
              setId(ii);
              setLabel(""+ ii);
              setDescription("Test Cue " + ii);  
          }});

    }
//    TableView<MotionCue> table = Fx.table(MotionCue.class);
//    table.setItems(list);
    
    cueList.loadItems(cues);
    
    Button printIt = new Button("Print Data");
    
    printIt.setOnAction(x -> 
    {
      cueList.getItems().forEach(System.out::println);
    });
    
    workspace.getChildren()
      .addAll(
          tools,
          cueList,
          //itb,
//          table,
          controls,
          printIt,
          status
          );
    
    parent = workspace;
    return parent;
  }

  @Override
  protected void initialize()
  { 
  }

  @Override
  protected void postInitialize()
  { 
  }

  @Override
  protected void shutdown()
  { 
  }

  public void addCue(CueType type)
  {
    cueList.addMotionCue(type);
  }
  
  public void openCueList()
  {
    File open = app().openFile("motion");
    if(open != null)
    {
      loadFrom(open, true);
    }
  }
  public void saveTo(File save)
  {
    final ArrayList<MotionCue> copy = new ArrayList<MotionCue>();
    for(MotionCue q : cueList.getItems())
      copy.add(q);
      
    String data = MotionCue.serialize(copy);
    System.out.println(data);
    Utility.writeFile(save.getAbsolutePath(), data);
    app().showInformationDialog(msg("cuelist-saved"), msg("culist-saved-detail"));
  }
  
  public void loadFrom(File file, boolean showDialog)
  {
    StringBuffer data = Utility.readFile(file.getAbsolutePath());
    try
    {
      String dataS = data.toString();
      Log.info(dataS);
      ArrayList<MotionCue> load = MotionCue.deserializeList(dataS);
      cueList.loadItems(FXCollections.observableArrayList(load));
      
      if(showDialog)
        app().showInformationDialog(msg("cues-loaded"), msg("cues-loaded", file.getName()));
    }
    catch(Throwable t)
    {
      app().showCriticalDialog(msg("error-loading"), t.getMessage());
      t.printStackTrace();
    }
  }
  
  public void saveCueList()
  {
    File save = app().saveFile("motion");
    if(save != null)
    {
      saveTo(save);
    }
  }
}
