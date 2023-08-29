package shows.ululations;

import com.danielbchapman.brushes.ImageBrush;

public class RainBrush extends ImageBrush
{
  public RainBrush()
  {
    super();
    maxOpacity = 96;
    minOpacity = 16;
    opacityStart = 128;
    opacityEnd = 16;
    
    fadeTime = 5000;
  }

}
