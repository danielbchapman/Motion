package shows.troubledwater;

import java.util.ArrayList;

import processing.core.PGraphics;
import processing.core.PImage;

import com.danielbchapman.brushes.EllipseBrush;
import com.danielbchapman.brushes.ImageBrush;
import com.danielbchapman.brushes.SaveableBrush;
import com.danielbchapman.physics.toxiclibs.Layer;
import com.danielbchapman.physics.toxiclibs.MotionEngine;
import com.danielbchapman.physics.toxiclibs.Point;

public class RecordingLayer extends Layer
{

  boolean blank;
  int mode = 0;
  int index = -1;
  ArrayList<PImage> images;
  
  public RecordingLayer(MotionEngine engine)
  {
    this.engine = engine;
  }
  
  @Override
  public Point[] init()
  {
    return new Point[0];
  }

  @Override
  public void render(PGraphics g)
  {
    if(images == null)
    {
      images = new ArrayList<>();
      String[] files = new String[]
          {
            "show/one-leaf-draw.png",
            "show/HeroPoem.png",
            "show/hero2.png",
            "show/hero22.png",
            "show/hero23.png",
            "show/Hero-1.jpeg",
            "show/Hero-2.jpeg",
            "show/Hero-3.jpeg",
            "show/Hero.jpeg"
          };
      
      for(String s : files)
      {
        try
        {
          PImage x = engine.loadImage(s);
          images.add(x);
        }
        catch (Throwable t)
        {
          t.printStackTrace();
        }
      }
      
      if(images.size() > 0)
        index = 0;
    }
    //If this isn't setup set it up
    if(!blank)
    {
      g.background(0);
      try{
        if(index > -1)
        {
          g.image(images.get(index), 100, 0, 600, 600);
        }
       // PImage image = engine.loadImage("show/OneLeaf.PNG");
//        image.filter(PConstants.INVERT);
//        g.image(image, 100, 50, 600, 600);
      }
      catch(Throwable t)
      {
        t.printStackTrace();
      }
      blank = true;
    }
  }

  @Override
  public void update()
  {

  }

  @Override
  public void go(MotionEngine engine)
  {
    System.out.println("GO FIRED");
    if(index > -1)
    {
      index++;
      if(images != null && images.size() > 0)
        index = index % images.size();
    }
      
    mode++;
        
    blank = false;//clear the canvas
  }
  @Override
  public void renderBrush(SaveableBrush brush, PGraphics g, int currentFrame)
  {
    brush.update();
    brush.draw(g);
  }
}
