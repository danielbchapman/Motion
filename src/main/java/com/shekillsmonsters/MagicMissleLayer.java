package com.shekillsmonsters;

import processing.core.PApplet;

import com.danielbchapman.physics.toxiclibs.SpriteLayer;

public class MagicMissleLayer extends SpriteLayer
{

	public MagicMissleLayer(PApplet app)
	{
		super(app);
		image = app.loadImage("test/sprite/sprite.png");
	}

}
