package com.danielbchapman.video.onestream;

import processing.core.PApplet;
import processing.core.PGraphics;

public class DrawingUtil
{
  public static final int X_AXIS = 0;
  public static final int Y_AXIS = 1;
  
  public static void drawGradient(PGraphics g, int colorA, int colorB, int x, int y, float w, float h, int axis)
  {
    g.fill(255,255,0, 128);
    //g.rect(x, y, w, h);
    g.noFill();
    if (axis == Y_AXIS) {  // Top to bottom gradient
      for (int i = y; i <= y+h; i++) {
        float inter = PApplet.map(i-1, y+1, y+h, 0, 1);
        if(inter <= 0.008032128f)
          inter = 0.008032128f;//acceptable minimum for a "1";
        if(inter > 0.99196786f)
          inter = 0.99196786f;
        //System.out.println("Y->" + inter);
        int c = g.lerpColor(colorA, colorB, inter);
        g.stroke(c);
        g.line(x, i, x+w, i);
      }
    }  
    else if (axis == X_AXIS) {  // Left to right gradient
      for (int i = x; i <= x+w; i++) {
        float inter = PApplet.map(i-1, x+1, x+w, 0, 1);
        if(inter <= 0.008032128f)
          inter = 0.008032128f;//acceptable minimum for a "1";
        if(inter > 0.99196786f)
          inter = 0.99196786f;
        //System.out.print("(" + i + ", " + y + ") to (" + i + ", " + (y + h) +") (" + inter + ")");
        int c = g.lerpColor(colorA, colorB, inter);
        //byte[] b =Utility.encode(c); 
        //System.out.println(" :: C ->" + b[0] + " | " + b[1] + " | " + b[2] + " | " + b[3] +" |");
        g.stroke(c);
        g.line(i, y, i, y+h);
      }
    }
  }
  
  /**
   * Draws a gradient from angle X to angle Y. This isn't perfect, there is a problem
   * with the final "line" being drawn on each side. I recommend a one pixel overlap
   * for blending needs.
   * 
   * @param g the grapghics context
   * @param c1 color a
   * @param c2 color b
   * @param x center(x)
   * @param y center(y)
   * @param r (radius) (internally multiplied by 2
   * @param start the start radian
   * @param stop the end radian  
   * 
   */
  public static void drawGradientRadial(PGraphics g, int c1, int c2, int x, int y, int r, float start, float stop)
  {
    
    g.fill(255);
    g.stroke(0);
    int radius = r*2; //arcs draw based on an ellipse (diameter)
    //g.arc(x, y, radius, radius, start, stop);
    
    g.noFill();
    for(int i = 1; i <= radius; i++ )
    {
      float inter = PApplet.map(x+i, x, x + radius, 0, 1);
      int c = g.lerpColor(c1, c2, inter);
      g.stroke(c);;
      g.arc(x, y, i, i, start, stop);
    }
  }
  
  public static void drawGrid(PGraphics g, int x, int y, int w, int h, int spacing, int color)
  {
    int xcount = w/spacing;
    int ycount = h/spacing;
    g.stroke(color);
    g.noFill();
    for(int i = 0; i < xcount; i++)
      g.line(x+(i *spacing), y, x+(i *spacing), y+h);
    for(int i = 0; i < ycount; i++)
      g.line(x, y + (i * spacing), x+w, y+ (i * spacing)); 
  }
}
