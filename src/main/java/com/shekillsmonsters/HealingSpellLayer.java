package com.shekillsmonsters;

import java.io.File;

import processing.core.PApplet;

import com.danielbchapman.physics.toxiclibs.Action;
import com.danielbchapman.physics.toxiclibs.Actions;
import com.danielbchapman.physics.toxiclibs.Cue;
import com.danielbchapman.physics.toxiclibs.MotionEngine;
import com.danielbchapman.physics.toxiclibs.MotionInteractiveBehavior;
import com.danielbchapman.physics.toxiclibs.SpriteLayer;

public class HealingSpellLayer extends SpriteLayer
{

	public HealingSpellLayer(PApplet app)
	{
		super(app);
		super.image = app.loadImage("test/sprite/sprite-light.png");
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
	}

	public void go(MotionEngine engine)
	{
		//reset variables
		MotionInteractiveBehavior beh = MotionInteractiveBehavior.load(new File("show/healing/healing-brush"));
		engine.brush = beh;
		
		Action env = Actions.loadEnvironment(new File("show/healing/healing.env"));
		Cue x = new Cue("Load env...", null);
		x.addAction(env);
		x.go(this,  engine);
		
	}
	
	public String getName()
	{
		return "HealingSpell";
	}
}
