package shows.troubledwater;

import processing.core.PGraphics;

import com.danielbchapman.layers.BleedingCanvasLayer;
import com.danielbchapman.physics.toxiclibs.MotionEngine;

public class HeroLayer extends BleedingCanvasLayer
{
	TWCueStack stack;
	public HeroLayer(MotionEngine engine) {
		super(engine);
		stack = new TWCueStack(this)
				{
		      SmallCharacterBrush pen = new SmallCharacterBrush();
					
					@Override
					public void load() {
					  
					  //PRESET
					  add(
					    cue("PRESET CUE..."),		

					    //SEQUENCE 1
							load(450, -30, 533, 350,
							    "1", 
							    "show/hero/hero-1-1", 
							    pen
							    ),
							    
							load(370, -120, 533, 450,
							    "2", 
									"show/hero/hero-1-2", 
									pen),
									
							load(250, -200, 533, 450,
							    "1", 
									"show/hero/hero-1-3", 
									pen),
									
							//SEQUENCE 2
	             load(350, -40, 400, 300,
	                  "1", 
	                  "show/hero/hero-2-1", 
	                  pen
	                  ),
	                  
	              load(210, -75, 400, 300,
	                  "2", 
	                  "show/hero/hero-2-2", 
	                  pen),
	                  
	              load(40, 150, 533, 400,
	                  "1", 
	                  "show/hero/hero-2-3", 
	                  pen),		
	                  
                load(130, -80, 400, 300,
                    "1", 
                    "show/hero/hero-2-4", 
                    pen),   
                load(-20, -220, 533, 450,
                    "1", 
                    "show/hero/hero-2-5", 
                    pen),   
									
							//SEQUENCE 3		
							load(20, -20, 400, 300,
							    "3", 
									"show/hero/hero-3-1", 
									pen),
									
							load(0, -20, 400, 300,
							    "3", 
									"show/hero/hero-3-2", 
									pen),
									
							load(-220, -30, 400, 300,
							    "3", 
									"show/hero/hero-3-3", 
									pen),
									
							load(-220, 0, 400, 300,
							    "3", 
									"show/hero/hero-3-4", 
									pen)
						);
					}
			
				};
		stack.load();
		this.go(engine);
	}
	
	public void go(MotionEngine engine)
	{
		stack.go(engine, this);
	}
	
	public void render(PGraphics g)
	{
	  super.render(g);
	  
	  //draw 50 px grid;
	  g.color(255,0,0);
	  g.stroke(255,0,0);
	  
//	  for(int i = 0; i < 600; i+=50)
//	  {
//	    g.line(0, i, 800, i);
//	  }
//	  
//	  for(int i = 0; i < 800; i+=50)
//    {
//      g.line(i, 0, i, 800);
//    }
	}
	
	public String getName()
	{
		return "HeroLayer";
	}
}
