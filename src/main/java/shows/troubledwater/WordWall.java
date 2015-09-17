package shows.troubledwater;

import com.danielbchapman.physics.toxiclibs.Action;
import com.danielbchapman.physics.toxiclibs.Actions;
import com.danielbchapman.physics.toxiclibs.Layer;
import com.danielbchapman.physics.toxiclibs.MotionEngine;
import com.danielbchapman.physics.toxiclibs.Point;

import processing.core.PGraphics;

public class WordWall extends Layer
{
	TWCueStack stack;
	public WordWall()
	{
		stack = new TWCueStack(this)
				{

					@Override
					public void load() {
						add(
							cue("START WORDS", (Action[])null),
							cue("WORDS FALL", Actions.dragToVeryLow, Actions.gravityOn),
							cue("WORDS BLOW AWAY", Actions.dragToNone)
						);
						
					}
				
				};
				
		stack.load();
	}
	@Override
	public Point[] init() {
		return new Point[0];
	}

	@Override
	public void render(PGraphics g) {
	}

	@Override
	public void update() {
	}

	@Override
	public void go(MotionEngine engine) {

	}
	
	public String getName()
	{
		return "WordWall";
	}

}
