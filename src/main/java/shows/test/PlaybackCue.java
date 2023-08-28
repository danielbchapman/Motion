package shows.test;

import java.io.File;
import java.util.ArrayList;

import com.danielbchapman.motion.core.BrushEditor2017;
import com.danielbchapman.motion.core.MotionBrush;
import com.danielbchapman.motion.core.Playback2017;
import com.danielbchapman.motion.core.RecordAction2017;
import com.danielbchapman.motion.core.Recorder2017;
import com.danielbchapman.physics.toxiclibs.ActionsOLD;

public class PlaybackCue
{
  public ArrayList<RecordAction2017> actions;
  public MotionBrush brush;
  public String name;
  
  public PlaybackCue(String recordingFile, String brushFile, String name, int w, int h, int offsetX, int offsetY)
  {
    this.actions = Recorder2017.load(recordingFile, w, h, offsetX, offsetY);
    this.brush = MotionBrush.loadFromFile(brushFile);
    this.name = name;
  }
}
