package com.danielbchapman.motion.core;

import java.util.ArrayList;

import processing.core.PGraphics;

public class PlaybackCue extends AbstractCue<PlaybackCue>
{
	public String recordFilePath;
	public String brushFilePath;
	public String name;
	public Integer x;
	public Integer y;
	public Integer w;
	public Integer h;
	
	private volatile MotionBrush brush;
	private volatile ArrayList<RecordAction2017> actions;
	
	@Override
	public void load(Motion motion)
	{
		int _x = 0;
		int _y = 0;
		int _w = Motion.WIDTH;
		int _h = Motion.HEIGHT;
		
		if(x != null)
			_x = x;
		
		if(y != null) 
			_y = y;
		
		if(w != null) 
			 _w = w;
		if(h != null)
			_h = h;
		
		actions = Recorder2017.load(recordFilePath, _w, _h, _x, _y);
		brush = MotionBrush.loadFromFile(brushFilePath);		
	}

	@Override
	public void start(Motion motion, long time)
	{
		motion.runPlayback(name, actions, brush);
	}

	@Override
	public void update(Motion motion, long time)
	{		
	}

	@Override
	public void preview(PGraphics g, Motion motion, long time)
	{		
	}
}
