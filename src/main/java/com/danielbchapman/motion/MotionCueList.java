package com.danielbchapman.motion;

import java.util.ArrayList;

import com.danielbchapman.international.MessageUtility;
import com.danielbchapman.international.MessageUtility.Instance;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.util.Callback;

public class MotionCueList extends TableView<IMotionCue>
{
  Instance msg = MessageUtility.getInstance(MotionCueList.class);
  public ArrayList<IMotionCue> cues = new ArrayList<IMotionCue>();
  
  public static int LAST_CUE = 1;
  public MotionCueList()
  {
    //No sorting
    setSortPolicy( x -> { return false; });
    getColumns().addAll(
        idColumn(),
        new TableColumn<IMotionCue, String>(msg.get("cueNumber")),
        new TableColumn<IMotionCue, String>(msg.get("Label"))
        );
    
    for(int i = 0; i < 21; i++)
    {
      addMotionCue(new DummyCueItem(0, "Test Cue " + i));
    }
  }
  
  public void addMotionCue(IMotionCue cue)
  {
    LAST_CUE++;
    cue.setId(LAST_CUE);
    getItems().add(cue);
  }
  
  public void removeMotionCue(IMotionCue cue)
  {
    getItems().removeIf( x-> x.getId() == cue.getId());
  }
  
  private TableColumn<IMotionCue, String> idColumn()
  {
    TableColumn<IMotionCue, String> column = new TableColumn<>();
    
    column.setCellValueFactory(new Callback<CellDataFeatures<IMotionCue, String>, ObservableValue<String>>()
    {
      public SimpleObjectProperty<String> call(CellDataFeatures<IMotionCue, String> callback)
      {
        Integer value = callback.getValue().getId();
        return new SimpleObjectProperty<String>(value.toString());
      }
    });
    
    column.setText(msg.get("idColumn"));
    column.setEditable(true);
    return column;
  }
}
