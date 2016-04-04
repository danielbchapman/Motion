package shows.gravitationalwaves;

import com.danielbchapman.physics.toxiclibs.Actions;
import com.danielbchapman.physics.toxiclibs.Moveable;
import com.danielbchapman.physics.toxiclibs.Point;
import com.danielbchapman.physics.toxiclibs.Transform;

import toxi.geom.Vec3D;

public class TriangleWaveCrossing extends TriangleWave
{  
  /**
   * @param x
   * @param y
   * @param z
   * @param rads
   */
  //FIXME Java Doc Needed
  public TriangleWaveCrossing(float x, float y, float z, float rads)
  {
    float mag = 1f;
    
    int tX = Transform.size(x, Actions.WIDTH);
    int tY = Transform.size(y, Actions.HEIGHT);
    int tZ = 10;
    a = new Point(tX, tY, tZ, 1);
    b = new Point(tX, tY, tZ, 1);
    c = new Point(tZ, tY, tZ, 1);
    
    Vec3D base = new Vec3D(1, 0, 0);
    ;
    aMag = base.copy().rotateZ(rads);
    bMag = aMag.copy().rotateZ(rads);
    cMag = bMag.copy().rotateZ(rads);
    
    aMag.scale(mag);
    bMag.scale(mag);
    cMag.scale(mag);
    
    a.addForce(aMag);
    b.addForce(bMag);
    c.addForce(cMag);
  }
}
