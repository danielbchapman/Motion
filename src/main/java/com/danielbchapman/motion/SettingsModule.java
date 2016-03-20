package com.danielbchapman.motion;

import com.danielbchapman.application.Module;
import com.danielbchapman.application.ScopeType;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class SettingsModule extends Module
{
  
  VBox parent;
  @Override
  public ScopeType getScopeType()
  {
    return ScopeType.MODULE;
  }

  @Override
  protected Node getNode()
  {
    if(parent != null)
      return parent;
    
    parent = new VBox();
    parent.getChildren().addAll(
        new Label("Settings Module"),
        new Label("..that isn't finished at all!")
        );
    
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

}
