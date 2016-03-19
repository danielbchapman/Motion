package com.danielbchapman.motion;

import java.lang.reflect.Field;
import java.util.ArrayList;

import javax.swing.table.TableCellEditor;

import com.danielbchapman.application.IInternationalized;
import com.danielbchapman.fx.builders.Fx;
import com.danielbchapman.international.MessageUtility;
import com.danielbchapman.international.MessageUtility.Instance;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import javafx.util.converter.FloatStringConverter;
import javafx.util.converter.IntegerStringConverter;

public class MotionCueList extends TableView<MotionCue> implements IInternationalized
{
  Instance msg = MessageUtility.getInstance(MotionCueList.class);

  public static int LAST_CUE = 1;
  @SuppressWarnings("unchecked")
  public MotionCueList()
  { 
    TableColumn<MotionCue, Integer> id = new TableColumn<>(msg("id"));
    TableColumn<MotionCue, String> label = new TableColumn<>(msg("label"));
    TableColumn<MotionCue, String> description = new TableColumn<>(msg("description"));
    TableColumn<MotionCue, Float> time = new TableColumn<>(msg("time"));
    
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
        time
        );
    //No sorting
    setSortPolicy( x -> { return false; });
  }
  
  public void addMotionCue(IMotionCue cue)
  {
    LAST_CUE++;
    cue.getId().set(LAST_CUE++);
//    getItems().add(cue);
  }
  
  public void removeMotionCue(IMotionCue cue)
  {
//    getItems().removeIf( x-> x.getId() == cue.getId());
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
}

