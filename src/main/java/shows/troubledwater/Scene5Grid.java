package shows.troubledwater;

import com.danielbchapman.physics.toxiclibs.GridLayer;

public class Scene5Grid extends GridLayer
{
  TWCueStack stack;
  
  public Scene5Grid()
  {
    stack = new TWCueStack(this)
    {
      private static final long serialVersionUID = 1L;

      @Override
      public void load()
      {
        add(
            );
      }
    };
  }
  public String getName()
  {
    return "Grid";
  } 
}
