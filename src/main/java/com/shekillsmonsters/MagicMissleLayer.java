package com.shekillsmonsters;

import processing.core.PApplet;

import com.danielbchapman.physics.toxiclibs.SpriteLayer;

public class MagicMissleLayer extends SpriteLayer
{

	public MagicMissleLayer(PApplet app)
	{
		super(app);
		super.image = app.loadImage("test/sprite/sprite.png");
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

	public String getName()
	{
		return "MagicMissle";
	}
}
