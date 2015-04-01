package com.danielbchapman.physical;

import java.util.ArrayList;

public class Physical 
{
	
	/**
	 * A class that is concerned with recording and recreating a drawing
	 * @author chapm_000
	 *
	 */
	public class DrawnObjectRecorder
	{
		
	}
	/**
	 * An object that is concerned with recording user actions
	 * @author chapm_000
	 *
	 */
	public class UserInteractionRecorder
	{
		ArrayList<Frame> frames = new ArrayList<>();
		boolean isRecording;
		
		public void start()
		{
			
		}
		
		public void stop()
		{
			
		}
	}
	
	/**
	 * A collection of user actions that are recorded. These are then translated into 
	 * a sequence of events. This is used to capture a user's actions and translated to 
	 * Actions
	 * @author chapm_000
	 *
	 */
	public class Frame
	{
		
	}
}
