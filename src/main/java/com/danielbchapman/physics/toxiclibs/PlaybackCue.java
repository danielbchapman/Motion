package com.danielbchapman.physics.toxiclibs;

import java.util.ArrayList;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.danielbchapman.utility.FileUtil;
import com.danielbchapman.utility.Utility;

@Data
@EqualsAndHashCode(callSuper = true, doNotUseGetters = true)
@SuppressWarnings("PlaybackCue and Cue structure makes no sense, probably this is a bad heirarchy.")
public class PlaybackCue extends Cue
{
  String brushFile;
  String motionFile;
  String label;
  @SuppressWarnings("Why is this variable here?")
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

  /** 
   * OVERRIDE
   * This checks the persistent data for the structure and compares that. The
   * Volatile data is not compared (list of actions)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  public boolean equals(Object o)
  {
    if(o == null)
      return false;
    
    if(!(o instanceof PlaybackCue ))
      return false;
    
    PlaybackCue other = (PlaybackCue) o;
    
    int ret = Utility.compareToNullSafe(brushFile, other.brushFile); 
    if(ret != 0)
      return false;
    
    ret = Utility.compareToNullSafe(motionFile, other.motionFile); 
    if(ret != 0)
      return false;
    
    ret = Utility.compareToNullSafe(label, other.label); 
    if(ret != 0)
      return false;

    
    return true;
  }
}
