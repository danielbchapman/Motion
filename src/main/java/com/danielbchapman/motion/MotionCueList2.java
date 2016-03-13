package com.danielbchapman.motion;

import javafx.scene.control.ListView;

public class MotionCueList2 extends ListView<IMotionCue>
{
  public MotionCueList2()
  {
    setEditable(true);
    
    for(int i = 0; i < 10; i++)
    {
      getItems().add(new DummyCueItem(1, "Dummy " + i));
    }
  }
}
