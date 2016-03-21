package com.danielbchapman.motion;

import java.util.ArrayList;
import java.util.Collection;

import com.danielbchapman.application.IInternationalized;
import com.danielbchapman.application.functional.Procedure;
import com.danielbchapman.fx.builders.Fx;
import com.danielbchapman.international.MessageUtility;
import com.danielbchapman.international.MessageUtility.Instance;
import com.danielbchapman.motion.MotionCue.CueType;

import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.FloatStringConverter;
import javafx.util.converter.IntegerStringConverter;

public class MotionCueList extends TableView<MotionCue> implements IInternationalized
{
  CueModule parent;
  Instance msg = MessageUtility.getInstance(MotionCueList.class);
//  ArrayList<MotionCue> cues;
  private int last_cue = 1;
  
  @SuppressWarnings("unchecked")
  public MotionCueList(CueModule parent)
  { 
    this.parent = parent;
    TableColumn<MotionCue, Integer> id = new TableColumn<>(msg("id"));
    TableColumn<MotionCue, String> label = new TableColumn<>(msg("label"));
    TableColumn<MotionCue, String> description = new TableColumn<>(msg("description"));
    TableColumn<MotionCue, Float> time = new TableColumn<>(msg("time"));

    //Actions
    TableColumn<MotionCue, Procedure<MotionCue>> print = 
        Fx.columnAction(msg("print"), msg("print-button"), 
            (mc)->
            { 
              System.out.println(mc);
            });
    
    TableColumn<MotionCue, Procedure<MotionCue>> remove = 
        Fx.columnAction(msg("remove"), msg("remove-button"), 
            (mc)->
            { 
              removeMotionCue(mc);
            });
    
    id.setCellValueFactory(cell -> cell.getValue().getIdProperty().asObject());  
    id.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
    id.setOnEditCommit(e -> 
    {
      e.getRowValue().setId(e.getNewValue());
    });
    id.setEditable(false);
    
    label.setCellValueFactory(cell -> cell.getValue().getLabelProperty());  
    label.setCellFactory(TextFieldTableCell.forTableColumn());
    label.setOnEditCommit(e -> 
    {
      e.getRowValue().setLabel(e.getNewValue());
    });
    
    
    description.setCellValueFactory(cell -> cell.getValue().getDescriptionProperty());  
    description.setCellFactory(TextFieldTableCell.forTableColumn());
    description.setOnEditCommit(e -> 
    {
      e.getRowValue().setDescription(e.getNewValue());
    });
    
    time.setCellValueFactory(cell -> cell.getValue().getTimeProperty().asObject());  
    time.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));
    time.setOnEditCommit(e -> 
    {
      e.getRowValue().setTime(e.getNewValue());
    });
    
    setEditable(true);
    
    
    getColumns().addAll(
        id,
        label,
        description,
        time,
        print,
        remove
        );
    
    //Add selection listeners
    getSelectionModel().selectedItemProperty().addListener(
      (prop, oldVal, newVal)->
      {
        parent.tabs.load(newVal);
      });
    //No sorting
    setSortPolicy( x -> { return false; });
  }
  
  public void addMotionCue(CueType type)
  {
    last_cue++;
    MotionCue cue = new MotionCue();
    cue.setId(last_cue);
    getItems().add(cue);
  }
  
  public void removeMotionCue(MotionCue cue)
  {
    getItems().remove(cue);
  }
  
  @Override
  public String msg(String key)
  {
    return msg.get(key);
  }

  @Override
  public String msg(String key, Object... params)
  {
    return msg.get(key, params);
  }
  
  public synchronized void loadItems(Collection<MotionCue> cues)
  {
    if(cues == null)
      return;
    ArrayList<MotionCue> newCues = new ArrayList<>(cues.size());
    int max = 0;
    for(MotionCue cue : cues)
    {
      newCues.add(cue);
      if(max < cue.getId())
        max = cue.getId();
    }

    getItems().clear();
    cues.clear();
    
    this.last_cue = max;
    super.setItems(FXCollections.observableArrayList(newCues));
  }
}

