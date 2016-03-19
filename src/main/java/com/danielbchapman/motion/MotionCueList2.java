package com.danielbchapman.motion;

import java.util.ArrayList;

import com.danielbchapman.application.IInternationalized;
import com.danielbchapman.international.MessageUtility;
import com.danielbchapman.international.MessageUtility.Instance;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class MotionCueList2 extends TableView<IMotionCue> implements IInternationalized
{
  Instance _msg = MessageUtility.getInstance(MotionCueList2.class);
  public MotionCueList2()
  {
    setEditable(true);

    ArrayList<IMotionCue> base = new ArrayList<>();
    ObservableList<IMotionCue> data = FXCollections.observableArrayList(base);
    
    for (int i = 0; i < 10; i++)
      data.add(new DummyCueItem(i, "Dummy " + i));

    TableColumn idCol = new TableColumn(msg("id"));
    TableColumn cueCol = new TableColumn(msg("label"));

    idCol.setCellValueFactory(new PropertyValueFactory<IMotionCue, Integer>("id"));
    cueCol.setCellValueFactory(new PropertyValueFactory<IMotionCue, String>("label"));
    
    getColumns().addAll(idCol, cueCol);
    
    setItems(data);
  }

  @Override
  public String msg(String key)
  {
    return _msg.get(key);
  }

  @Override
  public String msg(String key, Object... params)
  {
    return _msg.get(key, params);
  }
}
