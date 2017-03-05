package shows.troubledwater;

import java.util.Random;

import processing.core.PGraphics;
import toxi.geom.Vec3D;

import com.danielbchapman.brushes.ImageBrush;
import com.danielbchapman.physics.toxiclibs.Emitter;

public class RainEmitter extends Emitter<BrushPoint>
{
  RainLayer parent;
  public RainEmitter(RainLayer layer, Vec3D position, Vec3D heading, int lifeSpan, int rate, float randomVector, int randomTime)
  {
    super(position, heading, lifeSpan, rate, randomVector, randomTime);
    this.parent = layer;
  }

  @Override
  public BrushPoint createPoint(float x, float y, float z, float w)
  {
    Random rand = new Random();
    int randX = rand.nextInt(800) - 200;
    int randY = rand.nextInt(100) * -1;
    float randMax = rand.nextFloat();
    if(randMax < .1f)
      randMax = .1f;
    BrushPoint p = new BrushPoint(randX, randY, z, w);
    p.getWrap().maxSize = randMax;
    return p;
  }

  @Override
  public void draw(PGraphics g)
  {
    for(BrushPoint p  : children)
    {
      ImageBrush b = p.getWrap();
      if(!b.isDrawing())
      {
        b.startDraw();
      }
      
      b.setPosition(new Vec3D(p.x, p.y, p.z));
      parent.renderBrush(b, g, -1);
      //b.draw(g);
      
//      g.fill(255);
//      g.stroke(255);
//      g.ellipse(p.x, p.y, 5, 5);
    }
      
  }
}