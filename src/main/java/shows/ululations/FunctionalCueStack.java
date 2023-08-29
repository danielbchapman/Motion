package shows.ululations;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.danielbchapman.motion.core.Action;
import com.danielbchapman.motion.core.Actions;
import com.danielbchapman.motion.core.AbstractCue;
import com.danielbchapman.motion.core.CueStack;
import com.danielbchapman.motion.core.Motion;
import com.danielbchapman.motion.core.MotionBrush;
import com.danielbchapman.motion.core.Cue;
import com.danielbchapman.motion.core.Scene;

public abstract class FunctionalCueStack extends CueStack
{
  public FunctionalCueStack(Scene scene)
  {
//    String brush = "show/scene3/scene-3-paint";
//    CourtesanLayer.this.engine.brush = new ImageBrush();
    
//    add(
//      load("Stroke 1",
//        "show/scene3/3-1",
//        brush,
//        Actions.follow(1000)),
//      load("Stroke 2",
//          "show/scene3/3-2",
//          brush,
//          Actions.follow(1000)),
//      load("Stroke 3",
//          "show/scene3/3-3",
//          brush,
//          Actions.follow(1000)),
//      load("Stroke 4",
//          "show/scene3/3-4",
//          brush,
//          Actions.follow(1000)),
//      load("Stroke 5",
//          "show/scene3/side-stroke",
//          brush,
//          Actions.follow(1000)),
//      load("Stroke 6",
//          "show/scene3/3-6",
//          brush,
//          Actions.follow(1000)),
//      load("Stroke 7",
//          "show/scene3/long-stroke",
//          brush,
//          Actions.follow(1000))
//    );
  }

  public abstract void load();
  public AbstractCue<?> cue(String label, Action ... actions)
  {
    return cue(label, null, actions);
  }
  
  public AbstractCue<?> cue(String label, List<Action> post, Action ... pre)
  {
    ArrayList<Action> list = new ArrayList<Action>();
    if(pre != null)
      for(Action a : pre)
        list.add(a);
    
    if(post != null)
      for(Action a : post)
        list.add(a);
    
    AbstractCue<?> cue = new Cue();
    cue.label = label;
    cue.actions = list;
    return cue;
  }
  
  public AbstractCue<?> load(String label, String env, String file, String brushFile, Action ... post)
  {
    ArrayList<Action> acts = Actions.loadRecordingAsAction(file, brushFile);
    Action loadEnv = Actions.loadEnvironment(new File(env));
    if(post != null)
      for(Action a : post)
        acts.add(a);
    return cue(label, acts, loadEnv);
  }
  
  public AbstractCue<?> load(String label, String file, String brush)
  {
    ArrayList<Action> acts = Actions.loadRecordingAsAction(file, brush);
    return cue(label, acts);
  }
  
  public AbstractCue<?> load(String file, String brushFile)
  {
    return Actions.loadRecording(new File(file), new File(brushFile));
  }
  
  public AbstractCue<?> load(int x, int y, int w, int h, String label, String file, String brush, Action... post)
  {
    ArrayList<Action> acts = Actions.loadRecordingAsAction(label, x, y, w, h, file, brush);
    if(post != null)
      for(Action a : post)
        acts.add(a);
    return cue(label, acts);
  }
  
  public AbstractCue<?> load(String label, String file, MotionBrush brush, Action ... post)
  {
    ArrayList<Action> acts = Actions.loadRecordingAsAction(file, brush);
    if(post != null)
      for(Action a : post)
        acts.add(a);
    return cue(label, acts);
  }
  
  public AbstractCue<?> load(String label, String file, String brushFile, Action ... post)
  {
    ArrayList<Action> acts = Actions.loadRecordingAsAction(file, brushFile);
    if(post != null)
      for(Action a : post)
        acts.add(a);
    return cue(label, acts);
  }

  public Action action(String label, Consumer<Scene> fL, Consumer<Motion> fE)
  {
    return new Action(label, 0, fL, fE);
  }
  public Action action(String label, Consumer<Scene> fL, Consumer<Motion> fE, int delay)
  {
    return new Action(label, delay, fL, fE);
  }
}
