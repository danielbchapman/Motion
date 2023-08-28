package shows.troubledwater;

import java.io.File;

import com.danielbchapman.physics.toxiclibs.ActionsOLD;
import com.danielbchapman.physics.toxiclibs.SquareGrid;
import com.danielbchapman.physics.toxiclibs.MotionEngine;

public class Scene5Grid extends SquareGrid
{
  TWCueStack stack;
  
  public Scene5Grid()
  {
    stack = new TWCueStack(this)
    {
      @Override
      public void load()
      {
        add(
        		cue("Load Grid", 
        				ActionsOLD.dragTo(0.1133f),
        				ActionsOLD.loadEnvironment(new File("show/scene5/shatter-environment.env"))
        			),
        		cue("Start Sine Waves", 
        				ActionsOLD.lfoOn),
        		load("SHATTER",
        				"show/scene5/shatter",
        				"show/scene5/shatter-brush",
        				ActionsOLD.homeOff,
        				ActionsOLD.dragToNone,
        				ActionsOLD.homeLinearOff
        				)
        		//Load Grid
        		//Start Sine Waves
        		//Shatter Cues
            );
      }
    };
    
    stack.load();
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


