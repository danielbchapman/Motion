package shows.troubledwater;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.danielbchapman.brushes.ImageBrush;
import com.danielbchapman.layers.BleedingCanvasLayer;
import com.danielbchapman.physics.toxiclibs.ActionOLD;
import com.danielbchapman.physics.toxiclibs.ActionsOLD;
import com.danielbchapman.physics.toxiclibs.Cue;
import com.danielbchapman.physics.toxiclibs.CueStack;
import com.danielbchapman.physics.toxiclibs.Layer;
import com.danielbchapman.physics.toxiclibs.MotionEngine;
import com.danielbchapman.physics.toxiclibs.PointOLD;

import processing.core.PGraphics;

public class CourtesanLayer extends BleedingCanvasLayer {

	boolean first = true;
	Stack stack;
	public CourtesanLayer() {
		stack = new Stack();
	}

	@Override
	public PointOLD[] init() {
		return new PointOLD[0];
	}

	@Override
	public void render(PGraphics g) {
		if(first) {
			g.background(0);
			first = false;
		}
	}

	@Override
	public void update() {

	}

	@Override
	public void go(MotionEngine engine) {
		stack.go(engine, this);

	}

	
	public class Stack extends CueStack
	{
		public Stack()
		{
			String brush = "show/scene3/scene-3-paint";
			MotionEngine.brush = new ImageBrush();
			
			add(
				load("Stroke 1",
					"show/scene3/3-1",
					brush,
					ActionsOLD.follow(1000)),
				load("Stroke 2",
						"show/scene3/3-2",
						brush,
						ActionsOLD.follow(1000)),
				load("Stroke 3",
						"show/scene3/3-3",
						brush,
						ActionsOLD.follow(1000)),
				load("Stroke 4",
						"show/scene3/3-4",
						brush,
						ActionsOLD.follow(1000)),
				load("Stroke 5",
						"show/scene3/side-stroke",
						brush,
						ActionsOLD.follow(1000)),
				load("Stroke 6",
						"show/scene3/3-6",
						brush,
						ActionsOLD.follow(1000)),
				load("Stroke 7",
						"show/scene3/long-stroke",
						brush,
						ActionsOLD.follow(1000))
			);
		}
	}
	
	public Cue cue(String label, ActionOLD ... actions)
	  {
	    return cue(label, null, actions);
	  }
	  public Cue cue(String label, List<ActionOLD> post, ActionOLD ... pre)
	  {
	    ArrayList<ActionOLD> list = new ArrayList<ActionOLD>();
	    if(pre != null)
	      for(ActionOLD a : pre)
	        list.add(a);
	    
	    if(post != null)
	      for(ActionOLD a : post)
	        list.add(a);
	    
	    Cue cue = new Cue(label, list);
	    return cue;
	  }
	  
	  public Cue load(String label, String env, String file, String brushFile, ActionOLD ... post)
	  {
	    ArrayList<ActionOLD> acts = ActionsOLD.loadRecordingAsAction(new File(file), new File(brushFile));
	    ActionOLD loadEnv = ActionsOLD.loadEnvironment(new File(env));
	    if(post != null)
	      for(ActionOLD a : post)
	        acts.add(a);
	    return cue(label, acts, loadEnv);
	  }
	  
	  public Cue load(String label, String file, String brush)
	  {
	    ArrayList<ActionOLD> acts = ActionsOLD.loadRecordingAsAction(new File(file), new File(brush));
	    return cue(label, acts);
	  }
	  
	  public Cue load(String file, String brushFile)
	  {
	    return ActionsOLD.loadRecording(new File(file), new File(brushFile));
	  }
	  
	  public Cue load(String label, String file, String brushFile, ActionOLD ... post)
	  {
	    ArrayList<ActionOLD> acts = ActionsOLD.loadRecordingAsAction(new File(file), new File(brushFile));
	    if(post != null)
	      for(ActionOLD a : post)
	        acts.add(a);
	    return cue(label, acts);
	  }
	  
	  public Cue load(String label)
	  {
	    return cue(label);
	  }
	  public ActionOLD action(String label, Consumer<Layer> fL, Consumer<MotionEngine> fE)
	  {
	    return new ActionOLD(label, 0, fL, fE);
	  }
	  public ActionOLD action(String label, Consumer<Layer> fL, Consumer<MotionEngine> fE, int delay)
	  {
	    return new ActionOLD(label, delay, fL, fE);
	  }
	  
}
