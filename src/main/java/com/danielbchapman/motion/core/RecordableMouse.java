package com.danielbchapman.motion.core;

import lombok.Data;

@Data
public class RecordableMouse
{
  boolean mouseDown;
  int mouseX, mouseY, pmouseX, pmouseY;
  MotionBrush brush;
}
