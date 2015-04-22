package com.danielbchapman.physics.toxiclibs;

import java.util.ArrayList;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.danielbchapman.utility.FileUtil;

@Data
@EqualsAndHashCode(callSuper = true, doNotUseGetters = true)
public class PlaybackCue extends Cue
{
  String brushFile;
  String motionFile;
  String label;
  Cue cue;
  MotionInteractiveBehavior brush;
  ArrayList<RecordAction> actions;
  
  public Cue loadCue(int w, int h, int offsetX, int offsetY) 
  {
    if(cue != null)
      return cue;
    
    brush = MotionInteractiveBehavior.load(FileUtil.readFile(brushFile));
    actions = Recorder.load(motionFile, w, h, offsetX, offsetY);
    
    return cue;
  }
  public PlaybackCue(String label, String brushFile, String motionFile)
  {
    super(label, null);
    this.label = label;
    this.brushFile = brushFile;
    this.motionFile = motionFile;
  }
  
  public static String toLine(PlaybackCue cue)
  {
    StringBuilder b = new StringBuilder();
    b.append(cue.label);
    b.append(",");
    b.append(cue.brushFile);
    b.append(",");
    b.append(cue.motionFile);
    
    return b.toString();
  }
  
  public static PlaybackCue fromLine(String line)
  {
    String[] parts = line.split(",");
    int i = 0;
    PlaybackCue ret = new PlaybackCue(parts[i++], parts[i++], parts[i++]);
    
    return ret;
  }

}
