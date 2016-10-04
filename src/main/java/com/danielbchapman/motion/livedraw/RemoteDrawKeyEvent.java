package com.danielbchapman.motion.livedraw;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RemoteDrawKeyEvent implements IRemoteDrawCommand
{
  public char key;
}
