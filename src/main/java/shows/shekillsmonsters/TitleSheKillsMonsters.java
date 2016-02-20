package shows.shekillsmonsters;

import shows.troubledwater.TWCueStack;

import com.danielbchapman.layers.BleedingCanvasLayer;
import com.danielbchapman.physics.toxiclibs.MotionEngine;

public class TitleSheKillsMonsters extends BleedingCanvasLayer
{

	TWCueStack stack;

	public TitleSheKillsMonsters(MotionEngine engine)
	{
		super(engine);

		stack = new TWCueStack(this)
		{
      @Override
			public void load()
			{
				//CharacterBrush pen = new CharacterBrush();
				
//				add(load(40, 0, 533, 400, "One", "show/prologue/leaf-1", pen),
//						load(40, -25, 533, 533, "Leaf", "show/prologue/leaf-2", pen, Actions.follow(3000)));
			}
		};

		stack.load();
	}

	@Override
	public String getName()
	{
		return "SheKillsMonstersTitle";
	}

	public void go(MotionEngine engine)
	{
		stack.go(engine, this);
	}

}
