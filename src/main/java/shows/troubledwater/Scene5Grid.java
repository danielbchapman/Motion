package shows.troubledwater;

import com.danielbchapman.physics.toxiclibs.Action;
import com.danielbchapman.physics.toxiclibs.Actions;
import com.danielbchapman.physics.toxiclibs.LetterGrid;
import com.danielbchapman.physics.toxiclibs.MotionEngine;

public class Scene5Grid extends LetterGrid
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
        		cue("Load Grid"),
        		cue("Start Sine Waves", Actions.lfoOn),
        		cue("FIRE SHATTERING LINES")
        		//Load Grid
        		//Start Sine Waves
        		//Shatter Cues
            );
      }
    };
  }
  
  public void go(MotionEngine engine)
  {
	  stack.go(engine, this);
  }
  public String getName()
  {
    return "Grid";
  }
  
}


