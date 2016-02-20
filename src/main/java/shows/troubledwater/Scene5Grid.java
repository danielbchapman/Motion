package shows.troubledwater;

import java.io.File;

import com.danielbchapman.physics.toxiclibs.Actions;
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
        				Actions.dragTo(0.1133f),
        				Actions.loadEnvironment(new File("show/scene5/shatter-environment.env"))
        			),
        		cue("Start Sine Waves", 
        				Actions.lfoOn),
        		load("SHATTER",
        				"show/scene5/shatter",
        				"show/scene5/shatter-brush",
        				Actions.homeOff,
        				Actions.dragToNone,
        				Actions.homeLinearOff
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


