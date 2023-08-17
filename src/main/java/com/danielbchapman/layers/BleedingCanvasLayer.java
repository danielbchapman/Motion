package com.danielbchapman.layers;

import processing.core.PGraphics;

import com.danielbchapman.brushes.EllipseBrush;
import com.danielbchapman.brushes.ImageBrush;
import com.danielbchapman.motion.core.BaseScene;
import com.danielbchapman.motion.core.Motion;

public class BleedingCanvasLayer extends BaseScene {
	boolean blank;
	int mode = 0;

	@Override
	public void draw(PGraphics g) 
	{
		if (!blank) {
			g.background(0);
			try {
				// PImage image = engine.loadImage("show/OneLeaf.PNG");
	//        image.filter(PConstants.INVERT);
	//        g.image(image, 100, 50, 600, 600);
			} catch (Throwable t) {
				t.printStackTrace();
			}
			blank = true;
		}
	}

	public void go(Motion engine) {
		System.out.println("GO FIRED");
		if (mode % 2 == 0) {
			ImageBrush brush = new ImageBrush();
			engine.setCurrentBrush(brush);
		} else {
			EllipseBrush brush = new EllipseBrush();
			engine.setCurrentBrush(brush);
		}
		mode++;

		blank = false;// clear the canvas
	}
}
