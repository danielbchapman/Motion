package com.danielbchapman.motion.cues;

import java.util.ArrayList;

public class UiCueStackModel 
{
	private ArrayList<UiCue> stack = new ArrayList<>();
	
	public void addCue(UiCue cue)
	{
		stack.add(new UiCue());
	}

	public void createCue() 
	{
		System.out.println("Adding Cue");
		stack.add(new UiCue());
	}
	
}
