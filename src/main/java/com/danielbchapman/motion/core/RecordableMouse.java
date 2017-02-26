package com.danielbchapman.motion.core;

import lombok.Data;

@Data
public class RecordableMouse
{
  boolean left;
  boolean right;
  boolean center;
  int mouseX; 
  int mouseY; 
  int pmouseX;
  int pmouseY;
  int mouseZ = 0; //not often used
  int pmouseZ = 0;
  MotionBrush brush;
}
