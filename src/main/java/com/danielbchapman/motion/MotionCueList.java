package com.danielbchapman.motion;

import java.lang.reflect.Field;
import java.util.ArrayList;

import com.danielbchapman.application.IInternationalized;
import com.danielbchapman.fx.builders.Fx;
import com.danielbchapman.international.MessageUtility;
import com.danielbchapman.international.MessageUtility.Instance;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.util.Callback;

public class MotionCueList extends TableView<MotionCue> implements IInternationalized
{
  Instance msg = MessageUtility.getInstance(MotionCueList.class);
  public ArrayList<IMotionCue> cues = new ArrayList<IMotionCue>();

  public static int LAST_CUE = 1;
  public MotionCueList()
  {
    Field[] fields = MotionCue.class.getFields();
    ArrayList<TableColumn<?, ?>> cols = new ArrayList<>(fields.length);
    
    
    //Columns
    
    getColumns().addAll(
        Fx.column(MotionCue.class, msg("id")),
        Fx.column(MotionCue.class, msg("cueId")),
        Fx.column(MotionCue.class, msg("label")),
        Fx.column(MotionCue.class, msg("description")),
        Fx.column(MotionCue.class, msg("time")),
        Fx.column(MotionCue.class, msg("delay")),
        Fx.column(MotionCue.class, msg("follow"))
        );
    
    //No sorting
    setSortPolicy( x -> { return false; });
    
//    
//    getColumns().addAll(
////        idColumn(),
//        new TableColumn<IMotionCue, String>(msg.get("cueNumber")),
//        new TableColumn<IMotionCue, String>(msg.get("Label"))
//        );
//    
    for(int i = 0; i < 21; i++)
    {
      addMotionCue(new DummyCueItem(new SimpleIntegerProperty(0), new SimpleStringProperty("Test Cue " + i)));
    }
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
