package dance2015;

import processing.core.PGraphics;

public class DrawUtil
{
  
  public static FrameCounter getCounter()
  {
    return new FrameCounter();
  }
  
  public static class FrameCounter
  {
    private long last;
    private long current;
    
    FrameCounter()
    {
      last = System.currentTimeMillis();
    }
    
    public void drawText(PGraphics g, int x, int y, int color)
    {
      current = System.currentTimeMillis();
      drawText(g, current, last, x, y, color);
    }
    
    public void drawText(PGraphics g, long current, long last, float x, float y, int color)
    {
      int old = g.backgroundColor;
      g.color(color);
//      long diff = current - last;
      g.text("Frame Rate: " + ((current - last)), x, y);
      g.color(old);
      this.last = current;
    }
  }
}
