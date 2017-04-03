package shows.oz;

import com.danielbchapman.motion.core.AbstractEmitter;
import com.danielbchapman.physics.toxiclibs.Point;

import processing.core.PGraphics;
import toxi.geom.Vec3D;
import toxi.physics3d.VerletPhysics3D;

public class LeafEmitter extends AbstractEmitter<Leaf>
{

  public LeafEmitter(VerletPhysics3D physics, Vec3D position, Vec3D heading, int lifeSpan, int rate, float randomVector, int randomTime)
  {
    super(physics, position, heading, lifeSpan, rate, randomVector, randomTime);
  }

  @Override
  public Leaf createPoint(float x, float y, float z, float w)
  {
    Leaf l = new Leaf(x, y, z, w);
    return l;
  }

  @Override
  public void draw(PGraphics g)
  {
    for(Point p : this.children)
    {
      g.pushMatrix();
      g.stroke(255,0,0);
      g.fill(255,0,0);
      g.translate(p.x, p.y, p.z);
      Point.rotation(g, p);
      g.ellipse(0, 0, 5, 5);
      g.popMatrix();
    }
  }

}
