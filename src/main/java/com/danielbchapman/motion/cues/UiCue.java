package com.danielbchapman.motion.cues;

import lombok.Getter;
import lombok.Setter;

public class UiCue 
{
	@Getter
	@Setter
	private String label;
	@Getter
	@Setter
	private String description;
	
	@Getter
	@Setter
	private float number;
	
	//Timing Section
	private Long delay;
	private Long follow;
	private Long wait;
}
