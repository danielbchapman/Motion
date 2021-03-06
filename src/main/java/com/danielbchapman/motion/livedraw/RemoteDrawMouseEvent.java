package com.danielbchapman.motion.livedraw;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * A marker interface for the commands
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RemoteDrawMouseEvent implements IRemoteDrawCommand
{
  public float x;
  public float y;
  public boolean down;
}
