package shows.ululations;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;
import processing.core.PConstants;
import processing.core.PGraphics;
import shows.troubledwater.BrushPoint;
import shows.troubledwater.TWCueStack;
import toxi.geom.Vec3D;

import com.danielbchapman.brushes.old.ImageBrush;
import com.danielbchapman.layers.BleedingCanvasLayer;
import com.danielbchapman.motion.core.Motion;
import com.danielbchapman.physics.toxiclibs.ActionOLD;
import com.danielbchapman.physics.toxiclibs.ActionsOLD;
import com.danielbchapman.physics.toxiclibs.MotionEngine;
import com.danielbchapman.physics.toxiclibs.Point;

public class RainLayer extends BleedingCanvasLayer
{
  boolean first = true;
  boolean hardRain = false;
  boolean stopRain = false;
  ArrayList<RainEmitter> rain;
  ArrayList<RainEmitter> hard;
  
  FunctionalCueStack stack;

  ArrayList<BrushPoint> list = new ArrayList<>();
  
  @Override
  public Point[] init()
  {
	  if(rain == null)
		  rain = new ArrayList<>();
	  rain.add(new RainEmitter(this, new Vec3D(200, 200, 0), Vec3D.randomVector(), 15000, 1000, 2f, 1000));
	  rain.add(new RainEmitter(this, new Vec3D(-100, 150, 0), Vec3D.randomVector(), 20000, 1000, 2f, 2000));
	  rain.add(new RainEmitter(this, new Vec3D(600, 75, 0), Vec3D.randomVector(), 10000, 1000, 2f, 3000));
    
	  if(hard == null)
	    hard = new ArrayList<>();
	  
	  hard.add(new RainEmitter(this, new Vec3D(100, 200, 0), Vec3D.randomVector(), 15000, 1000, 2f, 200));
	  hard.add(new RainEmitter(this, new Vec3D(-0, 150, 0), Vec3D.randomVector(), 20000, 1000, 2f, 150));
	  hard.add(new RainEmitter(this, new Vec3D(450, 75, 0), Vec3D.randomVector(), 10000, 1000, 2f, 100));
	  
	  if(stack == null)
	  {
		  stack = 
				  new FunctionalCueStack(this)
				  {
  					@Override
  					public void load() {
  					  ImageBrush brush = new ImageBrush();
  						add(
  							cue("Rain Falls",
  								ActionsOLD.dragTo(0.5f),
  								startBleed(),
  								ActionsOLD.gravityTo(new Vec3D(.45f, .85f, 0)),
  								ActionsOLD.gravityOn,
  								startRain()
  							),
  							
  							cue("Harder Rain", //---Add 5 or so emitters for 1 seconds
  									ActionsOLD.follow(500),
  									hardOn(),
  									ActionsOLD.gravityTo(new Vec3D(.45f, .85f, 0).scale(2f)),
  									ActionsOLD.gravityOn
								),
  							cue("Freeze Paint",
  							    stopBleed(),
  							    ActionsOLD.follow(1000)
						    ),
  							cue("AF: Stop Rain", 
  							    hardOff(),
                    stopRain()
                ),
  							
  							cue("76 Start Rain again, bleeding starts",
  							    startBleed(),
  							    startRain(),
  							    ActionsOLD.dragTo(0.5f),
  							    ActionsOLD.gravityTo(new Vec3D(.45f, .85f, 0)),
                    ActionsOLD.gravityOn),
                    
                cue("82 A large Brush runs over the stage", 
                    ActionsOLD.gravityOff,
                    stopRain(),
                    ActionsOLD.dragToNone,
                    ActionsOLD.follow(0)),
                    
                load("AF->PaintSmear", 
                    "show/rain/paint-slash", 
                    brush, 
                    ActionsOLD.follow(500)),
                
                cue("AF->Stop Bleed", 
                    stopBleed()),
                    
                cue("85 Bleed Effect", startBleed())
                );
  								//basic rain
  								//Harder rain
  								//freeze rain, stop bleeding away
  								//76 Start the rain allow bleeding and stop again
  								//82 a large brush runs over the stage--stop the bleeding
  								//85 start an actual bleed effect on the rain --per processing
  					}
			  
				  };
				  
				  stack.load();
	  }
	  
    return new Point[0];
  }

  @Getter
  @Setter
  boolean bleeding = true;
  
  public ActionOLD stopRain()
  {
    return new  ActionOLD("Rain STOP", 0, (x)->{stopRain = true;}, null);
  }
  public ActionOLD startRain()
  {
    return new  ActionOLD("Rain START", 0, (x)->{stopRain = false;}, null);
  }
  
  public ActionOLD hardOn()
  {
    return new  ActionOLD("Hard Starts", 0, (x)->{hardRain = true;}, null);
  }
  public ActionOLD hardOff()
  {
    return new  ActionOLD("Hard Ends", 0, (x)->{hardRain = false;}, null);
  }
  public ActionOLD startBleed()
  {
    return new ActionOLD("Start Bleeding", 0, (x)->{bleeding = true;}, null);
  }
  
  public ActionOLD stopBleed()
  {
    return new ActionOLD("Start Bleeding", 0, (x)->{bleeding = false;}, null);
  }
  
  @Override
  public void render(PGraphics g)
  {
	//super.draw(g);//Black background if first pass
    if (first)
    { 
      g.background(0);
      first = false;
    }
    
    if(bleeding)
    {
      g.fill(0,0,0, 2);
      g.stroke(0,0,0, 2);
      g.rectMode(PConstants.CORNER);
      g.rect(0, 0, ActionsOLD.WIDTH, ActionsOLD.HEIGHT);
        
    }
    
    for(RainEmitter e : rain)
    	e.draw(g);      
    
    for(RainEmitter e : hard)
      e.draw(g);
  }

  @Override
  public void update(long time)
  {
    if(stopRain)
      return;
    
    for(RainEmitter e : rain)
    	e.update(time);
    
    if(hardRain)
      for(RainEmitter e : hard)
        e.update(time);

  }

  @Override
  public void go(Motion engine)
  {
    stack.go(engine, this);
    return;
//    // engine.addBehavior(Actions.gravity);
//    engine.getPhysics().setDrag(0.5f);
//    Actions.gravity.setGravity(new Vec3D(.45f, .85f, 0));
//
//    try
//    {
//      engine.getPhysics().addBehavior(Actions.gravity);
//    }
//    catch (Exception e)
//    {
//      e.printStackTrace();
//    }
  }
  
  @Override
  public String getName()
  {
    return "RainLayer";
  }
}

