package shows.troubledwater;

import com.danielbchapman.layers.BleedingCanvasLayer;
import com.danielbchapman.physics.toxiclibs.MotionEngine;

public class Scene3 extends BleedingCanvasLayer
{
	TWCueStack stack;
	public Scene3(MotionEngine engine) {
		super(engine);
		stack = new TWCueStack(this)
				{
					private static final long serialVersionUID = 1L;

					@Override
					public void load() {
						add(
								cue("Water and Chaos on the Bridge"),
								cue("Freeze the water"),
								cue("slow strokes on the water"),
								cue("low lapping brush strokes")								
								);
					}
			
				};
		loadStack(stack);
	}
	
	
	public void loadStack(TWCueStack stack)
	{
	}
	
	public String getName()
	{
		return "Scene3";
	}

}
