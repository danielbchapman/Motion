package shows.gravitationalwaves;

import com.danielbchapman.physics.toxiclibs.Actions;
import com.danielbchapman.physics.toxiclibs.Drawable;
import com.danielbchapman.physics.toxiclibs.Moveable;
import com.danielbchapman.physics.toxiclibs.Point;
import com.danielbchapman.physics.toxiclibs.Transform;

import processing.core.PConstants;
import processing.core.PGraphics;
import toxi.geom.Vec3D;

public class SplineWave implements Moveable, Drawable
{  
  Point a;
  Point ab1;
  Point ab2;
  Point b;
  Point bc1;
  Point bc2;
  Point c;
  Point ca1;
  Point ca2;
  
  Point[] points;
  /**
   * @param x
   * @param y
   * @param z
   * @param rads
   */
  //FIXME Java Doc Needed
  public SplineWave(float x, float y, float z)
  {
    float rads = (float) (Math.PI / 3 * 2);
    float mag = 1f;
    float offsetMag = .5f;
    int tX = Transform.size(x, Actions.WIDTH);
    int tY = Transform.size(y, Actions.HEIGHT);
    int tZ = 10;
    a = new Point(tX, tY, tZ, 1);
    ab1 = new Point(tX, tY, tZ, 1);
    ab2 = new Point(tX, tY, tZ, 1);
    b = new Point(tX, tY, tZ, 1);
    bc1 = new Point(tX, tY, tZ, 1);
    bc2= new Point(tX, tY, tZ, 1);
    c = new Point(tX, tY, tZ, 1);
    ca1 = new Point(tX, tY, tZ, 1);
    ca2 = new Point(tX, tY, tZ, 1);
    
    Vec3D base = new Vec3D(1, 0, 0);
    
    Vec3D aMag = base.copy().rotateZ(rads);
    Vec3D bMag = aMag.copy().rotateZ(rads);
    Vec3D cMag = bMag.copy().rotateZ(rads);
    
    aMag.scale(mag);
    bMag.scale(mag);
    cMag.scale(mag);
    
    a.addForce(aMag);
    ab1.addForce(aMag.copy().rotateZ(rads/3).scale(1f+offsetMag));
    ab2.addForce(aMag.copy().rotateZ(rads/3*2).scale(1f-offsetMag));
    b.addForce(bMag);
    bc1.addForce(bMag.copy().rotateZ(rads/3).scale(1f+offsetMag));
    bc2.addForce(bMag.copy().rotateZ(rads/3*2).scale(1f-offsetMag));
    c.addForce(cMag);
    ca1.addForce(cMag.copy().rotateZ(rads/3));
    ca2.addForce(cMag.copy().rotateZ(rads/3*2).scale(1f-offsetMag));
  }

  @Override
  public <T extends Point> Point[] getPoints()
  {
    if(points != null)
      return points;
    
    points = new Point[]{a,ab1, ab2, b, bc1, bc2, c, ca1, ca2};
    return points;
  }

  @Override
  public void draw(PGraphics g)
  {
    g.pushMatrix();
    g.noFill();
    g.bezier(a.x, a.y, a.z, ab1.x, ab1.y, ab1.z, ab2.x, ab2.y, ab2.z, b.x, b.y, b.z);
    g.bezier(b.x, b.y, b.z, bc1.x, bc1.y, bc1.z, bc2.x, bc2.y, bc2.z, c.x, c.y, c.z);
    g.bezier(c.x, c.y, c.z, ca1.x, ca1.y, ca1.z, ca2.x, ca2.y, ca2.z, a.x, a.y, a.z);
    g.popMatrix();
    
    g.ellipseMode(PConstants.CENTER);
    for(Point p : points)
    {
      g.pushMatrix();
      g.ellipse(p.x, p.y, 10, 10);
      g.popMatrix();
    }
  }
}
