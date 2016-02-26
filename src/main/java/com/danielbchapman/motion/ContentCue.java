package com.danielbchapman.motion;

import java.math.BigDecimal;

import com.danielbchapman.utility.Utility;

import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class ContentCue extends UICue<IPlaceholder>
{
  VBox content = new VBox();
  
  public ContentCue()
  {
    TextField title = new TextField(getLabel());
    TextField number = new TextField(Utility.ifNull(getCueNumber(), BigDecimal.ZERO).toString());
    
    content.getChildren()
      .addAll(
        number,
        title
        );
    
    getChildren().addAll(content);
  }
}
