package shows.oz;

import java.util.Random;

import com.danielbchapman.physics.toxiclibs.PointOLD;

public class Bubble extends PointOLD
{
  int color;
  int shape;
  int size;
  
  public Bubble()
  {
    super();
    size = new Random().nextInt(64) + 24;
  }
  
  public Bubble(float x, float y, float z, float w)
  {
    super(x, y, z, w);
    size = new Random().nextInt(64) + 24;
  }
}
