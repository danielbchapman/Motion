package shows.shekillsmonsters;

import processing.core.PApplet;
import shows.troubledwater.TWCueStack;

import com.danielbchapman.physics.toxiclibs.MotionEngine;
import com.danielbchapman.physics.toxiclibs.SpriteLayer;

public class HealingSpellLayer extends SpriteLayer
{
	TWCueStack stack;

	public HealingSpellLayer(PApplet app)
	{
		super(app);
		super.image = app.loadImage("test/sprite/pony.png");
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
					load("Load Variables", 
						"show/healing/healing.env",
						"show/healing/healing",
						"show/healing/healing-brush")
				);
			}
	
		};
		
		stack.load();
	}

	public void go(MotionEngine engine)
	{
		stack.go(engine, this);
		
	}
	
	public String getName()
	{
		return "HealingSpell";
	}
}
