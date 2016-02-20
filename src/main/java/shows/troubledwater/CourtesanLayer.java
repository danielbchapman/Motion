package shows.troubledwater;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.danielbchapman.brushes.ImageBrush;
import com.danielbchapman.layers.BleedingCanvasLayer;
import com.danielbchapman.physics.toxiclibs.Action;
import com.danielbchapman.physics.toxiclibs.Actions;
import com.danielbchapman.physics.toxiclibs.Cue;
import com.danielbchapman.physics.toxiclibs.CueStack;
import com.danielbchapman.physics.toxiclibs.Layer;
import com.danielbchapman.physics.toxiclibs.MotionEngine;
import com.danielbchapman.physics.toxiclibs.Point;

import processing.core.PGraphics;

public class CourtesanLayer extends BleedingCanvasLayer {

	boolean first = true;
	Stack stack;
	public CourtesanLayer(MotionEngine engine) {
		super(engine);
		stack = new Stack();
	}

	@Override
	public Point[] init() {
		return new Point[0];
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
					Actions.follow(1000)),
				load("Stroke 2",
						"show/scene3/3-2",
						brush,
						Actions.follow(1000)),
				load("Stroke 3",
						"show/scene3/3-3",
						brush,
						Actions.follow(1000)),
				load("Stroke 4",
						"show/scene3/3-4",
						brush,
						Actions.follow(1000)),
				load("Stroke 5",
						"show/scene3/side-stroke",
						brush,
						Actions.follow(1000)),
				load("Stroke 6",
						"show/scene3/3-6",
						brush,
						Actions.follow(1000)),
				load("Stroke 7",
						"show/scene3/long-stroke",
						brush,
						Actions.follow(1000))
			);
		}
	}
	
	public Cue cue(String label, Action ... actions)
	  {
	    return cue(label, null, actions);
	  }
	  public Cue cue(String label, List<Action> post, Action ... pre)
	  {
	    ArrayList<Action> list = new ArrayList<Action>();
	    if(pre != null)
	      for(Action a : pre)
	        list.add(a);
	    
	    if(post != null)
	      for(Action a : post)
	        list.add(a);
	    
	    Cue cue = new Cue(label, list);
	    return cue;
	  }
	  
	  public Cue load(String label, String env, String file, String brushFile, Action ... post)
	  {
	    ArrayList<Action> acts = Actions.loadRecordingAsAction(new File(file), new File(brushFile));
	    Action loadEnv = Actions.loadEnvironment(new File(env));
	    if(post != null)
	      for(Action a : post)
	        acts.add(a);
	    return cue(label, acts, loadEnv);
	  }
	  
	  public Cue load(String label, String file, String brush)
	  {
	    ArrayList<Action> acts = Actions.loadRecordingAsAction(new File(file), new File(brush));
	    return cue(label, acts);
	  }
	  
	  public Cue load(String file, String brushFile)
	  {
	    return Actions.loadRecording(new File(file), new File(brushFile));
	  }
	  
	  public Cue load(String label, String file, String brushFile, Action ... post)
	  {
	    ArrayList<Action> acts = Actions.loadRecordingAsAction(new File(file), new File(brushFile));
	    if(post != null)
	      for(Action a : post)
	        acts.add(a);
	    return cue(label, acts);
	  }
	  
	  public Cue load(String label)
	  {
	    return cue(label);
	  }
	  public Action action(String label, Consumer<Layer> fL, Consumer<MotionEngine> fE)
	  {
	    return new Action(label, 0, fL, fE);
	  }
	  public Action action(String label, Consumer<Layer> fL, Consumer<MotionEngine> fE, int delay)
	  {
	    return new Action(label, delay, fL, fE);
	  }
	  
}
