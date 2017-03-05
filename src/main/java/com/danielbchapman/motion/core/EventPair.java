package com.danielbchapman.motion.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
class EventPair
{
  public MotionMouseEvent event;
  public MotionBrush brush;
  
}
