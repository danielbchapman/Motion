package shows.ululations;

import java.util.ArrayList;

import com.danielbchapman.brushes.ImageBrush;
import com.danielbchapman.layers.BleedingCanvasLayer;
import com.danielbchapman.motion.core.Action;
import com.danielbchapman.motion.core.Actions;
import com.danielbchapman.motion.core.Log;
import com.danielbchapman.motion.core.Motion;
import com.danielbchapman.physics.toxiclibs.ActionsOLD;

import lombok.Getter;
import lombok.Setter;
import processing.core.PConstants;
import processing.core.PGraphics;
import shows.troubledwater.BrushPoint;
import toxi.geom.Vec3D;
import toxi.physics3d.VerletPhysics3D;

public class RainLayer extends BleedingCanvasLayer
{
	boolean first = true;
	boolean hardRain = false;
	boolean stopRain = false;
	ArrayList<RainEmitter> rain;
	ArrayList<RainEmitter> hard;

	FunctionalCueStack stack;

	ArrayList<BrushPoint> list = new ArrayList<>();
	public VerletPhysics3D physics;
	
	@Override
	public boolean isVerletPhysic3D()
	{
		Log.info("CHECKING FOR VERLET");
		return true;
	}
	@Override
	public VerletPhysics3D get3DPhysics() {
		return physics;
	}
	
	@Override
	public void initialize(Motion motion)
	{
		physics = new VerletPhysics3D();
		if (rain == null)
			rain = new ArrayList<>();
		rain.add(new RainEmitter(this, new Vec3D(200, 200, 0), Vec3D.randomVector(), 15000, 1000, 2f, 1000));
		rain.add(new RainEmitter(this, new Vec3D(-100, 150, 0), Vec3D.randomVector(), 20000, 1000, 2f, 2000));
		rain.add(new RainEmitter(this, new Vec3D(600, 75, 0), Vec3D.randomVector(), 10000, 1000, 2f, 3000));

		if (hard == null)
			hard = new ArrayList<>();

		hard.add(new RainEmitter(this, new Vec3D(100, 200, 0), Vec3D.randomVector(), 15000, 1000, 2f, 200));
		hard.add(new RainEmitter(this, new Vec3D(-0, 150, 0), Vec3D.randomVector(), 20000, 1000, 2f, 150));
		hard.add(new RainEmitter(this, new Vec3D(450, 75, 0), Vec3D.randomVector(), 10000, 1000, 2f, 100));

		if (stack == null)
		{
			stack = new FunctionalCueStack(this)
			{
				@Override
				public void load()
				{
					ImageBrush brush = new ImageBrush();
					add(
							cue("Rain Falls", Actions.dragTo(0.5f), startBleed(), Actions.gravityTo(new Vec3D(.45f, .85f, 0)),
									Actions.gravityOn, startRain()),

							cue("Harder Rain", // ---Add 5 or so emitters for 1 seconds
									Actions.follow(500), hardOn(), Actions.gravityTo(new Vec3D(.45f, .85f, 0).scale(2f)),
									Actions.gravityOn),
							cue("Freeze Paint", stopBleed(), Actions.follow(1000)), cue("AF: Stop Rain", hardOff(), stopRain()),

							cue("76 Start Rain again, bleeding starts", startBleed(), startRain(), Actions.dragTo(0.5f),
									Actions.gravityTo(new Vec3D(.45f, .85f, 0)), Actions.gravityOn),

							cue("82 A large Brush runs over the stage", Actions.gravityOff, stopRain(), Actions.dragToNone,
									Actions.follow(0)),

							load("AF->PaintSmear", "show/rain/paint-slash", brush, Actions.follow(500)),

							cue("AF->Stop Bleed", stopBleed()),

							cue("85 Bleed Effect", startBleed()));
					// basic rain
					// Harder rain
					// freeze rain, stop bleeding away
					// 76 Start the rain allow bleeding and stop again
					// 82 a large brush runs over the stage--stop the bleeding
					// 85 start an actual bleed effect on the rain --per processing
				}

			};

			stack.load();
		}
		stack.setLabel("Rain Layer Stack");
	}

	@Getter
	@Setter
	boolean bleeding = true;

	public Action stopRain()
	{
		return new Action("Rain STOP", 0, (x) -> {
			stopRain = true;
		}, null);
	}

	public Action startRain()
	{
		return new Action("Rain START", 0, (x) -> {
			stopRain = false;
		}, null);
	}

	public Action hardOn()
	{
		return new Action("Hard Starts", 0, (x) -> {
			hardRain = true;
		}, null);
	}

	public Action hardOff()
	{
		return new Action("Hard Ends", 0, (x) -> {
			hardRain = false;
		}, null);
	}

	public Action startBleed()
	{
		return new Action("Start Bleeding", 0, (x) -> {
			bleeding = true;
		}, null);
	}

	public Action stopBleed()
	{
		return new Action("Start Bleeding", 0, (x) -> {
			bleeding = false;
		}, null);
	}

	@Override
	public void draw(PGraphics g)
	{
		// super.draw(g);//Black background if first pass
		if (first)
		{
			g.background(0);
			first = false;
		}

		if (bleeding)
		{
			g.fill(0, 0, 0, 2);
			g.stroke(0, 0, 0, 2);
			g.rectMode(PConstants.CORNER);
			g.rect(0, 0, ActionsOLD.WIDTH, ActionsOLD.HEIGHT);

		}

		for (RainEmitter e : rain)
			e.draw(g);

		for (RainEmitter e : hard)
			e.draw(g);
	}

	@Override
	public void update(long time)
	{
		super.update(time);
		if(physics != null)
		{
			physics.update();
		}
		
		if (stopRain)
			return;

		for (RainEmitter e : rain)
			e.update(time);

		if (hardRain)
			for (RainEmitter e : hard)
				e.update(time);
	}

	@Override
	public void go(Motion engine)
	{
		stack.go(this);
		return;
	}

	@Override
	public String getName()
	{
		return "RainLayer";
	}
}
