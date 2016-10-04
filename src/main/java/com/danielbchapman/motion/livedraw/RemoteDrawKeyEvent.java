package com.danielbchapman.motion.livedraw;

import processing.event.KeyEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RemoteDrawKeyEvent implements IRemoteDrawCommand
{
  char key;
  int code;
}
