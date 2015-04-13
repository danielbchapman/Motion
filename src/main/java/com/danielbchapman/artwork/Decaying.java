package com.danielbchapman.artwork;

import toxi.physics3d.VerletPhysics3D;

public abstract class Decaying extends Fadeable
{
  public boolean decaying = false;
  
  public Decaying(int opacityStart, int opacityEnd, long count, long delay)
  {
    super(opacityStart, opacityEnd, count, delay);
  }
  
  public void decay(long time)
  {
    //Set up the fade variables to fade to black over the required time.
    decaying = true;
    finished = false;
    count = time;
    opacityStart = opacity;
    opacityEnd = 0;
    start = -1L;
  }
  
  @Override
  public int color(long time, int color) 
  {
    if(decaying)
    {
      if(finished){
        onDecayComplete();
      }
      return super.color(time, color);
    }
    else
      return super.color(time, color);
  };

  /**
   * This method is called when the decay is completed, perform all 
   * cleanup that you need when this is called which likely involves cleaning up 
   * a data-structure and removing this Decaying instance.
   */
  public abstract void onDecayComplete();
}
