package com.danielbchapman.physics.ui;

import java.util.function.BiConsumer;
import java.util.function.Function;

import javax.swing.JCheckBox;

public class CheckBoxProperty<Source> extends JCheckBox
{
  private static final long serialVersionUID = 1L;
  private Source source;
  Function<Source, Boolean> onSync; 
  BiConsumer<Boolean, Source> onUpdate;
  
  public CheckBoxProperty(String label, Source s, Function<Source, Boolean> onSync, BiConsumer<Boolean, Source> onUpdate)
  {
    if(label != null && !label.isEmpty())
      setText(label);
    
    this.source = s;
    this.onSync = onSync;
    this.onUpdate = onUpdate;
    
    addActionListener(
        a->
        {
          onUpdate.accept(isSelected(), source);
        });
  }
  
  public void sync()
  {
    setSelected(onSync.apply(source)); 
  }
}
