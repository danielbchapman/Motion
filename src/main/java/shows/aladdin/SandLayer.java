package shows.aladdin;

import java.util.Arrays;

import com.danielbchapman.motion.core.BaseScene;
import com.danielbchapman.motion.core.Motion;
import com.danielbchapman.motion.core.PhysicsBrush;
import com.danielbchapman.motion.core.PhysicsScene;
import com.danielbchapman.physics.toxiclibs.Point;

import processing.core.PGraphics;
import processing.core.PImage;

public class SandLayer extends PhysicsScene
{
  Motion motion;
  PImage source;
  int[] pixels;
  PointC[] points;
  
  @Override
  public void initialize(Motion motion)
  {
    super.initialize(motion);
    this.motion = motion;
    this.pixels = new int[motion.WIDTH * motion.HEIGHT];
    
    setImage("aladdin/ancient-prison.jpg");
    
    int multiplier = 3;
    
    this.points = new PointC[motion.WIDTH * motion.HEIGHT / multiplier / multiplier]; //every third pixel
    
    
    for(int i = 0, j = 0; i < points.length; i++, j+=multiplier)
    {
      int width = j % motion.WIDTH * multiplier;
      int height = (int) (j / motion.HEIGHT) * multiplier;
      
      int index = width + motion.HEIGHT * height;
      int color = index >= pixels.length ? 0x000000 : pixels[index];
      PointC p = new PointC(width, height, 0, 1, color);
      
      points[i] = p;
    }
  }
  
  public void setImage(String path)
  {
    PImage item = motion.loadImage(path);
    item.resize(motion.WIDTH, motion.HEIGHT);
    
    this.source = item;
    
    int[] newPixels = new int[motion.WIDTH * motion.HEIGHT];
    
    int[] copy = Arrays.copyOf(source.pixels, source.pixels.length);
    this.pixels = copy;
  }
  
  @Override
  public void update(long time)
  { 
  }
  
  @Override
  public void debug(PGraphics g)
  {
    g.fill(255,0,255);
    g.stroke(255,0,255);
    
    g.pushMatrix();
    g.tint(255,255,255,196);
    g.image(source, 0, 0);
    g.popMatrix();
    
    g.text("SAND LAYER", 60, 60, 0);
  }
  
  @Override
  public void draw(PGraphics g)
  {
    g.fill(0, 0, 0);
    g.rect(0, 0, g.width, g.height);
    
    if(points != null)
    {
      for(int i = 0; i < points.length; i++)
      {
        //Point p = points[i];
//        g.pushMatrix();
//        g.translate(points[i].x, points[i].y, points[i].z);
        //g.translate(p.x, p.y, p.z);
        g.stroke(points[i].color);
        g.strokeWeight(9);
//        g.point(0, 0);
//        g.popMatrix();
//        
        g.point(points[i].x, points[i].y, points[i].z);
      }
    }
  }
  
  static class PointC extends Point
  {
    int color;
    public PointC(float x, float y, float z, float w, int c)
    {
      super(x, y, z, w);
      this.color = c;
    }
  }
}
