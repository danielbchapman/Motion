package shows.oz;

import java.util.Random;

import com.danielbchapman.physics.toxiclibs.PointOLD;

public class Poppy extends PointOLD
{
  int color;
  int shape;
  int size;
  int image = -1;
  int base = 12;
  int rand = 32;
  public boolean isSnow = false;
  
  public Poppy()
  {
    super();
    size = new Random().nextInt(rand) + base;
  }
  
  public Poppy(float x, float y, float z, float w)
  {
    super(x, y, z, w);
    size = new Random().nextInt(rand) + base;
  }
}
