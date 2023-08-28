package shows.gravitationalwaves;

import com.danielbchapman.physics.toxiclibs.ActionsOLD;
import com.danielbchapman.physics.toxiclibs.Moveable;
import com.danielbchapman.physics.toxiclibs.Point;
import com.danielbchapman.physics.toxiclibs.Transform;

import lombok.Getter;
import lombok.Setter;
import toxi.geom.Vec3D;

public class TriangleWave implements Moveable
{
  public Point a;
  public Point b;
  public Point c;
  
  private Point[] points;
  public Vec3D aMag;
  public Vec3D bMag;
  public Vec3D cMag;
  
  @Getter
  @Setter
  public int color = 0xFFFFFFFF;
  
  @Getter
  @Setter
  public float weight = 3f;
  
  
  /**
   * Waves base constructor
   */
  protected TriangleWave()
  {
  }
  /**
   * @param x
   * @param y
   * @param z
   * @param rads
   */
  //FIXME Java Doc Needed
  public TriangleWave(float x, float y, float z, float rads)
  {
    float mag = 1f;
    int tX = Transform.size(x, ActionsOLD.WIDTH);
    int tY = Transform.size(y, ActionsOLD.HEIGHT);
    int tZ = 10;
    a = new Point(300, 300, 10, 1);
    b = new Point(300, 300, 10, 1);
    c = new Point(300, 300, 10, 1);
    
    Vec3D base = new Vec3D(1, 0, 0);
    
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
  
  public void push(float velocity)
  {
    
  }

  /* (non-Javadoc)
   * @see com.danielbchapman.physics.toxiclibs.Moveable#getPoints()
   */
  @Override
  public <T extends Point> Point[] getPoints()
  {
   if(points != null)
     return points;
   
   points = new Point[]{a, b, c};
   return points;
  }
}
