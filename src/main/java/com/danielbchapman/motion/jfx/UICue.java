package com.danielbchapman.motion.jfx;

import java.math.BigDecimal;

import javafx.scene.layout.Pane;
import lombok.Getter;
import lombok.Setter;


/**
 * A placeholder class for the cue editor in the cue sheets. This
 * is a pretty basic abstract pattern. Any "cues" that are edited in
 * the cue list extend this abstract type.
 *
 * @author Daniel B. Chapman
 * @since Dec 21, 2015
 * @version 0.1 - Development
 * @link http://cue.lighting
 *
 */
public abstract class UICue<T> extends Pane
{
  @Getter
  @Setter
  private BigDecimal cueNumber;
  
  @Getter
  @Setter
  private String label;
  
}
