package shows.troubledwater;

import com.danielbchapman.brushes.SmallBrush;
import com.danielbchapman.layers.BleedingCanvasLayer;
import com.danielbchapman.physics.toxiclibs.Actions;
import com.danielbchapman.physics.toxiclibs.MotionEngine;

public class HeroLayer extends BleedingCanvasLayer
{
	TWCueStack stack;
	public HeroLayer(MotionEngine engine) {
		super(engine);
		stack = new TWCueStack(this)
				{
					SmallBrush pen = new SmallBrush();
					@Override
					public void load() {
						add(
							cue("PRESET"),
							load("1", 
								"show/hero/hero-1-1", 
								pen, 
								Actions.follow(3000)),
							load("1", 
									"show/hero/hero-1-2", 
									pen, 
									Actions.follow(3000)),
							load("1", 
									"show/hero/hero-1-3", 
									pen, 
									Actions.follow(3000)),
							load("3", 
									"show/hero/hero-3-1", 
									pen, 
									Actions.follow(3000)),
							load("3", 
									"show/hero/hero-3-2", 
									pen, 
									Actions.follow(3000)),
							load("3", 
									"show/hero/hero-3-3", 
									pen, 
									Actions.follow(3000)),
							load("3", 
									"show/hero/hero-3-4", 
									pen, 
									Actions.follow(3000))
						);
					}
			
				};
		stack.load();
		this.go(engine);
	}
	
	public void go(MotionEngine engine)
	{
		stack.go(engine, this);
	}
	
	public String getName()
	{
		return "HeroLayer";
	}
}
