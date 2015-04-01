package com.danielbchapman.physical;

/**
 * A step that causes something to happen in the user interface. These actions have types
 * @author chapm_000
 *
 */
public class Action 
{
	public enum Type
	{
		MOUSE_DOWN,
		MOUSE_DOWN_RIGHT,
		MOUSE_UP,
		MOUSE_UP_RIGHT,
		PAINT,
		POINT,
		VERTEX
	}
}
