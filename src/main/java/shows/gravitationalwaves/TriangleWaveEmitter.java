package shows.gravitationalwaves;

import com.danielbchapman.physics.toxiclibs.AbstractEmitter;

import processing.core.PGraphics;
import toxi.geom.Vec3D;

public abstract class TriangleWaveEmitter extends AbstractEmitter<TriangleWave>
{
  public TriangleWaveEmitter(Vec3D position)
  {
    super(position);
    setLifeSpan(30000);
  }

  @Override
  public void draw(PGraphics g)
  {
    for(TriangleWave w : children)
    {
      g.stroke(w.color);
      g.strokeWeight(w.weight);
      g.pushMatrix();
      g.text("LINE A", w.a.x, w.a.y, w.a.z);
      g.line(w.a.x, w.a.y, w.a.z, w.b.x, w.b.y, w.b.z);
      g.line(w.b.x, w.b.y, w.b.z, w.c.x, w.c.y, w.c.z);
      g.line(w.c.x, w.c.y, w.c.z, w.a.x, w.a.y, w.a.z);
      g.popMatrix();
    }
  }
}
