package shows.troubledwater;

import com.danielbchapman.physics.toxiclibs.Actions;
import com.danielbchapman.physics.toxiclibs.Layer;
import com.danielbchapman.physics.toxiclibs.MotionEngine;

public class Interlude2 extends PaintingLayer 
{
  Interlude2Stack stack = new Interlude2Stack(this);  
  public class Interlude2Stack extends TWCueStack
  {
    public Interlude2Stack(Layer layer)
    {
      super(layer);
    }

    @Override
    public void load()
    {
      String bigToSmall = "/show/Interlude0/default-brush";
      
      add(
          load("Stroke 1",
            "show/Interlude0/3-1",
            bigToSmall,
            Actions.follow(1000)),
          load("Stroke 2",
              "show/Interlude0/3-2",
              bigToSmall,
              Actions.follow(1000)),
          load("Stroke 3",
              "show/Interlude0/3-3",
              bigToSmall,
              Actions.follow(1000)),
          load("Stroke 4",
              "show/Interlude0/3-4",
              bigToSmall,
              Actions.follow(1000)),
          load("Stroke 6",
              "show/Interlude0/3-6",
              bigToSmall,
              Actions.follow(1000))
        );
      
    }
    
  }

  @Override
  public String getSpecificName()
  {
    return "Interlude2";
  }
}
