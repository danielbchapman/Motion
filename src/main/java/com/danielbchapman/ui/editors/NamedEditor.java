package com.danielbchapman.ui.editors;

import java.util.function.Consumer;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.Setter;


/**
 * A simple placeholder editor that you can assign a name to.
 *
 */
public class NamedEditor<T extends Node> extends VBox
{
  @Getter
  @Setter
  private Label name;
  
  public NamedEditor(String name, T value)
  {
    getChildren().addAll(new Label(name), value);
  }
}
