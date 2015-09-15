package shows.troubledwater;

import com.danielbchapman.physics.toxiclibs.Actions;
import com.danielbchapman.physics.toxiclibs.Layer;
import com.danielbchapman.physics.toxiclibs.MotionEngine;

public class Interlude0 extends PaintingLayer
{

  public Interlude0(MotionEngine engine)
  {
    super(engine);
  }

  @Override
  public String getSpecificName()
  {
    return "Interlude0";
  }
  
  Interlude0Stack stack = new Interlude0Stack(this);  
  public class Interlude0Stack extends TWCueStack
  {
    public Interlude0Stack(Layer layer)
    {
      super(layer);
    }

    private static final long serialVersionUID = 1L;

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
}
