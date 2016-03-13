package com.danielbchapman.motion;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class DummyCueItem implements IMotionCue
{
  @Getter
  @Setter
  private int id;
  @Getter
  @Setter
  private String label;
  
}
