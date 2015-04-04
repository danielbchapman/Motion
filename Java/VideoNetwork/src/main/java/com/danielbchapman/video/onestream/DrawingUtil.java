package com.danielbchapman.video.onestream;

import processing.core.PApplet;
import processing.core.PGraphics;

public class DrawingUtil
{
  public static final int X_AXIS = 0;
  public static final int Y_AXIS = 1;
  
  public static void drawGradient(PGraphics g, int colorA, int colorB, int x, int y, float w, float h, int axis)
  {
    if (axis == Y_AXIS)
    { // Top to bottom gradient
      for (int i = y + 1; i < y + h; i++)
      {
        float inter = PApplet.map(i, y, y + h, 0, 1);
        int c = g.lerpColor(colorA, colorB, inter);
        g.stroke(c);
        g.line(x, i - 1, x + w, i - 1);
      }
    }
    else
      if (axis == X_AXIS)
      { // Left to right gradient
        for (int i = x + 1; i < x + w; i++)
        {
          float inter = PApplet.map(i, x, x + w, 0, 1);
          int c = g.lerpColor(colorA, colorB, inter);
          g.stroke(c);
          g.line(i - 1, y, i - 1, y + h);
        }
      }
  }
}
