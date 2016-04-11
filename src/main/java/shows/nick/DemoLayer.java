package shows.nick;

import java.util.ArrayList;

import processing.core.PGraphics;

import com.danielbchapman.motion.utility.GraphicsUtility;
import com.danielbchapman.physics.toxiclibs.Action;
import com.danielbchapman.physics.toxiclibs.Actions;
import com.danielbchapman.physics.toxiclibs.Cue;
import com.danielbchapman.physics.toxiclibs.CueStack;
import com.danielbchapman.physics.toxiclibs.Layer;
import com.danielbchapman.physics.toxiclibs.MotionEngine;
import com.danielbchapman.physics.toxiclibs.Point;

public class DemoLayer extends Layer
{
	int red = 0xFFFF0000;
	int green = 0xFF00FF00;
	
	int activeColor = red;
	CueStack stack; 
	
	ArrayList<Point> grid = GraphicsUtility.createMotionGrid(20, 10, 10, 10, 0, 1, GraphicsUtility::point);
	
	public DemoLayer()
	{
		super();

		points = new Point[grid.size()];
		for(int i = 0; i < points.length; i++)
			points[i] = grid.get(i);
	
		stack = new CueStack();
		
		for(int i = 0; i < 10; i++)
		{
			Action sawp = new Action("Swap Color", 1000, 
					(layer)->
					{
						if(activeColor == red)
							activeColor = green;
						else
							activeColor = red;
					}, 
					(engine)->
					{
						System.out.println("Engine Function");
					});
				
			ArrayList<Action> acts = new ArrayList<Action>();
			acts.add(sawp);
			
			Cue swapColor = new Cue("Change Color", acts);
			stack.add(swapColor);
		}
	}
	
	@Override
	public Point[] init()
	{
		return null;
	}

	@Override
	public void render(PGraphics g)
	{
	  g.background(activeColor);
	  g.stroke(255);
	  g.strokeWeight(5f);
	  g.line(0,0, Actions.WIDTH, Actions.HEIGHT);
	  
	  for(Point p : points)
	  {
	  	g.point(p.x, p.y, p.z);
	  }
	}

	@Override
	public void update()
	{
	}

	@Override
	public void go(MotionEngine engine)
	{
		stack.go(engine, this);
	}

	@Override
	public String getName()
	{
		return "DemoLayer";
	}
}
