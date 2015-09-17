package shows.troubledwater;

import com.danielbchapman.brushes.ImageBrush;
import com.danielbchapman.layers.BleedingCanvasLayer;
import com.danielbchapman.physics.toxiclibs.Action;
import com.danielbchapman.physics.toxiclibs.Actions;
import com.danielbchapman.physics.toxiclibs.MotionEngine;

import lombok.Getter;
import lombok.Setter;
import processing.core.PGraphics;

public class Bridge extends BleedingCanvasLayer
{
	TWCueStack stack;
	public Bridge(MotionEngine engine) {
		super(engine);
		stack = new TWCueStack(this)
				{
					private static final long serialVersionUID = 1L;

					@Override
					public void load() {
						ImageBrush brush = new ImageBrush();
						add(
								load("Water and Chaos on the Bridge",
										"show/bridge/shake-a-sheet",
										brush,
										startBleed()),
								cue("Freeze the water",
										stopBleed(),
										Actions.follow(700)),
								cue("STOP BLEED", 
									Actions.stopPlayback()
									),
								
								//BIG SLASH
								load("SLASH-CUE: Slash 1",
										"show/bridge/slash-1",
										brush,
										startBleed(),
										Actions.follow(250)),
								load("Slash 2",
										"show/bridge/slash-2",
										brush,
										Actions.follow(350)),
								load("Slash 3",
										"show/bridge/slash-3",
										brush,
										stopBleed()
										),
								//Do wee need this?
								cue("Stop Playback", Actions.stopPlayback()),
								
								cue("slow strokes on the water",
										startBleed(), Actions.follow(100)),
								load("Slow Shake",
										"show/bridge/slow-shake-a-sheet", 
										brush),
								cue("low lapping brush strokes"),
								//More slashes
								load("Water and Chaos on the Bridge",
										"show/bridge/shake-a-sheet",
										brush,
										startBleed())
								);
					}
			
				};
		stack.load();
	}
	
	public Action stopBleed()
	{
		return new Action("Stop Bleed" , 0, (x)->{bleed = false;}, null);
	}
	
	public Action startBleed()
	{
		return new Action("Stop Bleed" , 0, (x)->{bleed = true;}, null);
	}
	public void loadStack(TWCueStack stack)
	{
	}
	
	public String getName()
	{
		return "Bridge";
	}
	
	boolean first = true;
	@Getter
	@Setter
	boolean bleed = true;
	@Override
	public void render(PGraphics g)
	{
		if(first)
		{
			g.background(0);
			first = false;
		}
		if(bleed)
		{
		    g.fill(0,0,0, 2);
		    g.stroke(0,0,0, 2);
		    g.rect(0, 0, this.engine.getWidth(), this.engine.getHeight());
		}

	    super.renderAfterBrushes(g);
	}
	
	public void go(MotionEngine engine)
	{
		stack.go(engine, this);
	}

}
