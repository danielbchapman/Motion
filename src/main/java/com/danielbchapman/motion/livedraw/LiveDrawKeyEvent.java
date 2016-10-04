package com.danielbchapman.motion.livedraw;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LiveDrawKeyEvent implements ILiveDrawCommand
{
  char key;
}
