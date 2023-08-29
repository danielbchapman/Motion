package com.danielbchapman.motion.core;

import java.util.ArrayList;

import processing.core.PGraphics;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Cue extends AbstractCue<Cue>
{
  private float timeScale = 1f;
  
  private transient Playback2017 playback;
  
  @Override
  public void start(Motion motion, long time)
  {
    if(!loaded)
      load(motion);

    if(playback == null)
    {
      inError = true;
      return;
    }
    
    startTime = time;
    playback.start(time);
  }


  @Override
  public void load(Motion motion)
  {
  	ArrayList<RecordAction2017> actions = new ArrayList<>();
  	MotionBrush brush;
  	try {
  		if(this.pathFile != null) {
    		actions = Recorder2017.load(pathFile, motion.width, motion.height, 0, 0);
    	} 	
  	} catch (Throwable t) {
  		Log.severe("Unable to load path file " + pathFile, t);
  	}
  	
  	if(this.pathFile != null) {
  		actions = Recorder2017.load(pathFile, motion.width, motion.height, 0, 0);
  	} 
  	
  	if(this.brushFile != null) {
  		brush = MotionBrush.loadFromFile(brushFile);
  	} else {
  		brush = motion.currentBrush.clone();
  	}
  	
    previewActions = null;
    playback = new Playback2017(pathFile, motion, brush, actions);
  }


  @Override
  public void update(Motion motion, long time)
  { 
  }

  private transient float previewW = -1;
  private transient float previewH = -1;
  private transient float previewX = -1;
  private transient float previewY = -1;
  private transient float previewSx = -1;
  private transient float previewSy = -1;
  private transient ArrayList<RecordAction2017> previewActions;
  
  /* (non-Javadoc)
   * @see com.danielbchapman.motion.core.Cue#preview(processing.core.PGraphics, com.danielbchapman.motion.core.Motion, long)
   */
  @Override
  public void preview(PGraphics g, Motion motion, long startTime)
  {
    //Check for updates
    if(previewW < 0){
      if(size.x <= 0)
        size.x = motion.WIDTH;
      if(size.y <= 0)
        size.y = motion.HEIGHT;
      
      if(previewX < 0)
        previewX = 0;
      
      if(previewY < 0)
        previewY = 0;
      
      previewW = size.x;
      previewH = size.y;
      previewX = position.x;
      previewY = position.y;
      previewSx = scale.x;
      previewSy = scale.y;
    }
    
    if(previewW != size.x)
    {
      previewActions = null;
      previewW = size.x;
    }
    
    if(previewH != size.y)
    {
      previewActions = null;
      previewH = size.y;
    }
    
    if(previewX != position.x)
    {
      previewActions = null;
      previewX = position.x;
    }
    
    if(previewY != position.y)
    {
      previewActions = null;
      previewY = position.y;
    }
    
    if(previewSx != scale.x)
    {
      previewActions = null;
      previewSx = scale.x;      
    }
    
    if(previewSy != scale.y)
    {
      previewActions = null;
      previewSy = scale.y;      
    }
    
    if(previewActions == null)
    {
      previewActions = Recorder2017.load(pathFile, size.x * scale.x, size.y * scale.y, position.x, position.y);
    }
    
    if(previewActions.size() < 1)
    {
      return;
    }
    
    RecordAction2017 last = previewActions.get(0);
    if(!last.centerClick && !last.rightClick && !last.leftClick)
      last = null;
    
    g.clear();
    g.stroke(0, 255, 255);
    g.strokeWeight(3);
    
    g.fill(0,255,255);
    
    //show data
    g.text(
        String.format(
            "ANCHOR[ (%1$.0f, %2$.0f) ] SIZE[ (%3$.0f, %4$.0f) ]", 
            anchor.x, 
            anchor.y, 
            size.x, 
            size.y),
          //Position
          20, 
          20);
    if(previewActions.size() == 1 && last != null)
    {
      g.point(last.x, last.y);
      return;
    }
    for(int i = 1; i < previewActions.size(); i++)
    {
      RecordAction2017 current = previewActions.get(i);
      
      if(!current.centerClick && !current.leftClick && !current.rightClick)
      {
        continue;
      }
      
      if(last != null)
      {
        g.line(last.x, last.y, current.x, current.y);
      }
      else
      {
        g.point(current.x, current.y);
      }
      last = current;
    }
    //FIXME this would be good to have a "timeline" preview, but right now I'm just drawing the path.
  }  
}
