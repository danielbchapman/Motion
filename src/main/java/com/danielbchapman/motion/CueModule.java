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
import lombok.val;

public class CueModule extends Module
{
  private Node parent;
  public Group test = Groups.getGroup("TestCues");
  
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
    
    MotionTools tools = new MotionTools();
    MotionStatus status = new MotionStatus();
    MotionTabs tabs = new MotionTabs();
    cueList = new MotionCueList();
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
    
    val list = FXCollections.observableArrayList(cues);
//    TableView<MotionCue> table = Fx.table(MotionCue.class);
//    table.setItems(list);
    
    cueList.setItems(list);
    
    Button printIt = new Button("Print Data");
    
    printIt.setOnAction(x -> 
    {
      list.forEach(System.out::println);
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
    cueList.addMotionCue(CueType.CONTENT);
  }
  
  public void openCueList()
  {
    File open = app().openFile("motion");
    if(open != null)
    {
      StringBuffer data = Utility.readFile(open.getAbsolutePath());
      try
      {
        String dataS = data.toString();
        Log.info(dataS);
        ArrayList<MotionCue> load = MotionCue.deserializeList(dataS);
        cueList.setItems(FXCollections.observableArrayList(load));
        app().showInformationDialog(msg("cues-loaded"), msg("cues-loaded", open.getName()));
      }
      catch(Throwable t)
      {
        app().showCriticalDialog(msg("error-loading"), t.getMessage());
      }
    }
  }
  
  public void saveCueList()
  {
    File save = app().saveFile("motion");
    if(save != null)
    {
      Utility.writeFile(save.getAbsolutePath(), MotionCue.serialize(cueList.getItems()));
      app().showInformationDialog(msg("cuelist-saved"), msg("culist-saved-detail"));
    }
  }
}
