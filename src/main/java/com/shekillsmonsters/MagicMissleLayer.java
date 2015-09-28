package com.shekillsmonsters;

import processing.core.PApplet;
import shows.troubledwater.TWCueStack;

import com.danielbchapman.physics.toxiclibs.Actions;
import com.danielbchapman.physics.toxiclibs.MotionEngine;
import com.danielbchapman.physics.toxiclibs.SpriteLayer;

public class MagicMissleLayer extends SpriteLayer
{
	TWCueStack stack;
	public MagicMissleLayer(PApplet app)
	{
		super(app);
		super.image = app.loadImage("test/sprite/white.png");
	    quad = app.createShape();
	    quad.beginShape();
	    quad.noFill();
	    quad.noStroke();
	    quad.tint(255, 64);
	    quad.texture(image);
	    quad.vertex(-50, -50, 0, 0, 0);
	    quad.vertex(50, -50, 0, image.width, 0);
	    quad.vertex(50, 50, 0, image.width, image.height);
	    quad.vertex(-50, 50, 0, 0, image.height);
	    quad.endShape();
	    
	    stack = new TWCueStack(this)
	    		{

					@Override
					public void load() {
						add(
								load("[1] Spell To Tilly",
									 "show/missle/magic-missles.env",
									 "show/missle/magic-missle-start",
									 "show/missle/magic-missle-brush"),
								//Slow down the world
								cue("STOP PLAYBACK", 
										Actions.stopPlayback(), 
										Actions.follow(200)),
								cue("Stop Physics", 
										Actions.follow(200),
										Actions.dragTo(0.069f),
										Actions.homeOff,
										Actions.homeLinearOff),
								load("[2] Spell Slows Over Stage",
										"show/missle/magic-missles.env",
										 "show/missle/missle-anchor",
										 "show/missle/missle-brush-new")
								);
					}
	    	
	    		};
	    		
	    		stack.load();
	}

	public String getName()
	{
		return "MagicMissle";
	}
	
	public void go(MotionEngine e)
	{
		stack.go(engine, this);
	}

}
