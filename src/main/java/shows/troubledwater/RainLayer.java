package shows.troubledwater;

import java.util.ArrayList;

import com.danielbchapman.layers.BleedingCanvasLayer;
import com.danielbchapman.physics.toxiclibs.Actions;
import com.danielbchapman.physics.toxiclibs.MotionEngine;
import com.danielbchapman.physics.toxiclibs.Point;

import processing.core.PGraphics;
import toxi.geom.Vec3D;

public class RainLayer extends BleedingCanvasLayer
{
  public RainLayer(MotionEngine engine)
  {
    super(engine);
  }

  boolean first = true;
  ArrayList<RainEmitter> rain;
  TWCueStack stack;

  ArrayList<BrushPoint> list = new ArrayList<>();
  
  @Override
  public Point[] init()
  {
	  if(rain == null)
		  rain = new ArrayList<>();
	  rain.add(new RainEmitter(this, new Vec3D(200, 200, 0), Vec3D.randomVector(), 15000, 1000, 2f, 1000));
	  rain.add(new RainEmitter(this, new Vec3D(-100, 150, 0), Vec3D.randomVector(), 20000, 1000, 2f, 2000));
	  rain.add(new RainEmitter(this, new Vec3D(600, 75, 0), Vec3D.randomVector(), 10000, 1000, 2f, 3000));
    
	  if(stack == null)
	  {
		  stack = 
				  new TWCueStack(this)
				  {
					private static final long serialVersionUID = 1L;

					@Override
					public void load() {
						add(
//							cue("Rain Falls",
//								null,
//								Actions.dragTo(0.5f),
//								Actions.gravity.setGravity(new Vec3D(.45f, .85f, 0))
//							),
//							cue("Harder Rain",
//									null,
//									Actions.dragTo(0f),
//									Actions.follow(500)
//									),
//							cue("Freeze Paint")
//								.addAction(new Action());
//							
//								
//								cue("Basic Rain--do nothing");
								//basic rain
								//Harder rain
								//freeze rain, stop bleeding away
								//76 Start the rain allow bleeding and stop again
								//82 a large brush runs over the stage--stop the bleeding
								//85 start an actual bleed effect on the rain --per processing
								);
					}
			  
				  };
				  
				  stack.load();
	  }
	  
    return new Point[0];
  }

  @Override
  public void render(PGraphics g)
  {
    if (first)
    { 
    	g.background(0);
      first = false;
    }
    g.fill(0,0,0, 2);
    g.stroke(0,0,0, 2);
    g.rect(0, 0, this.engine.getWidth(), this.engine.getHeight());
    for(RainEmitter e : rain)
    	e.draw(g);      
  }

  @Override
  public void update()
  {
    long sysTime = System.currentTimeMillis();
    for(RainEmitter e : rain)
    	e.update(sysTime);

  }

  @Override
  public void go(MotionEngine engine)
  {
    // engine.addBehavior(Actions.gravity);
    engine.getPhysics().setDrag(0.5f);
    Actions.gravity.setGravity(new Vec3D(.45f, .85f, 0));

    try
    {
      engine.getPhysics().addBehavior(Actions.gravity);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
  
  @Override
  public String getName()
  {
    return "RainLayer";
  }
}

