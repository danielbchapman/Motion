package com.shekillsmonsters;

import java.util.ArrayList;

import com.danielbchapman.physics.toxiclibs.Layer;
import com.danielbchapman.physics.toxiclibs.MotionEngine;
import com.danielbchapman.physics.toxiclibs.Util;

import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;

public class SpinningSquares extends Layer {

	ArrayList<Point> squares = new ArrayList<Point>();
	float spinSpeed = 0.01f;
	PImage image;
	public SpinningSquares(int width, int height) {
		
		for (int i = 0; i < 400; i++) {
			Point p = new Point();
			p.x = (int) Util.rand(0, width);
			p.y = (int) Util.rand(0, height);
			p.opacity = (int) Util.rand(32, 255);
			p.rotate = Util.rand(0, PConstants.PI);
			p.size = (int) Util.rand(4, 50);
			p.left = Util.rand(-1, 1) > 0;
			p.randomSpin = Util.rand(0.5f, 1.5f);
			squares.add(p);
		}
	}

	public void render(PGraphics g) {
		if(image == null)
		{
			image = engine.loadImage("test/sprite/pony.png");
		}
		g.background(0);
		for (Point p : squares) 
		{
			if (p.left)
				p.rotate += spinSpeed;
			else
				p.rotate -= spinSpeed;
			g.pushMatrix();
			g.stroke(255, 255, 255, p.opacity);
			g.fill(255, 255, 255, p.opacity);
			g.translate(p.x, p.y);
			g.rotate(p.rotate);
//			g.imageMode(PConstants.CENTER);
//			g.image(image, 0, 0, p.size, p.size);
			g.rectMode(PConstants.CENTER);
			g.rect(0, 0, p.size, p.size);

			g.popMatrix();
		}

	}

	class Point {
		int x, y, z;
		int opacity = 0;
		float rotate = 0;
		boolean left = false;
		int size;
		float randomSpin = 1.0f;
	}

	@Override
	public com.danielbchapman.physics.toxiclibs.Point[] init() {
		return new com.danielbchapman.physics.toxiclibs.Point[0];
	}

	@Override
	public void update() {
		
	}

	int step = 0;
	@Override
	public void go(MotionEngine engine) {
		int stepTo = step++ % 10;
		
		spinSpeed = ((float) stepTo) / 100;
		
	}
	
	@Override
	public String getName()
	{
		return "SpinningSquares";
	}
}
